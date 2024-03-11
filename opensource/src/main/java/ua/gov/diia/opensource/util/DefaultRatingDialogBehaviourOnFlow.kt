package ua.gov.diia.opensource.util

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import ua.gov.diia.core.models.rating_service.RatingFormModel
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRatingDialogOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.event.UiDataEvent
import javax.inject.Inject

class DefaultRatingDialogBehaviourOnFlow @Inject constructor() : WithRatingDialogOnFlow {

    private val _showRatingDialog = MutableSharedFlow<UiDataEvent<RatingFormModel>>(replay = 0, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    override val showRatingDialog = _showRatingDialog.asSharedFlow()

    private val _showRatingDialogByUserInitiative = MutableSharedFlow<UiDataEvent<RatingFormModel>>(replay = 0, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    override val showRatingDialogByUserInitiative = _showRatingDialogByUserInitiative.asSharedFlow()

    private val _sendingRatingResult = MutableStateFlow(false)
    override val sendingRatingResult = _sendingRatingResult.asStateFlow()

    override fun showRatingDialog(
        ratingDialog: RatingFormModel,
        key: String
    ) {}

    override fun <T> T.sendRating(
        ratingRequest: RatingRequest,
        category: String,
        serviceCode: String
    ) where T : ViewModel, T : WithErrorHandlingOnFlow, T : WithRetryLastAction {}

    override fun <T> T.getRating(
        category: String,
        serviceCode: String
    ) where T : ViewModel, T : WithErrorHandlingOnFlow, T : WithRetryLastAction {}
}