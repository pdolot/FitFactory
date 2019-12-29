package com.example.fitfactory.utils

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.util.*

object TimeUtil {

    fun isDateBetween(startDate: String?, endDate: String?, dateToCompare: String? = null, dateFormat: String): Boolean{
        if (startDate == null || endDate == null) return false

        val formatter = DateTimeFormat.forPattern(dateFormat) as DateTimeFormatter
        val sD = formatter.parseMutableDateTime(startDate)
        val eD = formatter.parseMutableDateTime(endDate).apply {
            addHours(23)
            addMinutes(59)
            addSeconds(59)
        }

        val date = if (dateToCompare == null) DateTime.now() else formatter.parseDateTime(dateToCompare)
        return (date.isAfter(sD) && date.isBefore(eD))
    }

    fun getDateAsString(date: DateTime, dateFormat: String): String{
        val formatter = DateTimeFormat.forPattern(dateFormat) as DateTimeFormatter
        return  formatter.print(date)
    }

    fun isBeforeNow(date: String?, dateFormat: String): Boolean{
        if (date == null) return false
        val formatter = DateTimeFormat.forPattern(dateFormat) as DateTimeFormatter
        val d = formatter.parseDateTime(date)
        val now = DateTime.now()

        return now.isBefore(d)
    }
}