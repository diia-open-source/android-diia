plugins {
    alias(libs.plugins.diia.android.feature)
    alias(libs.plugins.diia.android.library.jacoco)
    alias(libs.plugins.diia.android.library.compose)
}

android {
    namespace = "ua.gov.diia.menu"
}

dependencies {

    implementation(projects.libs.diiaStorage)
    implementation(projects.libs.web)

    implementation(libs.androidx.compose.livedata)
    implementation(libs.androidx.constraint.layout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.compose.material3)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.androidx.arch.core.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.bundles.mockito)
    testImplementation(libs.turbine)
}