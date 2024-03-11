package ua.gov.diia.opensource.util

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ua.gov.diia.core.models.rating_service.RatingFormModel
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.util.delegation.WithErrorHandling
import ua.gov.diia.core.util.delegation.WithRatingDialog
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.extensions.lifecycle.asLiveData
import javax.inject.Inject


class DefaultRatingDialogBehaviour @Inject constructor() : WithRatingDialog {

    private val _showRatingDialog = MutableLiveData<UiDataEvent<RatingFormModel>>()
    override val showRatingDialog = _showRatingDialog.asLiveData()

    private val _showRatingDialogByUserInitiative = MutableLiveData<UiDataEvent<RatingFormModel>>()
    override val showRatingDialogByUserInitiative = _showRatingDialogByUserInitiative.asLiveData()

    private val _sendingRatingResult = MutableLiveData<Boolean>()
    override val sendingRatingResult = _sendingRatingResult.asLiveData()

    override fun showRatingDialog(
        ratingDialog: RatingFormModel,
        key: String
    ) {
        _showRatingDialog.postValue(UiDataEvent(ratingDialog))
    }

    override fun <T> T.sendRating(
        ratingRequest: RatingRequest,
        category: String,
        serviceCode: String,
    ) where T : ViewModel, T : WithErrorHandling, T : WithRetryLastAction {
        // Implement your logic if required
    }

    override fun <T> T.getRating(
        category: String,
        serviceCode: String
    ) where T : ViewModel, T : WithErrorHandling, T : WithRetryLastAction {
        // Implement your logic if required
    }
}