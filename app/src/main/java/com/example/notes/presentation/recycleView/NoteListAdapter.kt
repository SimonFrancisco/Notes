package com.example.notes.presentation.recycleView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import com.example.notes.R
import com.example.notes.domain.entity.Note

class NoteListAdapter : ListAdapter<Note, NoteViewHolder>(NoteDiffCallBack()) {

    var onNoteClickListener: ((Note) -> Unit)? = null
    var onNoteLongClickListener: ((Note) -> Unit)? = null
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

        with(holder) {
            view.setOnClickListener {
                onNoteClickListener?.invoke(note)
            }
            view.setOnLongClickListener {
                onNoteLongClickListener?.invoke(note)
                true
            }
            tvTopic.text = note.topic
            tvContent.text = note.content
            when (note.isDraft) {
                true -> tvStatus.text = view.context.getString(R.string.note_draft)
                false -> tvStatus.text = view.context.getString(R.string.note_saved)
            }

            val startOn = ContextCompat.getDrawable(view.context, R.drawable.ic_star_full)
            val startOff = ContextCompat.getDrawable(view.context, R.drawable.ic_star_empty)
            when (note.isFavourite) {
                true -> ivStar.setImageDrawable(startOn)
                false -> ivStar.setImageDrawable(startOff)
            }
        }
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