pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
        maven(url = "https://jitpack.io")
        maven(url = "https://developer.huawei.com/repo/")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
        maven(url = "https://developer.huawei.com/repo/")
    }
}

rootProject.name = "Diia"

include(":ui_base")
include(":core")
include(":home")
include(":opensource")
include(":splash")
include(":verification")
include(":bankid")
include(":menu")
include(":web")
include(":login")
include(":publicservice")
include(":pin")
include(":biometric")
include(":notifications")
include(":search")
include(":address_search")
include(":ps_criminal_cert")
include(":analytics")
include(":diia_storage")
include(":documents")
include(":doc_driver_license")
