package ua.gov.diia.opensource.data.network

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import dagger.Reusable
import okhttp3.internal.platform.Platform
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import ua.gov.diia.core.util.delegation.WithCrashlytics
import javax.inject.Inject

@Reusable
class ApiLogger @Inject constructor(
    private val crashlytics: WithCrashlytics,
) : HttpLoggingInterceptor.Logger {

    override fun log(message: String) {
        if (message.startsWith("{") || message.startsWith("[")) {
            try {
                val msg = JsonParser().parse(message)
                loopThroughJson(msg)
                val prettyPrintJson = GsonBuilder()
                    .setPrettyPrinting()
                    .disableHtmlEscaping()
                    .create()
                    .toJson(msg)
                Platform.get().log(Platform.INFO, prettyPrintJson, null)
            } catch (e: Exception) {
                crashlytics.sendNonFatalError(e)
            }
        } else {
            Platform.get().log(Platform.INFO, message, null)
            return
        }
    }

    @Throws(JSONException::class)
    fun loopThroughJson(input: Any) {
        if (input is JsonObject) {
            val keys: Set<String> = input.keySet()
            val base64 = mutableListOf<String>()
            keys.forEach { key ->
                if (input[key] !is JsonArray) {
                    if (input[key] is JsonObject) {
                        loopThroughJson(input[key])
                    } else {
                        if (key == "photo" || key == "sign") {
                            base64.add(key)
                        }
                    }
                } else {
                    loopThroughJson(input[key])
                }
            }
            base64.forEach {
                input.remove(it)
                input.addProperty(it, "")
            }
        }
        if (input is JsonArray) {
            input.forEach {
                loopThroughJson(it)
            }
        }
    }
}