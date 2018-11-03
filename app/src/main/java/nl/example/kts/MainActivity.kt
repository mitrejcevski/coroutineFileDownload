package nl.example.kts

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat.checkSelfPermission
import kotlinx.android.synthetic.main.activity_demo.*
import nl.example.kts.data.FileDownloadEvent
import nl.example.kts.extensions.observe
import nl.example.kts.extensions.updateProgress

class MainActivity : AppCompatActivity() {

    private val viewModel = DownloadViewModel()

    private companion object {
        const val STORAGE_PERMISSION_REQUEST_CODE = 1234
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
        setupUI()
    }

    private fun setupUI() {
        buttonStartDownload.setOnClickListener {
            performStoragePermissionsCheck()
        }
    }

    private fun performStoragePermissionsCheck() {
        if (checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_REQUEST_CODE)
        } else {
            onStorageUsePermissionGranted()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            STORAGE_PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED)) {
                    onStorageUsePermissionGranted()
                } else {
                    toast(R.string.cannot_use_storage)
                }
            }
        }
    }

    private fun onStorageUsePermissionGranted() {
        buttonStartDownload.isEnabled = false
        val url = editTextFileUrl.text.toString()
        triggerFileDownload(url)
    }

    private fun triggerFileDownload(url: String) {
        viewModel.downloadFile(url).observe(this) {
            when (it) {
                is FileDownloadEvent.Progress -> {
                    buttonStartDownload.text = "Downloading... ${it.percentage}%"
                    progressBarStatus.updateProgress(it.percentage)
                }

                is FileDownloadEvent.Success -> {
                    toast(R.string.success)
                    buttonStartDownload.setText(R.string.start_download)
                    buttonStartDownload.isEnabled = true
                }

                is FileDownloadEvent.Failure -> {
                    toast(it.failure)
                    buttonStartDownload.setText(R.string.start_download)
                    buttonStartDownload.isEnabled = true
                }
            }
        }
    }

    private fun Context.toast(@StringRes message: Int, duration: Int = Toast.LENGTH_SHORT) =
            Toast.makeText(this, message, duration).show()
}
