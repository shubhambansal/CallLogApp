package test.app.calllog.data.rest

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import test.app.calllog.data.rest.dto.ApiResponse
import test.app.calllog.data.rest.dto.CallLogDto

interface CallLogApi {

    @GET("/log")
    suspend fun getCallLogList(): List<CallLogDto>

    @POST("/callStart")
    suspend fun callStart(@Body callLogDto: CallLogDto): ApiResponse

    @PUT("/callEnd")
    suspend fun callEnd(@Body callLogDto: CallLogDto): ApiResponse

}