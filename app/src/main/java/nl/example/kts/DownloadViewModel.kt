package nl.example.kts

import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import nl.example.kts.data.FileDownloadEvent
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class DownloadViewModel : CoroutineViewModel() {

    private val downloadsDirectory = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS)

    fun downloadFile(fileUrl: String): LiveData<FileDownloadEvent> {
        val result = MutableLiveData<FileDownloadEvent>()
        if (fileUrl.isBlank()) {
            result.value = FileDownloadEvent.Failure(R.string.bad_download_url_provided)
        } else {
            proceedFileDownload(fileUrl, result)
        }
        return result
    }

    private fun proceedFileDownload(fileUrl: String, result: MutableLiveData<FileDownloadEvent>) {
        launch(Dispatchers.IO) {
            try {
                performFileDownload(fileUrl, result)
            } catch (exception: IOException) {
                result.postValue(FileDownloadEvent.Failure(R.string.error_downloading_file))
            }
        }
    }

    private fun performFileDownload(fileUrl: String, result: MutableLiveData<FileDownloadEvent>) {
        val downloadTarget = targetFile(fileName(fileUrl))
        val connection = URL(fileUrl).openConnection() as HttpURLConnection
        val contentLength = connection.contentLength
        val inputStream = BufferedInputStream(connection.inputStream)
        val outputStream = FileOutputStream(downloadTarget.path)
        val buffer = ByteArray(4096)
        var downloadedFileSize = 0L
        var currentRead = 0
        while (currentRead != -1 && isActive) {
            downloadedFileSize += currentRead
            outputStream.write(buffer, 0, currentRead)
            currentRead = inputStream.read(buffer, 0, buffer.size)
            val progress = (100f * (downloadedFileSize.toFloat() / contentLength.toFloat())).toInt()
            result.postValue(FileDownloadEvent.Progress(progress))
        }
        result.postValue(FileDownloadEvent.Success(downloadTarget.path))
        outputStream.close()
        inputStream.close()
    }

    private fun fileName(fileUrl: String): String =
            fileUrl.substring(fileUrl.lastIndexOf("/") + 1, fileUrl.length)

    private fun targetFile(fileName: String): File =
            File(downloadsDirectory, fileName)
}
