# Preserve public SDK entry points and model classes
-keep class com.caribeanroyal.ecommercesample.sdk.checkout.core.** { *; }
-keep class com.caribeanroyal.ecommercesample.sdk.checkout.ui.** { *; }

# Respect @Keep annotations across the SDK
-keep @androidx.annotation.Keep class * {
    *;
}
-keepclasseswithmembers class * {
    @androidx.annotation.Keep <fields>;
}
-keepclasseswithmembers class * {
    @androidx.annotation.Keep <methods>;
}
