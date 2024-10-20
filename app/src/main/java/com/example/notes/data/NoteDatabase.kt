package com.example.notes.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NoteDb::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        private var INSTANCE: NoteDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "notes.db"

        fun getInstance(application: Application): NoteDatabase {
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                val database = Room.databaseBuilder(
                    application,
                    NoteDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = database
                return database
            }
        }
    }
}