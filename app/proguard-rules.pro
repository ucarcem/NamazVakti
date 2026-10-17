# Keep Gson DTOs and Data Models
-keep class com.example.namazvakti.data.remote.dto.** { *; }
-keep class com.example.namazvakti.data.local.entity.** { *; }
-keep class com.example.namazvakti.data.local.datastore.** { *; }

# Keep SerializedName annotations and reflection for Gson
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Keep Room
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# Keep Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
