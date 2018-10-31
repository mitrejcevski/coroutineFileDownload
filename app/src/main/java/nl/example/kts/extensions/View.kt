package nl.example.kts.extensions

import android.os.Build
import android.widget.ProgressBar

fun ProgressBar.updateProgress(value: Int, animate: Boolean = true) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        setProgress(value, animate)
    } else {
        progress = value
    }
}