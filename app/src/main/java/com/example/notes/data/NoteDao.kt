package com.example.notes.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun getNotes(): LiveData<List<NoteDb>>

    @Query("SELECT * FROM notes WHERE isFavourite=:isFavourite")
    fun getFavouriteNotes(isFavourite: Boolean = true): LiveData<List<NoteDb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(noteDb: NoteDb)

    @Query("DELETE FROM notes WHERE id=:noteId")
    suspend fun deleteNote(noteId: Int)

    @Query("SELECT * FROM notes WHERE id=:noteId LIMIT 1")
    suspend fun getNoteById(noteId: Int): NoteDb

    @Query("SELECT * FROM notes WHERE topic LIKE:noteTopic")
    fun searchNotesByTopic(noteTopic: String): LiveData<List<NoteDb>>
}