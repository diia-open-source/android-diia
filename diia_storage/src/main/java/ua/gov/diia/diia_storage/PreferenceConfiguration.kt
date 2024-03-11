package ua.gov.diia.diia_storage

data class PreferenceConfiguration(
    private val _preferenceName: String,
    val allowedScopes: Set<String>,
    val preferenceNamePrefix: String = "",
) {
    val preferenceName: String
        get() = preferenceNamePrefix + _preferenceName
}
