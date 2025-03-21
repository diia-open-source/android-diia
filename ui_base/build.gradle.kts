plugins {
    alias(libs.plugins.diia.android.library)
    alias(libs.plugins.diia.android.library.jacoco)
    alias(libs.plugins.diia.android.library.compose)
    alias(libs.plugins.diia.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.kapt) // need kapt for @BindingAdapter
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.navigation.safeargs)
}

android {
    namespace = "ua.gov.diia.ui_base"
    packaging {
        resources {
            excludes.addAll(listOf("META-INF/ASL-2.0.txt", "META-INF/LGPL-3.0.txt"))
            pickFirsts.addAll(listOf("draftv4/schema", "draftv3/schema"))
        }
    }

    sourceSets {
        getByName("main") {
            jniLibs.srcDirs("libs/native")
        }
    }

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(projects.libs.core)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.material)
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.accompanist.pager)


    //retrofit
    implementation(libs.retrofit.core)

    //compose
    implementation(libs.androidx.constraint.layout.compose)
    implementation(libs.lottie.compose)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.foundation)

    implementation(libs.exoplayer.core)
    implementation(libs.exoplayer.ui)
    implementation(libs.exoplayer.hls)

    //Paging
    implementation(libs.androidx.paging.ktx)

    constraints {
        implementation(libs.kotlin.stdlib7) {
            because("kotlin-stdlib-jdk7 is now a part of kotlin-stdlib")
        }
        implementation(libs.kotlin.stdlib8) {
            because("kotlin-stdlib-jdk8 is now a part of kotlin-stdlib")
        }
    }

    //Glide
    implementation(libs.glide)
    implementation(libs.glide.compose)
    ksp(libs.glide.ksp)

    // Moshi
    implementation(libs.bundles.moshi)
    ksp(libs.moshi.codegen)

    implementation(libs.better.link)

    implementation(libs.zxing)

    //diia
    implementation(libs.reorderable)

    //test
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