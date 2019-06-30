package com.jiaoerzhang.platform.entity.album


data class PhotoEntity(
    val `data`: Data,
    val message: String,
    val status: Int,
    val timestamp: Long
) {
    data class Data(
        val content: List<Content>,
        val empty: Boolean,
        val first: Boolean,
        val last: Boolean,
        val number: Int,
        val numberOfElements: Int,
        val pageable: Pageable,
        val size: Int,
        val sort: Sort,
        val totalElements: Int,
        val totalPages: Int
    ) {
        data class Sort(
            val empty: Boolean,
            val sorted: Boolean,
            val unsorted: Boolean
        )

        data class Pageable(
            val offset: Int,
            val pageNumber: Int,
            val pageSize: Int,
            val paged: Boolean,
            val sort: Sort,
            val unpaged: Boolean
        ) {
            data class Sort(
                val empty: Boolean,
                val sorted: Boolean,
                val unsorted: Boolean
            )
        }

        data class Content(
            val albumId: Int,
            val id: Int,
            val isUse: Int,
            val url: String
        )
    }
}