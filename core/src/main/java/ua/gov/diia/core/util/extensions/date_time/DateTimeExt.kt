package ua.gov.diia.core.util.extensions.date_time

import ua.gov.diia.core.util.DateFormats
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun getUTCDate(date: String): Date? {
    return try {
        val parsedDate = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)
            .atOffset(ZoneOffset.UTC)
            .toInstant()
        Date.from(parsedDate)
    } catch (e: java.lang.Exception) {
        return null
    }
}

fun getUTCDateUA(date: String): Date? {
    return try {
        val dateProduct = LocalDate.parse(
            date,
            DateTimeFormatter.ofPattern("dd.MM.yyyy")
        )
        Date.from(dateProduct.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
    } catch (e: java.lang.Exception) {
        return null
    }
}

fun getCurrentDateUtc(): Date {
    return try {
        val utc: OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC)
        Date.from(utc.toInstant())
    } catch (e: ParseException) {
        Date()
    }
}

fun Date.toLocalDate(): LocalDate? = try {
    this.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
} catch (e: Exception) {
    null
}

fun Date.toLocalDateTime(): LocalDateTime? = try {
    this.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
} catch (e: Exception) {
    null
}

fun LocalDate.toEpochMillis(): Long {
    val localTime = LocalTime.of(0, 0)
    val zonedDateTime = ZonedDateTime.of(this, localTime, ZoneOffset.UTC)
    return zonedDateTime.toEpochSecond() * 1000
}

fun LocalDate.toEpochSecond(): Long {
    val localTime = LocalTime.of(0, 0)
    val zonedDateTime = ZonedDateTime.of(this, localTime, ZoneOffset.UTC)
    return zonedDateTime.toEpochSecond()
}

fun LocalDate.toSimpleDisplayFormat(): String = try {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    this.format(formatter)
} catch (e: Exception) {
    ""
}

fun LocalDateTime.toSimpleDisplayFormatWithTime(): String = try {
    val formatter = DateTimeFormatter.ofPattern("HH:mm | dd.MM.yyyy")
    this.format(formatter)
} catch (e: Exception) {
    ""
}

fun LocalDateTime.toDefaultDisplayFormat(): String = try {
    val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy 'о' HH:mm", Locale("uk"))
    this.format(formatter)
} catch (e: Exception) {
    ""
}

fun LocalDate.toServerSendFormat(): String = try {
    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    this.format(formatter)
} catch (e: Exception) {
    ""
}

fun toServerSendFormat(date: LocalDate, time: LocalTime): String = try {
    val dateTime = ZonedDateTime.of(date, time, ZoneId.systemDefault())
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    dateTime.format(formatter)
} catch (e: Exception) {
    ""
}

fun toServerSendFormat(dateInMillis: Long): String? {
    return try {
        DateFormats.iso8601.format(Date(dateInMillis))
    } catch (e: Exception) {
        null
    }
}

fun LocalDate.toDisplayTimeFormat(): String = try {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    this.format(formatter)
} catch (e: Exception) {
    ""
}

fun toShortDateFormat(date: String): String = try {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val result = LocalDate.parse(date, inputFormatter)
    outputFormatter.format(result)
} catch (e: Exception) {
    ""
}

fun getCurrentDate(): String {
    val sdf = SimpleDateFormat("dd-MM-yyyy")
    return sdf.format(Date())
}

fun getLocalDateTime(): LocalDateTime{
    val c = Calendar.getInstance()

    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    val hour = c.get(Calendar.HOUR_OF_DAY)
    val minute = c.get(Calendar.MINUTE)

    return LocalDateTime.of(year, month+1, day, hour, minute)
}

fun getLocalDate(): LocalDate{
    val c = Calendar.getInstance()

    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    return LocalDate.of(year, month+1, day)
}

