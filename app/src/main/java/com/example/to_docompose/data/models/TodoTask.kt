package com.example.to_docompose.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.to_docompose.utils.Constants.DATABASE_TABLE
import kotlinx.parcelize.Parcelize

@Entity(tableName = DATABASE_TABLE)
@Parcelize
data class TodoTask (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0 ,
    val title: String = "",
    val description: String = "",
    val priority: Priority = Priority.NONE
): Parcelable
