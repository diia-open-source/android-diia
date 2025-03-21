plugins {
    alias(libs.plugins.diia.android.feature)
    alias(libs.plugins.diia.android.library.jacoco)
    alias(libs.plugins.diia.android.library.compose)
}

android {
    namespace = "ua.gov.diia.home"
}

dependencies {
    implementation(projects.libs.diiaStorage)
    implementation(projects.doc.documents)
    implementation(projects.features.feed)
    implementation(projects.features.menu)
    implementation(projects.ps.publicservice)

    implementation(libs.lottie)
    implementation(libs.lottie.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.constraint.layout)

    testImplementation(libs.androidx.arch.core.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.bundles.mockito)
    testImplementation(libs.turbine)
}