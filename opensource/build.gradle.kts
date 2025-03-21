
import com.android.build.api.variant.BuildConfigField
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.diia.android.application)
    alias(libs.plugins.diia.android.application.compose)
    alias(libs.plugins.diia.android.application.flavors)
    alias(libs.plugins.diia.hilt)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.navigation.safeargs)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.gms)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.huawei.agconnect)
}

androidComponents {
    onVariants {
        val platformType = if (it.flavorName == "huawei") "\"Huawei\"" else "\"Android\""
        it.buildConfigFields.put(
            "PLATFORM_TYPE",
            BuildConfigField("String", platformType, "platform type")
        )
    }
}
android {
    namespace = "ua.gov.diia.opensource"

    signingConfigs {
        getByName("debug") {
            val props = Properties()
            val propsFile = file("keys/debug.properties")
            if (propsFile.canRead()) {
                val fileInputStream = FileInputStream(propsFile)
                props.load(fileInputStream)
                fileInputStream.close()

                storeFile = file(props["storePath"] as String)
                storePassword = props["storePassword"] as String
                keyAlias = props["keyAlias"] as String
                keyPassword = props["keyPassword"] as String
            }
        }
    }
    packaging {
        resources {
            excludes.addAll(listOf("META-INF/ASL-2.0.txt", "META-INF/LGPL-3.0.txt"))
            pickFirsts.addAll(listOf("draftv4/schema", "draftv3/schema"))
        }
    }

    val versionProps = Properties()
    FileInputStream(file("version.properties")).run {
        versionProps.load(this)
        close()
    }

    defaultConfig {
        applicationId = "ua.gov.diia.opensource"
        versionCode = versionProps["VERSION_CODE"].toString().toInt()
        versionName = versionProps["VERSION_NAME"].toString()

        buildConfigField("long", "TOKEN_LEEWAY", "1")
    }

    sourceSets {
        getByName("main") {
            jniLibs.srcDirs("libs/native")
        }
    }
    buildFeatures {
        buildConfig = true
        dataBinding = true
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("String", "SERVER_URL", "\"https://api2oss.diia.gov.ua\"")
            buildConfigField("String","BANK_ID_CALLBACK_URL","\"https://api2oss.diia.gov.ua/api/v1/auth/bank-id/code/callback\"")
            multiDexKeepProguard = file("multidex_keep_file.pro")
        }
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("String", "SERVER_URL", "\"https://api2oss.diia.gov.ua\"")
            buildConfigField("String","BANK_ID_CALLBACK_URL","\"https://api2oss.diia.gov.ua/api/v1/auth/bank-id/code/callback\"")
            multiDexKeepProguard = file("multidex_keep_file.pro")
        }
    }

    configurations.configureEach {
        resolutionStrategy {
            force(libs.okhttp)
            force(libs.okhttp.logging)
        }
    }

    kapt {
        correctErrorTypes = true
    }

    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }

}

dependencies {
    implementation(projects.libs.core)
    implementation(projects.libs.uiBase)
    implementation(projects.features.splash)
    implementation(projects.features.home)
    implementation(projects.features.menu)
    implementation(projects.features.feed)
    implementation(projects.libs.web)
    implementation(projects.features.verification)
    implementation(projects.features.bankid)
    implementation(projects.features.login)
    implementation(projects.ps.publicservice)
    implementation(projects.ps.psCriminalCert)
    implementation(projects.features.pin)
    implementation(projects.libs.biometric)
    implementation(projects.features.notifications)
    implementation(projects.libs.analytics)
    implementation(projects.features.search)
    implementation(projects.features.addressSearch)
    implementation(projects.libs.diiaStorage)
    implementation(projects.doc.documents)
    implementation(projects.doc.docManualOptions)
    implementation(projects.doc.docDriverLicense)
    implementation(projects.features.contextMenu)

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))

    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.appcompat)

    implementation(libs.light.compressor)
    //kotlin
    implementation(libs.androidx.core.ktx)
    //constraint
    implementation(libs.androidx.constraint.layout)
    //lifecycle
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    //work
    implementation(libs.androidx.work.ktx)
    //navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    //hilt
    implementation(libs.hilt.ext.work)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    ksp(libs.hilt.compiler)
    ksp(libs.hilt.ext.compiler)
    //recycler
    implementation(libs.androidx.recyclerview)
    //viewpager
    implementation(libs.androidx.viewpager2)
    //glide
    ksp(libs.glide.ksp)
    implementation(libs.glide)
    implementation(libs.glide.okhttp)
    //material
    implementation(libs.material)
    //coroutine
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    //retrofit
    implementation(libs.retrofit.core)
    //okhttp
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit2.kotlin.coroutines.adapter)
    implementation(libs.converter.gson)

    // Moshi
    implementation(libs.moshi)
    implementation(libs.moshi.adapters)
    implementation(libs.moshi.kotlin)
    implementation(libs.retrofit.moshi.converter)
    ksp(libs.moshi.codegen)
    //multidex
    implementation(libs.androidx.multidex)
    //Firebase SDK
    gplayImplementation(platform(libs.firebase.bom))
    gplayImplementation(libs.firebase.crashlytics)
    //Huawei SDK
    huaweiImplementation(libs.huawei.agconnect.crash)
}