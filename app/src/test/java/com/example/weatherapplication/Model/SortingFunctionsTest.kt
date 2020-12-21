package com.example.weatherapplication.Model

import org.junit.Test
import com.google.common.truth.Truth.assertThat


class SortingFunctionsTest {

    @Test
    fun `returning correct list thrue insertion sorting`() {
        val list = mutableListOf("2020-04-22, 18:55", "2020-07-12, 10:02", "2019-01-01, 23:03", "2016-10-12, 18:33", "2016-10-12, 12:24")
        val correctList = mutableListOf("2016-10-12, 12:24", "2016-10-12, 18:33", "2019-01-01, 23:03", "2020-04-22, 18:55", "2020-07-12, 10:02")
        val result = SortingFunctions.dateInsertionSorting(list)
        assertThat(result).isEqualTo(correctList)
    }

    @Test
    fun `return correct list thrue bubble sort`() {
        val list = mutableListOf("5", "1", "3", "2", "4")
        val correctList = mutableListOf("1", "2", "3", "4", "5")
        val result = SortingFunctions.dateBubbleSorting(list)
        assertThat(result).isEqualTo(correctList)
    }

}