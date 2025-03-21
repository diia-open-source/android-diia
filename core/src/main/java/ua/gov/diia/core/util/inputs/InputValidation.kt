package ua.gov.diia.core.util.inputs

fun isPersonNameValid(name: String): Boolean {
    val nameRegex = Regex("^[А-ЩЬЮЯЄІЇҐа-щьюяєіїґ'\\-–ʼ]*$")
    return nameRegex.matches(name)
}

fun isCountryOrCityNameValid(name: String): Boolean {
    val nameRegex = Regex("^[А-ЩЬЮЯЄІЇҐа-щьюяєіїґ'\"“”\\-–ʼ\\s]*$")
    return nameRegex.matches(name)
}