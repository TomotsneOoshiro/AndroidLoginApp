# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Realm specific rules
-keep class io.realm.** { *; }
-dontwarn io.realm.**

# Keep your Realm model classes
-keep class com.example.loginapplication.User { *; }
-keep class com.example.loginapplication.User$* { *; }

# Keep Realm annotations
-keep @io.realm.annotations.RealmClass class *
-keep @io.realm.annotations.PrimaryKey class *
-keep @io.realm.annotations.Required class *