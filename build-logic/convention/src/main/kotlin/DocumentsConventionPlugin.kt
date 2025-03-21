import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import ua.gov.diia.libs

class DocumentsConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("diia.android.feature")
                apply("diia.android.library.compose")
                apply("diia.android.library.jacoco")
                apply("org.jetbrains.kotlin.plugin.parcelize")
            }
            dependencies {
                add("implementation", project(":libs:ui_base"))
                add("implementation", libs.findBundle("moshi").get())
                add("ksp", libs.findLibrary("moshi.codegen").get())
                add("testImplementation", libs.findBundle("mockito").get())
                add("testImplementation", libs.findLibrary("androidx.arch.core.testing").get())
                add("testImplementation", libs.findLibrary("kotlinx.coroutines.test").get())
                add("testImplementation", libs.findLibrary("turbine").get())
            }
        }
    }
}