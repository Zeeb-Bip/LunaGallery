package com.zeeb.luna.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.zeeb.luna.data.db.LunaDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class TrashCleanupWorker(
	    context: Context,
	        params: WorkerParameters
	        ) : CoroutineWorker(context, params) {

	        	    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
	        	    	        try {
	        	    	        	            val database = LunaDatabase.getDatabase(applicationContext)
	        	    	        	                        val mediaDao = database.mediaDao()

	        	    	        	                                                // Ambil preferensi user (nanti diimplementasikan di Settings)
	        	    	        	                                                            // Untuk sekarang gunakan default 30 hari
	        	    	        	                                                                        val daysLimit = 30
	        	    	        	                                                                                    val threshold = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(daysLimit.toLong())

	        	    	        	                                                                                                            // Hapus item yang isDeleted = 1 dan deletedAt < threshold
	        	    	        	                                                                                                                        mediaDao.cleanupOldTrash(threshold)

	        	    	        	                                                                                                                                                Result.success()
	        	    	        	                                                                                                                                                        } catch (e: Exception) {
	        	    	        	                                                                                                                                                        	            Result.failure()
	        	    	        	                                                                                                                                                        	                    }
	        	    	        	                                                                                                                                                        	                        }
	        	    	        	                                                                                                                                                        	                        }
	        	    	        	                                                                                                                                                        }
	        	    	        }
	        	    }
	        }
)
