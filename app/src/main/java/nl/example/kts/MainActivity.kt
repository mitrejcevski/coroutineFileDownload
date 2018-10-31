package nl.example.kts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_demo.*
import nl.example.kts.extensions.observe
import nl.example.kts.extensions.updateProgress

class MainActivity : AppCompatActivity() {

    private val viewModel = DownloadViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        val downloadUrl = urlFromExtras()
        viewModel.downloadFile(downloadUrl).observe(this) {
            progressBarStatus.updateProgress(it)
        }
    }

    private fun urlFromExtras(): String =
            intent.getStringExtra("") ?: ""
}
