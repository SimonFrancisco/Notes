package com.example.notes.presentation.allNotesFragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notes.data.NoteRepositoryImpl
import com.example.notes.domain.entity.Note
import com.example.notes.domain.usecases.DeleteNoteUseCase
import com.example.notes.domain.usecases.GetNotesUseCase
import kotlinx.coroutines.launch

class AllNotesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = NoteRepositoryImpl(application)
    private val getNotesUseCase = GetNotesUseCase(repository)
    private val deleteNoteUseCase = DeleteNoteUseCase(repository)
    private val _notes = getNotesUseCase.invoke()
    val notes: LiveData<List<Note>>
        get() = _notes

    fun deleteNote(note: Note){
        viewModelScope.launch {
            deleteNoteUseCase.invoke(note)
        }
    }
}