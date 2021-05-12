package test.app.calllog.data.local

interface KeyValueStorage {

    fun getServerIp(): String?
    fun getServerPort(): String?

    companion object {
        const val PREF_FILE_NAME = "app_preference"
        const val KEY_SERVER_IP = "key_server_ip"
        const val KEY_SERVER_PORT = "key_server_port"
    }
}