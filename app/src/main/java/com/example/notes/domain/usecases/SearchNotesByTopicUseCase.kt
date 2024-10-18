package com.example.notes.domain.usecases

import androidx.lifecycle.LiveData
import com.example.notes.domain.entity.Note
import com.example.notes.domain.repository.NoteRepository

class SearchNotesByTopicUseCase(private val repository: NoteRepository) {
    operator fun invoke(noteTopic: String): LiveData<List<Note>> {
        return repository.searchNotesByTopic(noteTopic)
    }
}