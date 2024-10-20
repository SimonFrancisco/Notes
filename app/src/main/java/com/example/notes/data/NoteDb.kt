package com.example.notes.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteDb (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val topic: String,
    val content: String,
    val isDraft: Boolean,
    val isFavourite: Boolean
) {

}