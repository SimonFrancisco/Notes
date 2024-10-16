package com.example.notes.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Mode() : Parcelable {
    ADD, EDIT
}