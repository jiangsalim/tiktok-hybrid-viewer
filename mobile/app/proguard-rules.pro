# Keep data models
-keep class com.tiktokviewer.domain.model.** { *; }

# Keep Room entities
-keep class com.tiktokviewer.data.local.entity.** { *; }

# Keep DTOs
-keep class com.tiktokviewer.data.remote.dto.** { *; }

# Keep Retrofit interfaces
-keep,allowobfuscation interface com.tiktokviewer.data.remote.ApiService

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep JNI bridge
-keep class com.tiktokviewer.domain.engine.light.SignatureGenerator {
    native <methods>;
}

# Strip debug symbols
-renamesourcefileattribute SourceFile
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,EnclosingMethod

# General obfuscation
-obfuscationdictionary obfuscation-dictionary.txt
-classobfuscationdictionary obfuscation-dictionary.txt
-packageobfuscationdictionary obfuscation-dictionary.txt
