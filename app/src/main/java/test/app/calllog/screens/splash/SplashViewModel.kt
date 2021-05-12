package test.app.calllog.screens.splash

import androidx.lifecycle.ViewModel
import test.app.calllog.data.local.KeyValueStorage

class SplashViewModel(
    private val keyValueStorage: KeyValueStorage
) : ViewModel() {


    fun isServerConfigPresent() =
        keyValueStorage.getServerIp() != null && keyValueStorage.getServerPort() != null


}