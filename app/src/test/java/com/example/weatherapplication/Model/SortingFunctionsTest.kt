package com.example.weatherapplication.Model

import org.junit.Test
import com.google.common.truth.Truth.assertThat


class SortingFunctionsTest {

    val post1 = PostFirestore("1","dafd", "2", "2016-10-12, 12:24")
    val post2 = PostFirestore("2", "asjdf", "6", "2016-10-12, 18:33")
    val post3 = PostFirestore("3","gad", "2", "2019-01-01, 23:03")
    val post4 = PostFirestore("4", "hsadfhg", "4", "2020-04-22, 18:55")
    val post5 = PostFirestore("5", "jildfg", "12", "2020-07-12, 10:02")
    val list = mutableListOf(post4, post5, post3, post1, post2)
    val correctList = mutableListOf(post1, post2, post3, post4, post5)

    @Test
    fun `returning correct list thrue insertion sorting`() {
        //val list = mutableListOf("2020-04-22, 18:55", "2020-07-12, 10:02", "2019-01-01, 23:03", "2016-10-12, 18:33", "2016-10-12, 12:24")
        //val correctList = mutableListOf("2016-10-12, 12:24", "2016-10-12, 18:33", "2019-01-01, 23:03", "2020-04-22, 18:55", "2020-07-12, 10:02")
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