package com.example.notes.presentation.favouriteNotesFragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notes.data.NoteRepositoryImpl
import com.example.notes.domain.entity.Note
import com.example.notes.domain.usecases.EditNoteUseCase
import com.example.notes.domain.usecases.GetFavouriteNotesUseCase
import kotlinx.coroutines.launch

class FavouriteNotesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = NoteRepositoryImpl(application)
    private val getFavouriteNotesUseCase = GetFavouriteNotesUseCase(repository)
    private val editNoteUseCase = EditNoteUseCase(repository)
    private val _favouriteNotes = getFavouriteNotesUseCase()
    val favouriteNotes: LiveData<List<Note>>
        get() = _favouriteNotes

    fun changeIsFavouriteState(note: Note) {
        viewModelScope.launch {
            val newNote = note.copy(isFavourite = !note.isFavourite)
            editNoteUseCase(newNote)
        }
    }

}