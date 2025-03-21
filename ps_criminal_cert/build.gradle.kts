plugins {
    alias(libs.plugins.diia.public.service)
}
android {
    namespace = "ua.gov.diia.ps_criminal_cert"

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(projects.diia.features.search)
    implementation(projects.diia.features.addressSearch)

    implementation(libs.better.link)

    testImplementation(libs.mockwebserver)
    testImplementation(libs.hamcrest)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
}