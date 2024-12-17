plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}


android {
    namespace = "com.example.loginsignup"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.loginsignup"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // AndroidX Libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Firebase Libraries
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore.ktx)



    // CircleImageView Library (Explicitly Added Dependency)
    implementation(libs.circleimageview)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    // retrofit gson

        implementation (libs.kotlinx.coroutines.core)
        implementation (libs.kotlinx.coroutines.android)
        implementation (libs.retrofit)
        implementation (libs.converter.gson)
        implementation (libs.okhttp)
     //for glide
        implementation (libs.glide)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth.ktx)
    annotationProcessor (libs.compiler)
    //for gif
    implementation (libs.android.gif.drawable)
}




