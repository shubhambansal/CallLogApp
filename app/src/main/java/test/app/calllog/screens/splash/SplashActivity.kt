package test.app.calllog.screens.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.koin.android.viewmodel.ext.android.viewModel
import test.app.calllog.R
import test.app.calllog.screens.log.CallLogListActivity
import test.app.calllog.screens.settings.SettingActivity

class SplashActivity : AppCompatActivity() {

    private val viewModel by viewModel<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        if (viewModel.isServerConfigPresent()) {
            startActivity(Intent(this, CallLogListActivity::class.java))
        } else {
            startActivity(Intent(this, SettingActivity::class.java))
        }

        finish()
    }
}