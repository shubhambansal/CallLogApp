package test.app.calllog.screens.log

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import test.app.calllog.R
import test.app.calllog.screens.ResultData
import test.app.calllog.screens.log.adapter.CallLogListAdapter
import test.app.calllog.screens.log.adapter.CallLogUiModel
import test.app.calllog.screens.settings.SettingActivity

class CallLogListActivity : AppCompatActivity() {

    private val viewModel by viewModel<CallLogViewModel>()

    private lateinit var adapter: CallLogListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        initView()

        viewModel.callLogList.observe(this, {

            when (it) {

                is ResultData.Loading -> enableProgress(it.isLoading)
                is ResultData.Success -> adapter.setList(it.data as List<CallLogUiModel>)
                is ResultData.Error -> showError(it.message ?: "Unknown Error")
            }
        })

        viewModel.loadCallLog(true)

        checkPermission()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_settings -> {
                startSettingActivity()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun enableProgress(isLoading: Boolean) {
        swipeRefreshView.isRefreshing = isLoading
    }

    private fun showError(error: String) {
        Snackbar.make(containerView, error, Snackbar.LENGTH_LONG).show()
    }

    private fun initView() {

        callLogRecyclerView.layoutManager = LinearLayoutManager(this)
        callLogRecyclerView.hasFixedSize()

        adapter = CallLogListAdapter()
        callLogRecyclerView.adapter = adapter


        swipeRefreshView.setOnRefreshListener {
            viewModel.loadCallLog(true)
        }
    }


    private fun startSettingActivity() {
        startActivity(Intent(this, SettingActivity::class.java))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when (requestCode) {
            PERMISSION_REQUEST -> if (isPermissionGranted(grantResults)) actionOnService(
                ServiceAction.START
            )
        }
    }

    private fun isPermissionGranted(grantResults: IntArray): Boolean {
        return if (grantResults.isEmpty()) false else grantResults.first() == PackageManager.PERMISSION_GRANTED
    }

    private fun checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val isReadCallLogGranted =
                checkSelfPermission(Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED

            val isPhoneStateGranted =
                checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED

//            val isPhoneStateGranted =
//                true


            val isReadContactGranted =
                checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED

            if (!isReadCallLogGranted || !isPhoneStateGranted || !isReadContactGranted) {
                requestPermissions(PERMISSIONS, PERMISSION_REQUEST)
                return
            }

            actionOnService(ServiceAction.START)
        }
    }

    private fun actionOnService(serviceAction: ServiceAction) {

        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        Intent(this, CallLogService::class.java).also {
            it.action = serviceAction.name

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(it)
                return
            }

            startService(it)
        }
    }


    companion object {

        private const val PERMISSION_REQUEST = 1

        private val PERMISSIONS = arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_CONTACTS,
        )
    }

}