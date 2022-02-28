import org.gradle.api.JavaVersion.VERSION_11

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = libs.versions.sdk.compile.get().toInt()

    defaultConfig {
        applicationId = "com.burrowsapps.example.gif"
        versionCode = 1
        versionName = "1.0"
        minSdk = libs.versions.sdk.min.get().toInt()
        targetSdk = libs.versions.sdk.target.get().toInt()

        testApplicationId = "burrows.apps.example.gif.test"
        testInstrumentationRunner =
            "test.CustomTestRunner" // "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments += mapOf(
            "clearPackageData" to "true",
            "disableAnalytics" to "true",
        )

    }

    buildTypes {
        release {
            minifyEnabled = false
            proguardFiles += listOf(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                file("${project.rootDir}/config/proguard/proguard-rules.txt")
            )
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = VERSION_11
        targetCompatibility = VERSION_11
    }

    kotlinOptions {
        jvmTarget = VERSION_11.toString()
    }
}

dependencies {

    implementation ("androidx.core:core-ktx:1.7.0")
    implementation ("androidx.appcompat:appcompat:1.4.1")
    implementation ("com.google.android.material:material:1.5.0")
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.3")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.4.0")

    implementation ("androidx.fragment:fragment-ktx:1.4.1")

    //hilt
    implementation ("com.google.dagger:hilt-android:2.38.1")
    kapt ("com.google.dagger:hilt-compiler:2.37")
    kapt ("androidx.hilt:hilt-compiler:1.0.0")

    implementation project(":feature-network-impl")


    implementation project(":feature-day-utils")

    implementation project(":feature-holiday-utils")
    implementation project(":feature-holiday-api")

    implementation project(":feature-cache-impl")

    implementation project(":common")

    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.7.2")
}