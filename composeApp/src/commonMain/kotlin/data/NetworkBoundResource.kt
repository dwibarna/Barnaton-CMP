package data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import utils.NetworkError
import utils.Result

abstract class NetworkBoundResource<ResultType, RequestType> {

    protected abstract fun loadFromDB(): Flow<ResultType>

    protected abstract suspend fun createCall(): Flow<Result<RequestType, NetworkError>>

    protected abstract suspend fun saveCallResult(data: RequestType)

    fun asFlow(): Flow<Resource<ResultType>> = flow {
        emit(Resource.Loading())
        when (val apiResponse = createCall().first()) {
            is Result.Error -> {
                emit(Resource.Error(message = apiResponse.error.toString()))
            }

            is Result.Success -> {
                saveCallResult(apiResponse.data)
                emitAll(
                    loadFromDB().map {
                        Resource.Success(it)
                    }
                )
            }
        }
    }
}