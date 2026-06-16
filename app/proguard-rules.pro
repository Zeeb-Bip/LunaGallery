-keep class com.zeeb.luna.data.db.entity.** { *; }
-keep class com.zeeb.luna.data.db.dao.** { *; }
-keep class com.zeeb.luna.domain.model.** { *; }
-keepclassmembers class * {
	    @androidx.room.* <methods>;
	        @androidx.room.* <fields>;
	        }
	        -keep class androidx.media3.** { *; }
	        -dontwarn androidx.media3.**
	        -keep class coil.** { *; }
	        -dontwarn coil.**
	        -keep class * extends androidx.work.Worker
	        -keep class * extends androidx.work.ListenableWorker {
	        	    public <init>(android.content.Context,androidx.work.WorkerParameters);
	        	    }
	        }
}
