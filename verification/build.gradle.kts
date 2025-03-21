plugins {
    alias(libs.plugins.diia.android.feature)
    alias(libs.plugins.diia.android.library.jacoco)
    alias(libs.plugins.kotlin.kapt) // need kapt for @BindingAdapter
}

android {
    namespace = "ua.gov.diia.verification"
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

    implementation(libs.androidx.constraint.layout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.material)
    implementation(libs.flexbox)
    implementation(libs.retrofit.core)

    implementation(libs.bundles.moshi)
    ksp(libs.moshi.codegen)

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