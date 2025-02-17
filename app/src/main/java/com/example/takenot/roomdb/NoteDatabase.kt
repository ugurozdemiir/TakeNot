package com.example.takenot.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.takenot.model.Note

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabasebase : RoomDatabase() {
    abstract fun noteDAO(): NoteDAO
}