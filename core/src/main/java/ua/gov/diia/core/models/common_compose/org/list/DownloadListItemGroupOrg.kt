package ua.gov.diia.core.models.common_compose.org.list

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.general.Action

@JsonClass(generateAdapter = true)
data class DownloadListItemGroupOrg(
    @Json(name = "items")
    val items: List<Item>,
    @Json(name = "listHeader")
    val listHeader: String,
    @Json(name = "loadedListHeader")
    val loadedListHeader: String
) {
    @JsonClass(generateAdapter = true)
    data class Item(
        @Json(name = "id")
        val id: String,
        @Json(name = "updateDate")
        val updateDate: String? = null,
        @Json(name = "mapLink")
        val mapLink: String? = null,
        @Json(name = "iconLeft")
        val iconLeft: List<Icon>?,
        @Json(name = "iconRight")
        val iconRight: List<Icon>?,
        @Json(name = "label")
        val label: String,
        @Json(name = "actions")
        val actions: List<Action>?,
        @Json(name = "description")
        val description: List<Description>?,
        @Json(name = "state")
        val state: ActionTypes?,
    ) {
        @JsonClass(generateAdapter = true)
        data class Description(
            @Json(name = "text")
            val text: String,
            @Json(name = "type")
            val type: ProgressTypes
        )

        @JsonClass(generateAdapter = true)
        data class Icon(
            @Json(name = "action")
            val action: Action?,
            @Json(name = "iconCode")
            val iconCode: String,
            @Json(name = "state")
            val state: String,
            @Json(name = "type")
            val type: ProgressTypes
        )
    }
}

enum class ProgressTypes {
    notDownloaded, inProgress, failedDownloaded, downloaded, notAvailable, updateAvailable, failedUpdate;
}

enum class ActionTypes {
    enabled, disabled, invisible
}