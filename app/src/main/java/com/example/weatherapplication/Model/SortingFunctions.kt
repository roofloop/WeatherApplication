package com.example.weatherapplication.Model

object SortingFunctions {

    fun dateInsertionSorting(list: MutableList<PostFirestore>): MutableList<PostFirestore> {

        if (list.isEmpty() || list.size < 2) {
            return list
        }
        println(list.count() - 1)
        for (count in 1 until list.count()) {

            println(count)
            val listItem = list[count]
            var i = count
            while (i > 0 && listItem.creationDate.toString() > list[i - 1].creationDate.toString()) {
                list[i] = list[i - 1]
                i -= 1
            }
            list[i] = listItem
        }
        println(list)
        return list
    }

    fun dateBubbleSorting(dateList: MutableList<String>): List<String> {

        var sort = true
        while (sort) {
            sort = false
            for (i in 0 until dateList.size - 1) {
                if (dateList[i] > dateList[i + 1]) {
                    val temp = dateList[i]
                    dateList[i] = dateList[i + 1]
                    dateList[i + 1] = temp
                    sort = true
                }
            }
        }
        return dateList
    }
}