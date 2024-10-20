package com.example.notes.domain.usecases

import com.example.notes.domain.entity.Note
import com.example.notes.domain.repository.NoteRepository

class DeleteNoteUseCase(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(note: Note){
        noteRepository.deleteNote(note)
    }
}