// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.binary.compatibility.validator) apply true
}

apiValidation {
    ignoredProjects += listOf("app", "app-headless", "network", "database", "domain", "booking", "designsystem")
}
