plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.loginsignup"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.loginsignup"
        minSdk = 28
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

    // Google Sign-In
    implementation("com.google.android.gms:play-services-auth:20.0.0")
    implementation("com.google.android.gms:play-services-base:18.0.1")

    // Firebase Libraries
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.auth.ktx)

    // CircleImageView Library (Explicitly Added Dependency)
    implementation(libs.circleimageview)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Retrofit and Gson for API requests
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)

    // Glide for image loading
    implementation(libs.glide)

    // Testing Libraries
    implementation(libs.androidx.junit.ktx)
    implementation(libs.androidx.monitor)
    androidTestImplementation(libs.junit.junit)
    annotationProcessor(libs.compiler)

    // Additional Libraries
    implementation(libs.android.gif.drawable) // For GIF support
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0") // SwipeRefreshLayout
    implementation("androidx.appcompat:appcompat:1.3.1") // AppCompat
    implementation("com.google.android.material:material:1.4.0") // Material Components
    testImplementation("junit:junit:4.13.2")
    implementation("androidx.recyclerview:recyclerview:1.2.1") // RecyclerView
}

// Ensure this is at the bottom of the file
apply(plugin = "com.google.gms.google-services")
