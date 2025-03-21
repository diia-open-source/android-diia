plugins {
    alias(libs.plugins.diia.android.feature)
}

android {
    namespace = "ua.gov.diia.web"

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(libs.androidx.browser)
}