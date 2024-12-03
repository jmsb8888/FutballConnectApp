plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.parcelize)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    kotlin("plugin.serialization")

}

android {
    namespace = "com.task.futballconnectapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.task.futballconnectapp"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation ("androidx.compose.foundation:foundation:1.5.0")
    // Unit test
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    // Android test
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation ("io.coil-kt:coil-compose:2.4.0")
    //bd sqlserver
    implementation(libs.jtds)
    implementation(libs.mssql.jdbc)
    implementation(libs.mssql.jdbc.v1220jre8)
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    testImplementation ("org.mockito:mockito-core:4.8.0")

    // Para usar Mockito con Kotlin
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.0.0")

    // Para pruebas en Android (si estás haciendo pruebas instrumentadas)
    androidTestImplementation ("org.mockito:mockito-android:4.8.0")
    androidTestImplementation ("androidx.compose.ui:ui-test-junit4:1.5.0") // o la versión que estés usando
    androidTestImplementation ("androidx.compose.ui:ui-test-manifest:1.5.0")

    // Para MockK y otras dependencias de pruebas
    testImplementation ("io.mockk:mockk:1.13.4")
    testImplementation ("junit:junit:4.13.2")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")

}