package ua.gov.diia.core.models.common

import ua.gov.diia.core.models.ConsumableItem
import ua.gov.diia.core.models.ConsumableString

interface BackStackEvent {

    data object Empty : BackStackEvent

    data class UserActionResult(val data: ConsumableString) : BackStackEvent

    data class TemplateRetryResult(val data: ConsumableString) : BackStackEvent

    data class RatingResult(val data: ConsumableItem) : BackStackEvent
}
