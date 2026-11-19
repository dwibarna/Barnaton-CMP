package data.source.remote

import domain.model.ConnectionStatus
import domain.model.WatchRoomState
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private const val WATCH_ROOM_WS_URL = "wss://ws.postman-echo.com/raw"
private const val HEARTBEAT_INTERVAL_MS = 8_000L

@Serializable
private data class WatchRoomPayload(
    val type: String,
    val roomId: Int,
    val viewers: Int
)

class WatchRoomSocketClient(private val httpClient: HttpClient) {
    private val json = Json { ignoreUnknownKeys = true }

    fun observeRoom(seriesId: Int): Flow<WatchRoomState> = flow {
        emit(WatchRoomState(status = ConnectionStatus.CONNECTING))
        try {
            httpClient.webSocket(urlString = WATCH_ROOM_WS_URL) {
                streamRoomSession(seriesId, this@flow)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            emit(WatchRoomState(status = ConnectionStatus.DISCONNECTED))
        }
    }

    private suspend fun DefaultClientWebSocketSession.streamRoomSession(
        seriesId: Int,
        collector: FlowCollector<WatchRoomState>
    ) {
        val initialViewers = 85 + (seriesId % 95)
        send(Frame.Text(json.encodeToString(WatchRoomPayload("join", seriesId, initialViewers))))
        val heartbeatJob = launchHeartbeat(seriesId, initialViewers)

        try {
            for (frame in incoming) {
                val textFrame = frame as? Frame.Text ?: continue
                val payload = decodePayload(textFrame.readText()) ?: continue
                collector.emit(
                    WatchRoomState(
                        status = ConnectionStatus.CONNECTED,
                        viewerCount = payload.viewers,
                        lastEvent = payload.type
                    )
                )
            }
        } finally {
            heartbeatJob.cancel()
        }
    }

    private fun DefaultClientWebSocketSession.launchHeartbeat(
        seriesId: Int,
        initialViewers: Int
    ): Job = launch {
        var viewers = initialViewers
        while (isActive) {
            delay(HEARTBEAT_INTERVAL_MS)
            viewers = (viewers + (-3..4).random()).coerceAtLeast(12)
            send(Frame.Text(json.encodeToString(WatchRoomPayload("heartbeat", seriesId, viewers))))
        }
    }

    private fun decodePayload(raw: String): WatchRoomPayload? =
        runCatching { json.decodeFromString<WatchRoomPayload>(raw) }.getOrNull()
}
