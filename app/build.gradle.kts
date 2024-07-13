plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.androidx.navigation.safe.args)
    alias(libs.plugins.dagger.hilt.android)
}

android {
    namespace = "com.example.coingeckotask"
    compileSdk = 34

    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        applicationId = "com.example.coingeckotask"
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
        dataBinding = true
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    // Hilt dependencies
    implementation(libs.hilt)
    annotationProcessor(libs.hilt.compiler)
    implementation(libs.hilt.androidx.compiler)
    implementation(libs.hilt.navigation.graph)

    // Add any other dependencies you need
    implementation(libs.timber)
    implementation(libs.androidx.annotations)
    implementation(libs.androidx.legacy.support)
    implementation(libs.recycler.view)
    implementation(libs.card.view)
    implementation(libs.gson)
    implementation(libs.fragment.ktx)
    implementation(libs.view.binding)
    implementation(libs.coil)
    implementation(libs.shimmer)
    implementation(libs.lifecycle)
    implementation(libs.nav.fragment)
    implementation(libs.nav.ui)
    implementation(libs.intuit)
    implementation(libs.datastore)
    implementation(libs.splash.screen)
    implementation(libs.retrofit2)
    implementation(libs.converter.gson)
    implementation(libs.retrofit.interceptor)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    implementation(libs.coroutines.play.service)
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)
    implementation(libs.room.runtime)
    testImplementation(libs.mockito)
    testImplementation(libs.mockito.core)
    annotationProcessor(libs.data.binding.compiler)

    configurations {
        named("implementation") {
            exclude(group = "org.jetbrains", module = "annotations")
        }
        named("api") {
            exclude(group = "org.jetbrains", module = "annotations")
        }
        named("implementation") {
            exclude(group = "org.jetbrains", module = "annotations")
        }
    }
}