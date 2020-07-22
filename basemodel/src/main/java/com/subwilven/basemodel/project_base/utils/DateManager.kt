package com.subwilven.basemodel.project_base.utils

import android.text.format.DateUtils
import com.subwilven.basemodel.project_base.BaseApplication
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

public object DateManager {

    fun getTimeDiff(endTime: Long,now: Long): String {
        return DateUtils.getRelativeTimeSpanString(endTime,now,
            DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE) as String
    }


    fun convertStringToDateObj(dateString: String, format: String): Date? {
        try {
            val sd1 = SimpleDateFormat(format, Locale.getDefault())
            return sd1.parse(dateString)

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return null
    }

    @Throws(ParseException::class)
    fun isTimeInBetween(openTime: Long, closeTime: Long): Boolean {
        val initialTime =
            SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(openTime * 1000))
        val finalTime =
            SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(closeTime * 1000))
        var valid = false
        val reg = "^([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$"
        if (initialTime.matches(reg.toRegex()) && finalTime.matches(reg.toRegex())) {

            val inTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).parse(initialTime)
            val calendar1 = Calendar.getInstance()
            calendar1.time = inTime

            val calendar3 = Calendar.getInstance()
            calendar3.time = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                .parse(SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date()))?:Date()

            //End Time
            val finTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).parse(finalTime)?:Date()
            val calendar2 = Calendar.getInstance()
            calendar2.time = finTime

            if (finalTime.compareTo(initialTime) < 0) {
                calendar2.add(Calendar.DATE, 1)
            }

            val actualTime = calendar3.time
            if ((actualTime.after(calendar1.time) || actualTime.compareTo(calendar1.time) == 0) && actualTime.before(
                    calendar2.time
                )
            ) {
                valid = true

            }
        }
        return valid
    }
}

fun Long.toStringDate(format: String = "dd-MM-yyyy"): String {
    val local = Locale(LocalManager.getLanguage(BaseApplication.instance?.applicationContext!!)?:"en")
    val dateFormat =
        SimpleDateFormat(format,local)
   return dateFormat.format(this)
}

fun Date.toStringDate(format: String = "dd-MM-yyyy"): String {
    return this.time.toStringDate(format)
}

fun String.toDate(format: String = "dd-MM-yyyy"): Date {
    return SimpleDateFormat(
        format,
        Locale(LocalManager.getLanguage(BaseApplication.instance?.applicationContext!!)?:"en")
    )
        .parse(this) ?: Date()
}

fun Long.toStringDateDiff(): String {
    return DateUtils.getRelativeTimeSpanString(this) as String
}