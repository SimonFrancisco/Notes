package com.example.notes.domain.entity

data class Note(
    val id: Int = DEFAULT_ID,
    val topic: String,
    val content: String,
    val isDraft: Boolean,
    val isFavourite: Boolean
) {
    companion object {
        const val DEFAULT_ID = 0
    }
}

