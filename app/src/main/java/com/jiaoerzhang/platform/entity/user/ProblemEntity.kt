package com.jiaoerzhang.platform.entity.user

data class ProblemEntity(
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
        val content: String,
        val eventTime: Int,
        val flag: Int,
        val id: Int,
        val updateTime: Int
    )
}