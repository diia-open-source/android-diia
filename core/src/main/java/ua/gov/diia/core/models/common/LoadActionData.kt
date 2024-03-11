package ua.gov.diia.core.models.common

abstract class LoadActionData {
    abstract val icon: String?
    abstract val name: String?
    abstract val isLoading: Boolean
    abstract val isEnabled: Boolean

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LoadActionData) return false

        if (icon != other.icon) return false
        if (name != other.name) return false
        if (isLoading != other.isLoading) return false
        if (isEnabled != other.isEnabled) return false

        return true
    }

    override fun hashCode(): Int {
        var result = icon?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + isLoading.hashCode()
        result = 31 * result + isEnabled.hashCode()
        return result
    }
}
