plugins {
    alias(libs.plugins.diia.android.feature)
    alias(libs.plugins.diia.android.library.jacoco)
    alias(libs.plugins.diia.android.library.compose)
    alias(libs.plugins.kotlin.kapt) // need kapt for @BindingAdapter
}

android {
    namespace = "ua.gov.diia.search"

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(libs.androidx.constraint.layout.compose)
    implementation(libs.androidx.recyclerview)

    testImplementation(libs.androidx.arch.core.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.bundles.mockito)
    testImplementation(libs.turbine)
}