package com.example.notes.presentation.noteFragment

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
    val closeScreen: LiveData<Unit>
        get() = _closeScreen
    val note: LiveData<Note>
        get() = _note

    fun getNote(noteId: Int) {
        viewModelScope.launch {
            val note = getNoteByIdUseCase(noteId)
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
            addNoteUseCase(note)
            finishTask()
        }
    }

    fun editNote(topic: String, content: String, isDraft: Boolean = false) {
        _note.value?.let {
            viewModelScope.launch {
                val note = it.copy(
                    topic = topic,
                    content = content,
                    isDraft = isDraft
                )
                editNoteUseCase(note)
                finishTask()
            }
        }
    }

    fun draftNote(topic: String, content: String) {
        _note.value?.let {
            viewModelScope.launch {
                val note = it.copy(
                    topic = topic,
                    content = content,
                    isDraft = true
                )
                editNoteUseCase(note)
                finishTask()
            }
            return
        }
        viewModelScope.launch {
            val note = Note(
                topic = topic,
                content = content,
                isFavourite = false,
                isDraft = true
            )
            addNoteUseCase(note)
            finishTask()
        }

    }

    private fun finishTask() {
        _closeScreen.value = Unit
    }
}