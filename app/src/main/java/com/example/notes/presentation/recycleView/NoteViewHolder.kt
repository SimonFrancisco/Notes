package com.example.notes.presentation.recycleView

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.R

class NoteViewHolder(val view: View):RecyclerView.ViewHolder(view) {
    val tvTopic: TextView = view.findViewById(R.id.tv_topic)
    val tvContent:TextView = view.findViewById(R.id.tv_content)
    val tvStatus:TextView = view.findViewById(R.id.tv_status)
}