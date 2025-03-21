plugins {
    alias(libs.plugins.diia.android.feature)
    alias(libs.plugins.diia.android.library.jacoco)
    alias(libs.plugins.diia.android.library.compose)
}
android {
    namespace = "ua.gov.diia.publicservice"

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(projects.diia.libs.diiaStorage)

    implementation(libs.androidx.browser)
    implementation(libs.androidx.viewpager2)
    implementation(libs.retrofit.core)
    implementation(libs.bundles.moshi)
    implementation(libs.glide)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.serialization.json)

    ksp(libs.moshi.codegen)
    ksp(libs.glide.ksp)

    testImplementation(libs.androidx.arch.core.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.bundles.mockito)
    testImplementation(libs.turbine)
    androidTestImplementation(libs.androidx.test.ext)
}