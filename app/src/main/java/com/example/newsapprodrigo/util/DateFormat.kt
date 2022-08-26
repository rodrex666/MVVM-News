package com.example.newsapprodrigo.util

import java.text.SimpleDateFormat
import java.util.*
import java.time.format.DateTimeFormatter


object DateFormat {
    fun changeDateFormat(oldDate: String?): String?{
        var newDate = ""
        newDate = try {
            val oldDateFormat = SimpleDateFormat(Constans.ARTICLE_DATE_FORMAT, Locale.GERMANY).parse(oldDate)
            SimpleDateFormat(Constans.NEW_DATE_FORMAT, Locale.GERMANY).format(oldDateFormat)
        } catch (ParseException: Exception){
            ""
        }
        return newDate
    }
}