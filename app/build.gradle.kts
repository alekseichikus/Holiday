plugins {
    id ("com.android.application")
    id ("kotlin-android")
    id ("kotlin-kapt")
    id ("dagger.hilt.android.plugin")
    id ("com.google.gms.google-services")
    id ("com.google.firebase.crashlytics")
}

android {
    compileSdk = ConfigData.compileSdkVersion

    defaultConfig {
        applicationId = "ru.createtogether.holiday"
        minSdk = ConfigData.minSdkVersion
        targetSdk = ConfigData.targetSdkVersion
        versionCode = ConfigData.versionCode
        versionName = ConfigData.versionName

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

    implementation ("androidx.core:core-ktx:1.7.0")
    implementation ("androidx.appcompat:appcompat:1.4.1")
    implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.41")
    implementation ("com.google.android.material:material:1.5.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.3")
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.3")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.4.0")

    implementation ("androidx.fragment:fragment-ktx:1.3.6")

    //hilt
    implementation ("com.google.dagger:hilt-android:2.38.1")
    implementation ("androidx.hilt:hilt-work:1.0.0")
    kapt ("com.google.dagger:hilt-compiler:2.37")
    kapt ("androidx.hilt:hilt-compiler:1.0.0")

    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.7.2")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")

    implementation ("androidx.work:work-runtime-ktx:${Versions.workKtx}")

    implementation(projects.common)

    implementation(projects.featureHolidayUtils)
    implementation(projects.featureHolidayImpl)
    implementation(projects.featureHoliday)

    implementation(projects.fragmentPhoto)
    implementation(projects.fragmentHoliday)
    implementation(projects.fragmentMain)
    implementation(projects.fragmentFavorite)
    implementation(projects.fragmentAbout)

    implementation(projects.bottomCalendar)

    implementation(projects.featureAppImpl)
}