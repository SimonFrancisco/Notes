package com.example.notes.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.notes.domain.entity.Note
import com.example.notes.domain.repository.NoteRepository

class NoteRepositoryImpl(application: Application) : NoteRepository {
    private val noteDao = NoteDatabase.getInstance(application).noteDao()
    private val noteMapper = NoteMapper()
    override fun getNotes(): LiveData<List<Note>> {
        return noteDao.getNotes().map {
            noteMapper.mapListDbToListEntity(it)
        }
    }

    override fun getFavouriteNotes(): LiveData<List<Note>> {
        return noteDao.getFavouriteNotes().map {
            noteMapper.mapListDbToListEntity(it)
        }
    }
    override fun searchNotesByTopic(noteTopic: String): LiveData<List<Note>> {
        return noteDao.searchNotesByTopic(noteTopic).map {
            noteMapper.mapListDbToListEntity(it)
        }
    }
    override suspend fun getNoteById(id: Int): Note {
        val noteDb = noteDao.getNoteById(id)
        return noteMapper.mapDbToEntity(noteDb)
    }

    override suspend fun addNote(note: Note) {
        noteDao.addNote(noteMapper.mapEntityToDb(note))
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note.id)
    }

    override suspend fun editNote(note: Note) {
        noteDao.addNote(noteMapper.mapEntityToDb(note))
    }
}