import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import ua.gov.diia.libs

class PublicServiceConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("diia.android.feature")
                apply("diia.android.library.compose")
                apply("diia.android.library.jacoco")
                apply("org.jetbrains.kotlin.plugin.parcelize")
            }
            dependencies {
                add("implementation", project(":ps:publicservice"))
                add("implementation", libs.findLibrary("androidx.browser").get())
                add("implementation", libs.findLibrary("androidx.hilt.navigation.fragment").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.livedata.ktx").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.viewmodel.ktx").get())
                add("implementation", libs.findLibrary("androidx.navigation.fragment.ktx").get())
                add("implementation", libs.findLibrary("androidx.navigation.ui.ktx").get())
                add("implementation", libs.findLibrary("androidx.paging.ktx").get())
                add("implementation", libs.findLibrary("androidx.viewpager2").get())
                add("implementation", libs.findLibrary("glide").get())
                add("implementation", libs.findLibrary("lottie").get())
                add("implementation", libs.findLibrary("retrofit.core").get())
                add("implementation", libs.findBundle("moshi").get())
                add("ksp", libs.findLibrary("moshi.codegen").get())
                add("ksp", libs.findLibrary("glide.ksp").get())
                add("testImplementation", libs.findBundle("mockito").get())
                add("testImplementation", libs.findLibrary("androidx.arch.core.testing").get())
                add("testImplementation", libs.findLibrary("kotlinx.coroutines.test").get())
                add("testImplementation", libs.findLibrary("turbine").get())
            }
        }
    }
}