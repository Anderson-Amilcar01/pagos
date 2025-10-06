plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.20"
    alias(libs.plugins.google.devtools.ksp)
}

android {
    namespace = "com.example.pagos"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.pagos"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.7.5"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // --- 🔹 Core Android + Lifecycle ---
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.activity:activity-compose:1.9.3")

    // --- 🔹 Jetpack Compose ---
    implementation(platform("androidx.compose:compose-bom:2025.09.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:2.8.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
    implementation("androidx.compose.material:material-icons-extended")

    // --- 🔹 Material Design (para XML si usas) ---
    implementation("com.google.android.material:material:1.12.0")

    // --- 🔹 Retrofit + Gson ---
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.google.code.gson:gson:2.11.0")

    // --- 🔹 Coroutines ---
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

    // --- 🔹 Librerías recomendadas para Compose ---
    // Accompanist (utilidades extra)
    implementation("com.google.accompanist:accompanist-permissions:0.36.0")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.36.0")

    // Landscapist (carga de imágenes con Coil)
    implementation("com.github.skydoves:landscapist-coil:2.3.6")

    // Compose Destinations (navegación type-safe)
    implementation("io.github.raamcosta.compose-destinations:core:2.2.0")
    ksp("io.github.raamcosta.compose-destinations:ksp:2.2.0")

    // Vico (gráficas)
    implementation("com.patrykandpatrick.vico:compose:2.2.1")

    // Lyricist (internacionalización)
    implementation("cafe.adriel.lyricist:lyricist:1.7.0")

    // Snapper (scroll con “snap” en listas)
    implementation("dev.chrisbanes.snapper:snapper:0.3.0")

    // --- 🔹 Pruebas ---
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2025.09.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
