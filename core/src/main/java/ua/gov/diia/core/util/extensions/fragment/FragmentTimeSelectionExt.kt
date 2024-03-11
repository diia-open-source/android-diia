package ua.gov.diia.core.util.extensions.fragment

import android.app.TimePickerDialog
import androidx.fragment.app.Fragment
import kotlinx.coroutines.suspendCancellableCoroutine
import java.time.LocalTime
import kotlin.coroutines.resume

suspend fun Fragment.awaitForSelectedTime(): LocalTime = suspendCancellableCoroutine { cont ->

    val listener: TimePickerDialog.OnTimeSetListener =
        TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            val time = LocalTime.of(hour, minute)
            if (cont.isActive) cont.resume(time)
        }

    val currentTime = LocalTime.now()
    val timePicker = TimePickerDialog(
        requireContext(),
        listener,
        currentTime.hour,
        currentTime.minute,
        true
    )

    timePicker.show()

    cont.invokeOnCancellation {
        if (timePicker.isShowing) {
            timePicker.dismiss()
        }
    }
}