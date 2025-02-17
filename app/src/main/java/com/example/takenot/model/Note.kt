package com.example.takenot.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @ColumnInfo(name = "Notes")
    var Notes : String,
){
    @PrimaryKey(autoGenerate = true)
    var id = 0
}