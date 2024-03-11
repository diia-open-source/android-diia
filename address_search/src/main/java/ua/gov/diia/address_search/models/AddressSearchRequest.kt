package ua.gov.diia.address_search.models

data class AddressSearchRequest(
    val resultCode: String,
    val searchType: SearchType,
    val items: Array<AddressItem>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AddressSearchRequest

        if (resultCode != other.resultCode) return false
        if (searchType != other.searchType) return false
        if (!items.contentEquals(other.items)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = resultCode.hashCode()
        result = 31 * result + searchType.hashCode()
        result = 31 * result + items.contentHashCode()
        return result
    }


}
