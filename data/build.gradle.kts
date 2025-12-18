plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(projects.domain)

    // Android Core
    implementation(libs.androidx.core.ktx)

    // Room Database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler) // Генерация кода Room

    // Google AI (Gemini)
    implementation(libs.google.generativeai)

    // Koin for Android (инжекция Context)
    implementation(libs.koin.android)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
}
