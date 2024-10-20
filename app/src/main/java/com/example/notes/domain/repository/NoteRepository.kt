package com.example.notes.domain.repository

import androidx.lifecycle.LiveData
import com.example.notes.domain.entity.Note

interface NoteRepository {
    fun getNotes(): LiveData<List<Note>>
    fun getFavouriteNotes(): LiveData<List<Note>>
    fun searchNotesByTopic(noteTopic:String): LiveData<List<Note>>
    suspend fun getNoteById(id: Int): Note
    suspend fun addNote(note: Note)
    suspend fun deleteNote(note: Note)
    suspend fun editNote(note: Note)
}