package com.example.notes.data

import com.example.notes.domain.entity.Note

class NoteMapper {
    fun mapEntityToDb(note: Note) = NoteDb(
        id = note.id,
        topic = note.topic,
        content = note.content,
        isDraft = note.isDraft,
        isFavourite = note.isFavourite
    )

    fun mapDbToEntity(noteDb: NoteDb) = Note(
        id = noteDb.id,
        topic = noteDb.topic,
        content = noteDb.content,
        isDraft = noteDb.isDraft,
        isFavourite = noteDb.isFavourite
    )

    fun mapListDbToListEntity(list: List<NoteDb>) =
        list.map { mapDbToEntity(it) }
}