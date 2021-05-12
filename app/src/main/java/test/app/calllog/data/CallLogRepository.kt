package test.app.calllog.data

import android.os.Build
import androidx.annotation.RequiresApi
import test.app.calllog.data.local.ContactDBHelper
import test.app.calllog.data.rest.CallLogApi
import test.app.calllog.data.rest.dto.CallLogDto
import test.app.calllog.screens.log.adapter.CallLogUiModel
import java.time.Instant
import java.util.*

class CallLogRepository(
    private val callLogApi: CallLogApi,
    private val callDBHelper: ContactDBHelper
) {

    suspend fun getCallLogs(): List<CallLogUiModel> {

        return callLogApi.getCallLogList().map {
            CallLogUiModel(it.name ?: "Unknown", it.number, "${it.duration} seconds")
        }.toList()
    }

    suspend fun setCallStart(phoneNumber: String) {

        if (phoneNumber.isEmpty()) return

        val log = CallLogDto(
            beginning = Date().time,
            number = phoneNumber,
            name = callDBHelper.resolveContactNameFromNumber(phoneNumber),
            timesQueried = null,
            end = 0,
            duration = null
        )

        val response = callLogApi.callStart(log)
        if (response.responseCode == 226) {
            throw Exception("Another call is ongoing!")
        }
    }

    suspend fun setCallEnd(phoneNumber: String) {

        if (phoneNumber.isEmpty()) return

        val log = CallLogDto(
            beginning = Date().time,
            number = phoneNumber,
            name = callDBHelper.resolveContactNameFromNumber(phoneNumber),
            timesQueried = null,
            end = Date().time,
            duration = null
        )

        val response = callLogApi.callEnd(log)
        if (response.responseCode == 409) {
            throw Exception("No call is in ongoing state!")
        }
    }
}