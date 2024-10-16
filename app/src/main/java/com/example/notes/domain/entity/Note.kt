package com.example.notes.domain.entity

data class Note(
    val id: Int = DEFAULT_ID,
    val topic: String,
    val content: String,
    val isDraft: Boolean = false,
    val isFavourite: Boolean = false
) {
    companion object {
        const val DEFAULT_ID = 0
    }
}

