package nl.example.kts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DownloadViewModel : CoroutineViewModel() {

    fun downloadFile(fileUrl: String): LiveData<Int> {
        val result = MutableLiveData<Int>()
        launch(Dispatchers.IO) {
            repeat(100) {
                delay(50)
                result.postValue(it)
            }
        }
        return result
    }
}
