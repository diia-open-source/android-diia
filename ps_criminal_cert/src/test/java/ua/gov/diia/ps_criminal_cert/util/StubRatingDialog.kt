package ua.gov.diia.ps_criminal_cert.util

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ua.gov.diia.core.models.rating_service.RatingFormModel
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.util.delegation.WithErrorHandling
import ua.gov.diia.core.util.delegation.WithRatingDialog
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.event.UiDataEvent

class StubRatingDialog : WithRatingDialog {

    override val showRatingDialog = MutableLiveData<UiDataEvent<RatingFormModel>>()
    override val showRatingDialogByUserInitiative = MutableLiveData<UiDataEvent<RatingFormModel>>()

    override val sendingRatingResult = MutableLiveData<Boolean>()

    override fun showRatingDialog(ratingDialog: RatingFormModel, key: String) {
        showRatingDialog.postValue(UiDataEvent(ratingDialog))
    }

    override fun <T : ViewModel> T.sendRating(
        ratingRequest: RatingRequest,
        category: String,
        serviceCode: String
    ) where T : WithErrorHandling, T : WithRetryLastAction = Unit

    override fun <T : ViewModel> T.getRating(
        category: String,
        serviceCode: String
    ) where T : WithErrorHandling, T : WithRetryLastAction = Unit
}
