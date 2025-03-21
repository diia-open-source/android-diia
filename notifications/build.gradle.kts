plugins {
    alias(libs.plugins.diia.android.feature)
    alias(libs.plugins.diia.android.library.jacoco)
    alias(libs.plugins.diia.android.library.compose)
}

android {
    namespace = "ua.gov.diia.notifications"

    buildFeatures {
        buildConfig = true
        dataBinding = true
    }
}

dependencies {

    implementation(projects.libs.diiaStorage)
    implementation(projects.libs.analytics)

    implementation(libs.androidx.constraint.layout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.localbroadcast)
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.paging.ktx)
    implementation(libs.androidx.work.ktx)
    implementation(libs.retrofit.core)
    implementation(libs.bundles.moshi)
    implementation(libs.bundles.exoplayer)

    //hilt
    implementation(libs.hilt.ext.work)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    ksp(libs.hilt.compiler)
    ksp(libs.hilt.ext.compiler)

    ksp(libs.moshi.codegen)
    implementation(libs.glide)
    ksp(libs.glide.ksp)
    implementation(libs.shortcut.badger)

    gplayImplementation(platform(libs.firebase.bom))
    gplayImplementation(libs.firebase.cloud.messaging)
    huaweiImplementation(libs.huawei.push)

    testImplementation(libs.androidx.arch.core.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.bundles.mockito)
    testImplementation(libs.turbine)
    testImplementation(libs.mockwebserver)
    testImplementation(libs.hamcrest)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
}