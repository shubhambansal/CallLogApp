package test.app.calllog.data.local

import android.content.SharedPreferences

class KeyValueStorageImpl(private val sharedPreferences: SharedPreferences) : KeyValueStorage {

    override fun getServerIp(): String? =
        sharedPreferences.getString(KeyValueStorage.KEY_SERVER_IP, null)


    override fun getServerPort(): String? =
        sharedPreferences.getString(KeyValueStorage.KEY_SERVER_PORT, null)

}