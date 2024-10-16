package com.example.notes.presentation.recycleView

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.notes.R

class NoteViewHolder(val view: View):RecyclerView.ViewHolder(view) {
    val tvTopic = view.findViewById<TextView>(R.id.tv_topic)
}