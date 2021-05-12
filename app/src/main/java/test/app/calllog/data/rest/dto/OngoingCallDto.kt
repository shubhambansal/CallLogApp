package test.app.calllog.data.rest.dto

data class OngoingCallDto(
    val ongoing: Boolean,
    val number: String,
    val name: String?,
    val beginning: String
)