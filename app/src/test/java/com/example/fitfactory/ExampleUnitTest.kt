package com.example.fitfactory

import android.content.Context
import com.example.fitfactory.functional.localStorage.SharedPrefLocalStorage
import com.example.fitfactory.utils.TimeUtil
import com.example.fitfactory.utils.Validator
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun isDateBetween(){
        assertEquals(true, TimeUtil.isDateBetween("22/11/2019", "21/11/2020", dateFormat = "dd/MM/yyyy"))
    }


}
