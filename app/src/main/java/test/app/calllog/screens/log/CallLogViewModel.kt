package test.app.calllog.screens.log

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import test.app.calllog.data.CallLogRepository
import test.app.calllog.screens.ResultData
import timber.log.Timber

class CallLogViewModel(
    private val callLogRepository: CallLogRepository
) : ViewModel() {


    private val shouldReload = MutableLiveData<Boolean>()

    val callLogList = Transformations.switchMap(shouldReload) {
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(ResultData.Loading<Boolean>(true))

            try {
                emit(ResultData.Success(callLogRepository.getCallLogs()))
            } catch (e: Exception) {
                Timber.e(e)
                emit(ResultData.Error<Unit>(message = "Something went wrong, please try again."))
            }

            emit(ResultData.Loading<Boolean>(false))
        }
    }

    fun loadCallLog(reload: Boolean) {
        shouldReload.value = reload
    }

}