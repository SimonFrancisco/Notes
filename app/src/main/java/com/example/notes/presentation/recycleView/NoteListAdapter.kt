package com.example.notes.presentation.recycleView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.notes.R
import com.example.notes.domain.entity.Note

class NoteListAdapter : ListAdapter<Note, NoteViewHolder>(NoteDiffCallBack()) {

    var onNoteClickListener: ((Note) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val layout = when (viewType) {
            NOTE_DRAFT -> {
                R.layout.note_draft
            }

            NOTE_ADD -> {
                R.layout.note_add
            }

            else -> throw RuntimeException("Unknown view type: $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)

        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        holder.view.setOnClickListener {
            onNoteClickListener?.invoke(note)
        }
        holder.tvTopic.text = note.topic
    }

    override fun getItemViewType(position: Int): Int {
        val note = getItem(position)
        if (note.isDraft) {
            return NOTE_DRAFT
        }
        return NOTE_ADD
    }

    companion object {
        const val NOTE_ADD = 1
        const val NOTE_DRAFT = 0
    }
}