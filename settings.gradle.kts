pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

rootProject.name = "Diia"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
// apps
include(":apps:opensource")
project(":apps:opensource").projectDir = File(rootDir, "opensource")

// libs
include(":libs:ui_base")
project(":libs:ui_base").projectDir = File(rootDir, "ui_base")
include(":libs:core")
project(":libs:core").projectDir = File(rootDir, "core")
include(":libs:diia_storage")
project(":libs:diia_storage").projectDir = File(rootDir, "diia_storage")
include(":libs:web")
project(":libs:web").projectDir = File(rootDir, "web")
include(":libs:analytics")
project(":libs:analytics").projectDir = File(rootDir, "analytics")
include(":libs:biometric")
project(":libs:biometric").projectDir = File(rootDir, "biometric")

//features
include(":features:splash")
project(":features:splash").projectDir = File(rootDir, "splash")
include(":features:verification")
project(":features:verification").projectDir = File(rootDir, "verification")
include(":features:bankid")
project(":features:bankid").projectDir = File(rootDir, "bankid")
include(":features:menu")
project(":features:menu").projectDir = File(rootDir, "menu")
include(":features:login")
project(":features:login").projectDir = File(rootDir, "login")
include(":features:pin")
project(":features:pin").projectDir = File(rootDir, "pin")
include(":features:notifications")
project(":features:notifications").projectDir = File(rootDir, "notifications")
include(":features:search")
project(":features:search").projectDir = File(rootDir, "search")
include(":features:address_search")
project(":features:address_search").projectDir = File(rootDir, "address_search")
include(":features:feed")
project(":features:feed").projectDir = File(rootDir, "feed")
include(":features:context_menu")
project(":features:context_menu").projectDir = File(rootDir, "context_menu")
include(":features:home")
project(":features:home").projectDir = File(rootDir, "home")

//features:doc
include(":doc:documents")
project(":doc:documents").projectDir = File(rootDir, "documents")
include(":doc:doc_driver_license")
project(":doc:doc_driver_license").projectDir = File(rootDir, "doc_driver_license")
include(":doc:doc_manual_options")
project(":doc:doc_manual_options").projectDir = File(rootDir, "doc_manual_options")

//features:ps
include(":ps:publicservice")
project(":ps:publicservice").projectDir = File(rootDir, "publicservice")
include(":ps:ps_criminal_cert")
project(":ps:ps_criminal_cert").projectDir = File(rootDir, "ps_criminal_cert")