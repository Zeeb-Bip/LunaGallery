package com.zeeb.luna.util

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

object FormatUtil {
	    fun formatFileSize(size: Long): String {
	    	        if (size <= 0) return "0 B"
	    	                val units = arrayOf("B", "KB", "MB", "GB", "TB")
	    	                        val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
	    	                                return String.format("%.1f %s", size / Math.pow(1024.0, digitGroups.toDouble()), units[digitGroups])
	    	                                    }

	    	                                        fun formatDuration(ms: Long): String {
	    	                                        	        return DateUtils.formatElapsedTime(ms / 1000)
	    	                                        	            }

	    	                                        	                fun formatDate(timestamp: Long): String {
	    	                                        	                	        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
	    	                                        	                	                return sdf.format(Date(timestamp))
	    	                                        	                	                    }

	    	                                        	                	                        fun formatHeaderDate(timestamp: Long): String {
	    	                                        	                	                        	        val now = Calendar.getInstance()
	    	                                        	                	                        	                val date = Calendar.getInstance().apply { timeInMillis = timestamp }

	    	                                        	                	                        	                                return when {
	    	                                        	                	                        	                                	            DateUtils.isToday(timestamp) -> "Hari ini"
	    	                                        	                	                        	                                	                        DateUtils.isToday(timestamp + DateUtils.DAY_IN_MILLIS) -> "Kemarin"
	    	                                        	                	                        	                                	                                    now.get(Calendar.YEAR) == date.get(Calendar.YEAR) -> {
	    	                                        	                	                        	                                	                                    	                SimpleDateFormat("EEEE, d MMMM", Locale.getDefault()).format(date.time)
	    	                                        	                	                        	                                	                                    	                            }
	    	                                        	                	                        	                                	                                    	                                        else -> SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(date.time)
	    	                                        	                	                        	                                	                                    	                                                }
	    	                                        	                	                        	                                	                                    	                                                    }
	    	                                        	                	                        	                                	                                    	                                                    }
	    	                                        	                	                        	                                	                                    }
	    	                                        	                	                        	                                }
	    	                                        	                	                        }
	    	                                        	                }
	    	                                        }
	    }
}
