plugins {
    id ("com.android.library")
    id ("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
    id ("dagger.hilt.android.plugin")
}

android {
    compileSdk = 31

    defaultConfig {
        minSdkVersion(21)
        targetSdk = 31


        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        dataBinding = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")
    implementation("junit:junit:4.13.2")
    implementation("androidx.test.ext:junit:1.1.3")
    implementation("androidx.test.espresso:espresso-core:3.4.0")

    implementation ("androidx.fragment:fragment-ktx:1.3.6")

    implementation("com.squareup.moshi:moshi:1.13.0")

    //hilt
    implementation("com.google.dagger:hilt-android:2.38.1")
    kapt("com.google.dagger:hilt-compiler:2.37")
    kapt("androidx.hilt:hilt-compiler:1.0.0")

    implementation("androidx.work:work-runtime-ktx:2.7.1")

    implementation(projects.common)

    implementation(projects.featureWorkerImpl)

    implementation(projects.featureCacheImpl)

    implementation(projects.featureHolidayUtils)
    implementation(projects.featureHolidayImpl)
    implementation(projects.featureHoliday)

    implementation(projects.featureInfoBoard)

    implementation(projects.featurePhotoUtils)

    implementation(projects.featureDayUtils)

    implementation(projects.fragmentHoliday)
    implementation(projects.fragmentFavorite)
    implementation(projects.fragmentPhoto)
    implementation(projects.fragmentAbout)

    implementation(projects.bottomCalendar)
}