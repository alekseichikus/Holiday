plugins {
    id ("com.android.library")
    id ("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
    id ("dagger.hilt.android.plugin")
}

android {
    compileSdkVersion(31)

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(31)


        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    testImplementation(projects.featureCountryUtils)
    testImplementation(projects.featureHolidayUtils)
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.hamcrest:hamcrest-all:1.3")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation ("org.robolectric:robolectric:4.7.3")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.0") {
        // https://github.com/Kotlin/kotlinx.coroutines/tree/master/kotlinx-coroutines-debug#debug-agent-and-android
        exclude("org.jetbrains.kotlinx", "kotlinx-coroutines-debug")
    }

    testImplementation("androidx.test:core-ktx:1.4.0")
    testImplementation("androidx.test.ext:junit:1.1.3")

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")


    implementation("androidx.test.ext:junit:1.1.3")
    implementation("androidx.test.espresso:espresso-core:3.4.0")

    implementation ("androidx.fragment:fragment-ktx:1.3.6")

    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //hilt
    implementation("com.google.dagger:hilt-android:2.38.1")
    kapt("com.google.dagger:hilt-compiler:2.37")
    kapt("androidx.hilt:hilt-compiler:1.0.0")

    implementation("com.squareup.retrofit2:retrofit:2.7.2")

    implementation(projects.common)

    implementation(projects.featureDayUtils)

    implementation(projects.featureNetworkImpl)

    implementation(projects.featureHolidayUtils)
    implementation(projects.featureHolidayApi)

    implementation(projects.featureCacheImpl)
}