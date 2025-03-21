package ua.gov.diia.core.util.type_enum

inline fun <reified T : Enum<T>> getEnumTypeValue(type: String): T? {
    val values = enumValues<T>()
    return values.firstOrNull {
        it is TypeEnum && (it as TypeEnum).type.equals(type, true)
    }
}