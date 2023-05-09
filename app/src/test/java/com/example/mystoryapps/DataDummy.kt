package com.example.mystoryapps

import com.example.mystoryapps.response.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "createdAt + $i",
                "name $i",
                "description $i",
                (i * 1.0),
                "id $i",
                (i * 1.0),
            )
            items.add(story)
        }
        return items
    }
}
