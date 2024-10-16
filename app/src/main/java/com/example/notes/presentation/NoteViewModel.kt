package com.example.notes.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.notes.data.NoteRepositoryImpl
import com.example.notes.domain.entity.Note
import com.example.notes.domain.usecases.AddNoteUseCase
import com.example.notes.domain.usecases.EditNoteUseCase
import com.example.notes.domain.usecases.GetNoteByIdUseCase
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = NoteRepositoryImpl(application)
    private val addNoteUseCase = AddNoteUseCase(repository)
    private val editNoteUseCase = EditNoteUseCase(repository)
    private val getNoteByIdUseCase = GetNoteByIdUseCase(repository)
    private val _note = MutableLiveData<Note>()
    private val _closeScreen = MutableLiveData<Unit>()
    val close:LiveData<Unit>
        get() = _closeScreen
    private val note: LiveData<Note>
        get() = _note

    fun getNote(noteId: Int) {
        viewModelScope.launch {
            val note = getNoteByIdUseCase.invoke(noteId)
            _note.value = note
        }
    }

    fun addNote(topic: String, content: String) {
        viewModelScope.launch {
            val note = Note(
                topic = topic,
                content = content,
                isFavourite = false,
                isDraft = false
            )
            addNoteUseCase.invoke(note)
            finishTask()
        }
    }

    fun editNote(topic: String, content: String) {
        _note.value?.let {
            viewModelScope.launch {
                val note = it.copy(
                    topic = topic,
                    content = content
                )
                editNoteUseCase.invoke(note)
                finishTask()
            }
        }
    }

    private fun finishTask() {
        _closeScreen.value = Unit
    }
}