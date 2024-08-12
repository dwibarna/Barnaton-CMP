package data.source.remote.network

sealed class ApiResponse<out R> private constructor() {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val message: String) : ApiResponse<Nothing>()
}