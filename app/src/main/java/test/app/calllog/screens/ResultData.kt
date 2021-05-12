package test.app.calllog.screens

sealed class ResultData<T>(val data: T? = null, val message: String? = null) {

    class Success<T>(data: T? = null) : ResultData<T>(data)
    class Loading<T>(var isLoading: Boolean) : ResultData<T>()
    class Error<T>(data: T? = null, message: String) : ResultData<T>(data, message)
}