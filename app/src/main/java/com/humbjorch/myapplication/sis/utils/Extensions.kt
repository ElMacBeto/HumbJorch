package com.humbjorch.myapplication.sis.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.humbjorch.myapplication.sis.utils.timer.CheckConnection
import com.humbjorch.myapplication.sis.utils.util.Constants.PHOTO_AUTHENTICATION_DEFAULT
import de.hdodenhof.circleimageview.CircleImageView
import java.text.DateFormat
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone
import java.util.Timer

fun CircleImageView.loadImageUrl(resource: String?) {
    if (resource != null) {
        val options: RequestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()

        Glide.with(this.context)
            .load(
                resource.ifEmpty { PHOTO_AUTHENTICATION_DEFAULT }
            )
            .apply(options)
            .into(this)
    }
}


fun Activity.launchTimer() {
    val timer = Timer()
    timer.schedule(CheckConnection(this), 0, 4000)
}

@SuppressLint("SimpleDateFormat")
fun String.toDateFormatMonths(format: String = "dd/MM/yyyy"): String {
    val symbols = DateFormatSymbols(Locale.forLanguageTag("es-MX"))

    val dateFormat = SimpleDateFormat(format, symbols)
    dateFormat.timeZone = TimeZone.getTimeZone("America/Mexico_City")
    val date = dateFormat.parse(this)

    val formatter: DateFormat = SimpleDateFormat("dd MMM", symbols)
    formatter.timeZone = TimeZone.getTimeZone("America/Mexico_City")
    val months = arrayOf(
        "ene", "feb", "mar", "abr", "may", "jun",
        "jul", "ago", "sep", "oct", "nov", "dic"
    )
    symbols.shortMonths = months
    return formatter.format(date!!).replace(".", "")
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(inputDate: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
    val instant = Instant.from(inputFormatter.parse(inputDate))

    val localDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC)

    val outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")

    return outputFormatter.format(localDateTime)
}