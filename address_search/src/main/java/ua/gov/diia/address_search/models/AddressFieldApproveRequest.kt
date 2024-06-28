package ua.gov.diia.address_search.models

import java.util.regex.Pattern

data class AddressFieldApproveRequest(
    val mandatory: Boolean,
    val data: Any?,
    val regex: String?,
    val flags: List<String>?
) {

    fun approved(): Boolean = when (data) {
        is String -> {
            val passedValidation = if (regex != null) {
                approveValidation(data)
            } else true

            if (mandatory) {
                data.isNotBlank() && passedValidation
            } else {
                passedValidation
            }
        }

        else -> if (mandatory) data != null else true
    }

    private val validationPattern: Pattern? by lazy {
        regex?.let { regex ->
            val flags = flags?.fold(0) { compiledFlags, flag ->
                when (flag) {
                    "i" -> compiledFlags or Pattern.CASE_INSENSITIVE
                    // Add other flag mappings as needed
                    else -> compiledFlags
                }
            } ?: 0
            Pattern.compile(
                regex, flags
            )
        }
    }

    private fun approveValidation(value: String?): Boolean =
        validationPattern?.matcher(value ?: "")?.matches() ?: true
}
