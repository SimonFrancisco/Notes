package com.example.notes.presentation.recycleView

import androidx.recyclerview.widget.DiffUtil
import com.example.notes.domain.entity.Note

class NoteDiffCallBack:DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }
}