package com.zeeb.luna.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zeeb.luna.data.db.dao.AlbumDao
import com.zeeb.luna.data.db.dao.MediaDao
import com.zeeb.luna.data.db.dao.SearchHistoryDao
import com.zeeb.luna.data.db.entity.AlbumEntity
import com.zeeb.luna.data.db.entity.MediaItemEntity
import com.zeeb.luna.data.db.entity.SearchHistoryEntity

@Database(
    entities = [MediaItemEntity::class, AlbumEntity::class, SearchHistoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class LunaDatabase : RoomDatabase() {
    abstract fun mediaDao(): MediaDao
    abstract fun albumDao(): AlbumDao
    abstract fun searchHistoryDao(): SearchHistoryDao

    companion object {
        @Volatile private var INSTANCE: LunaDatabase? = null

        fun getInstance(context: Context): LunaDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    LunaDatabase::class.java,
                    "luna_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
