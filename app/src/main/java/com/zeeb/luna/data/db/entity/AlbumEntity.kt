package com.zeeb.luna.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "albums")
data class AlbumEntity(
	    @PrimaryKey val id: Long,
	        val name: String,
	            val coverUri: String,
	                val itemCount: Int,
	                    val totalSize: Long,
	                        val isManual: Boolean = false
	                        )
)
