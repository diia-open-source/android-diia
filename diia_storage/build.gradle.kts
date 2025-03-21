plugins {
    alias(libs.plugins.diia.android.library)
    alias(libs.plugins.diia.android.library.jacoco)
    alias(libs.plugins.diia.hilt)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "ua.gov.diia.diia_storage"
}

dependencies {

    implementation(projects.libs.core)
    implementation(libs.moshi)
    implementation(libs.androidx.dataStore.preferences)
    implementation(libs.androidx.securityCrypto)

    testImplementation(libs.androidx.arch.core.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.bundles.mockito)
    testImplementation(libs.turbine)
    testImplementation(libs.mockwebserver)
    testImplementation(libs.hamcrest)
    testImplementation(libs.androidx.lifecycle.livedata.ktx)
}