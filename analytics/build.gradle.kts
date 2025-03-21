plugins {
    alias(libs.plugins.diia.android.library)
    alias(libs.plugins.diia.android.library.jacoco)
    alias(libs.plugins.diia.hilt)
}

android {
    namespace = "ua.gov.diia.analytics"
    buildTypes {
        debug {
            enableUnitTestCoverage = true
        }
    }
}

dependencies {
    implementation(projects.libs.core)

    gplayImplementation(libs.firebase.analytics)
    gplayImplementation(libs.firebase.crashlytics)

    huaweiImplementation(libs.huawei.analytics)
    huaweiImplementation(libs.huawei.agconnect.crash)

    testImplementation(libs.mockk)
    testImplementation(libs.junit)
}