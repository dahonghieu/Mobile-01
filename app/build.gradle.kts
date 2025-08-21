plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.dahonghieu_2122110267"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.dahonghieu_2122110267"
        minSdk = 24
        targetSdk = 36
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // RecyclerView (bắt buộc vì dùng RV)
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Volley (call API)
    implementation("com.android.volley:volley:1.2.1")

    // Glide (load ảnh)
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0") // dùng Java
    implementation("com.squareup.picasso:picasso:2.8")

    // --- Nếu CHƯA dùng thì để comment cho nhẹ project ---
    // Retrofit + Gson converter:
    // implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Không cần nếu đã dùng Glide:
    // implementation("com.squareup.picasso:picasso:2.8")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
