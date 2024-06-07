package ua.gov.diia.ui_base.util.extensions.fragment

import android.app.DatePickerDialog
import androidx.fragment.app.Fragment
import kotlinx.coroutines.suspendCancellableCoroutine
import ua.gov.diia.core.util.extensions.date_time.toEpochMillis

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.coroutines.resume

data class DateSelector(
    val minDate: LocalDate? = null,
    val maxDate: LocalDate? = null
)

suspend fun Fragment.awaitForSelectedDate(
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null
): LocalDate = suspendCancellableCoroutine { cont ->

    val calendar = Calendar.getInstance()

    val pickupListener: DatePickerDialog.OnDateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar[Calendar.YEAR] = year
            calendar[Calendar.MONTH] = month
            calendar[Calendar.DAY_OF_MONTH] = dayOfMonth

            val javaTimeInstance = LocalDateTime.ofInstant(
                calendar.toInstant(),
                ZoneId.systemDefault()
            )
            val result = LocalDate.of(
                javaTimeInstance.year,
                javaTimeInstance.month,
                javaTimeInstance.dayOfMonth
            )

            if (cont.isActive) cont.resume(result)
        }

    val picker = DatePickerDialog(
        requireContext(),
        pickupListener,
        calendar[Calendar.YEAR],
        calendar[Calendar.MONTH],
        calendar[Calendar.DAY_OF_MONTH]
    )

    picker.datePicker.apply {
        if (minDate != null) setMinDate(minDate.toEpochMillis())
        if (maxDate != null) setMaxDate(maxDate.toEpochMillis())
    }

    picker.show()

    cont.invokeOnCancellation {
        if (picker.isShowing) {
            picker.dismiss()
        }
    }
}