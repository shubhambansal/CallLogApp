package test.app.calllog.data.rest.dto

data class CallLogDto(
    val beginning: Long,
    val end: Long?,
    val duration: Long?,
    val number: String,
    val name: String?,
    val timesQueried: Int?
)