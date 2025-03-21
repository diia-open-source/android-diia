plugins {
    alias(libs.plugins.diia.android.feature)
    alias(libs.plugins.diia.android.library.jacoco)
    alias(libs.plugins.diia.android.library.compose)
}


android {
    namespace = "ua.gov.diia.pin"

    buildTypes {
        debug {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
    }

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(projects.libs.diiaStorage)

    implementation(libs.androidx.constraint.layout.compose)
    implementation(libs.androidx.compose.material3)

    // Moshi
    implementation(libs.bundles.moshi)
    ksp(libs.moshi.codegen)

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