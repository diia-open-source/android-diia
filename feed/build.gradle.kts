plugins {
    alias(libs.plugins.diia.android.feature)
    alias(libs.plugins.diia.android.library.jacoco)
    alias(libs.plugins.diia.android.library.compose)
}

android {
    namespace = "ua.gov.diia.feed"

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(projects.libs.analytics)
    implementation(projects.libs.diiaStorage)

    implementation(libs.retrofit.core)
    implementation(libs.bundles.moshi)
    ksp(libs.moshi.codegen)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.viewpager2)
    implementation(libs.glide)
    ksp(libs.glide.ksp)
    implementation(libs.lottie)
    implementation(libs.better.link)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.paging.ktx)

    //test
    testImplementation(libs.androidx.arch.core.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.bundles.mockito)
    testImplementation(libs.turbine)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
}