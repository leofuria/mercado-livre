plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.navigation.safeargs)
    alias(libs.plugins.ksp)
}

android {
    namespace = "br.com.bitsolutions.mercadolivre"
    compileSdk = 35

    defaultConfig {
        applicationId = "br.com.bitsolutions.mercadolivre"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
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
        buildConfig = true
    }
    testOptions {
        unitTests { isIncludeAndroidResources = true }
        unitTests.all { test ->
            test.testLogging {
                events("passed", "skipped", "failed", "standardOut", "standardError")
                test.outputs.upToDateWhen { false }
                showStandardStreams = true
            }
        }
    }
    testBuildType = "debug"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))

    implementation(libs.coil)
    implementation(libs.coil.networking)
    implementation(libs.reactivex.rxjava)
    implementation(libs.reactivex.rxandroid)
    implementation(libs.reactivex.rxkotlin)
    implementation(libs.kotlin.coroutines.android)
    api(libs.kotlin.coroutines.core)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.converter.gson)
    implementation(libs.squareup.okhttp)
    implementation(libs.squareup.logging.interceptor)
    implementation(libs.squareup.adapter.rxjava)
    implementation(libs.squareup.json)
    implementation(libs.squareup.rest.json.converter)
    api(libs.room.ktx)
    api(libs.room.runtime)
    ksp(libs.room.compiler)

    testImplementation(libs.junit)
    testImplementation(libs.test.mockk)
    testImplementation(libs.test.mockito.android)
    testImplementation(libs.test.mockito.inline)
    testImplementation(libs.test.coroutines.testing)
    testImplementation(libs.test.turbine)
    androidTestImplementation(libs.test.mockk.android)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
