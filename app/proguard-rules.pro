# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#Event bus rules
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }


#--------------------------------------------------
# keep anything annotated with @Expose
-keepclassmembers public class * {
    @com.google.gson.annotations.Expose *;
}
# Also keep classes that @Expose everything
-keep @com.google.gson.annotations.Expose public class *

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation


-keep class **$Properties {*;}
-keep class com.finopaytech.finosdk.models.sqlite.*{*;}

# If you do not use SQLCipher:
-dontwarn net.sqlcipher.database.**
# If you do not use RxJava:
-dontwarn rx.**

#New proguard rules---------------------

#Fingerprint
-keep class com.easypay.epmoney.epmoneylib.request_model.finger_model.uid.* {*;}


