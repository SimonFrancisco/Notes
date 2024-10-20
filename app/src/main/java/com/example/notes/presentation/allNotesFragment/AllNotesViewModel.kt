package com.example.notes.presentation.allNotesFragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notes.data.NoteRepositoryImpl
import com.example.notes.domain.entity.Note
import com.example.notes.domain.usecases.EditNoteUseCase
import com.example.notes.domain.usecases.GetNotesUseCase
import com.example.notes.domain.usecases.SearchNotesByTopicUseCase
import kotlinx.coroutines.launch

class AllNotesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = NoteRepositoryImpl(application)
    private val getNotesUseCase = GetNotesUseCase(repository)
    private val editNoteUseCase = EditNoteUseCase(repository)
    private val searchNotesByTopicUseCase = SearchNotesByTopicUseCase(repository)
    private val _notes = getNotesUseCase()
    val notes: LiveData<List<Note>>
        get() = _notes


    fun searchNotesByTopic(noteTopic: String) =
        searchNotesByTopicUseCase(noteTopic)

    fun changeIsFavouriteState(note: Note) {
        viewModelScope.launch {
            val newNote = note.copy(isFavourite = !note.isFavourite)
            editNoteUseCase(newNote)
        }
    }

}