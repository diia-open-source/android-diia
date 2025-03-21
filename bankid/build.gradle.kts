plugins {
    alias(libs.plugins.diia.android.feature)
    alias(libs.plugins.diia.android.library.jacoco)
    alias(libs.plugins.diia.android.library.compose)
}

android {
    namespace = "ua.gov.diia.bankid"

    defaultConfig {
        testOptions {
            unitTests {
                isReturnDefaultValues = true
                isIncludeAndroidResources = true
            }
        }
    }
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(projects.features.verification)

    implementation(libs.androidx.recyclerview)
    implementation(libs.material)
    implementation(libs.flexbox)

    implementation(libs.bundles.moshi)
    ksp(libs.moshi.codegen)

    testImplementation(libs.androidx.arch.core.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.bundles.mockito)
    testImplementation(libs.turbine)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
}