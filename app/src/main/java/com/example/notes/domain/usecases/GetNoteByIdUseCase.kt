package com.example.notes.domain.usecases

import com.example.notes.domain.entity.Note
import com.example.notes.domain.repository.NoteRepository

class GetNoteByIdUseCase(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(noteId: Int): Note {
        return noteRepository.getNoteById(noteId)
    }
}