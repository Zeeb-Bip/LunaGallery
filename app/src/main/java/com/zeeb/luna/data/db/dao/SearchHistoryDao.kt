package com.zeeb.luna.data.db.dao

import androidx.room.*
import com.zeeb.luna.data.db.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {
	    @Query("SELECT * FROM search_history ORDER BY timestamp DESC LIMIT 10")
	        fun getRecentSearch(): Flow<List<SearchHistoryEntity>>

	            @Insert(onConflict = OnConflictStrategy.REPLACE)
	                suspend fun insertSearch(search: SearchHistoryEntity)

	                    @Query("DELETE FROM search_history")
	                        suspend fun clearHistory()
	                        }
}
