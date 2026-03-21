plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.mountreach.campusmanagementsystem"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mountreach.campusmanagementsystem"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    // AndroidX
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.0")

    // Activity (stable versions for compileSdk 34)
    implementation("androidx.activity:activity:1.8.2")
    implementation("androidx.activity:activity-ktx:1.8.2")

    // UI Components
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("androidx.recyclerview:recyclerview:1.3.0")
    implementation("androidx.cardview:cardview:1.0.0")

    // Glide for image loading
    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation(libs.monitor)
    implementation(libs.ext.junit)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)
    implementation(libs.google.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.messaging)
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")

    implementation ("androidx.biometric:biometric:1.2.0-alpha05")


    //ai dependancy


    implementation ("com.squareup.okhttp3:okhttp:4.10.0")

}
