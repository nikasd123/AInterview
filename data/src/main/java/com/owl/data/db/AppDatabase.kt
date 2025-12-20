package com.owl.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.owl.data.db.converters.RoomConverters
import com.owl.data.db.dao.SessionDao
import com.owl.data.db.entity.SessionEntity

@Database(entities = [SessionEntity::class], version = 1, exportSchema = false)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
}