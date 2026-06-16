package com.zeeb.luna.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.zeeb.luna.data.db.LunaDatabase
import com.zeeb.luna.data.repository.MediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MediaSyncWorker(
	    context: Context,
	        params: WorkerParameters
	        ) : CoroutineWorker(context, params) {

	        	    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
	        	    	        try {
	        	    	        	            val database = LunaDatabase.getDatabase(applicationContext)
	        	    	        	                        val repository = MediaRepository(applicationContext, database)

	        	    	        	                                                // Melakukan sinkronisasi penuh
	        	    	        	                                                            repository.syncMediaStore()

	        	    	        	                                                                                    Result.success()
	        	    	        	                                                                                            } catch (e: Exception) {
	        	    	        	                                                                                            	            Result.retry()
	        	    	        	                                                                                            	                    }
	        	    	        	                                                                                            	                        }
	        	    	        	                                                                                            	                        }
	        	    	        	                                                                                            }
	        	    	        }
	        	    }
	        }
)
