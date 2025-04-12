// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false // Upgrade to 2.0.0
    id("com.google.devtools.ksp") version "2.0.0-1.0.24" apply false // Match Kotlin 2.0.0
}