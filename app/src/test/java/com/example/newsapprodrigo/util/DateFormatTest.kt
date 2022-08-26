package com.example.newsapprodrigo.util

import org.junit.Assert.*

import org.junit.Test

class DateFormatTest {
//2022-03-17T14:00:00+0200
    val date = "2022-03-18T07:39:00Z" //2022-03-18T07:39:00Z
    val dateFormat = DateFormat
    @Test
    fun changeDateFormat() {
        val result = dateFormat.changeDateFormat(date)
        assertEquals("18.03.2022 07:39",result)
    }
}