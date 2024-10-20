package com.example.notes.domain.usecases

import androidx.lifecycle.LiveData
import com.example.notes.domain.entity.Note
import com.example.notes.domain.repository.NoteRepository

class GetNotesUseCase(private val noteRepository: NoteRepository) {
    operator fun invoke():LiveData<List<Note>>{
        return noteRepository.getNotes()
    }
}