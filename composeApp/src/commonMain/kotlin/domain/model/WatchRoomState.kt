package domain.model

enum class ConnectionStatus {
    CONNECTING,
    CONNECTED,
    DISCONNECTED
}

data class WatchRoomState(
    val status: ConnectionStatus = ConnectionStatus.DISCONNECTED,
    val viewerCount: Int = 0,
    val lastEvent: String = ""
)
