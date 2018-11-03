package nl.example.kts.data

import androidx.annotation.StringRes

sealed class FileDownloadEvent {

    data class Progress(val percentage: Int) : FileDownloadEvent()

    data class Success(val downloadedFilePath: String) : FileDownloadEvent()

    data class Failure(@StringRes val failure: Int) : FileDownloadEvent()
}