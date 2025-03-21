plugins {
    alias(libs.plugins.diia.android.library)
    alias(libs.plugins.diia.android.library.jacoco)
    alias(libs.plugins.diia.android.library.compose)
    alias(libs.plugins.diia.hilt)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "ua.gov.diia.core"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation(libs.better.link)
    implementation(libs.lottie)
    implementation(libs.androidx.work.ktx)
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.exifInterface)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.light.compressor)
    implementation(libs.retrofit.core)
    implementation(libs.glide)
    implementation(libs.mlkit.vision)
    ksp(libs.glide.ksp)
    implementation(libs.bundles.moshi)
    ksp(libs.moshi.codegen)
    implementation(libs.zxing)

    implementation(libs.androidx.hilt.navigation.fragment)
    implementation(libs.hilt.ext.work)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    ksp(libs.hilt.compiler)
    ksp(libs.hilt.ext.compiler)

    testImplementation(libs.androidx.arch.core.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.bundles.mockito)
    testImplementation(libs.turbine)
    testImplementation(libs.mockwebserver)
    testImplementation(libs.hamcrest)
    testImplementation(libs.androidx.lifecycle.livedata.ktx)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)

}

tasks.withType<Test> {
    // https://github.com/mockk/mockk/issues/681
    jvmArgs("--add-opens", "java.base/java.time=ALL-UNNAMED")
}