plugins {
    alias(libs.plugins.diia.android.feature)
    alias(libs.plugins.diia.android.library.jacoco)
    alias(libs.plugins.diia.android.library.compose)
}

android {
    namespace = "ua.gov.diia.splash"

    buildTypes {
        debug {
            enableUnitTestCoverage = true
        }
    }
}

dependencies {
    implementation(projects.libs.diiaStorage)

    implementation(libs.androidx.constraint.layout)
    implementation(libs.androidx.work.ktx)
    implementation(libs.androidx.compose.material3)
    implementation(libs.hilt.ext.work)
    implementation(libs.lottie)
    implementation(libs.lottie.compose)

    //test
    testImplementation(libs.androidx.arch.core.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.bundles.mockito)
    testImplementation(libs.turbine)
    testImplementation(libs.mockwebserver)
    testImplementation(libs.hamcrest)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
}