package ua.gov.diia.core.util.delegation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ua.gov.diia.core.models.rating_service.RatingFormModel
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.event.UiDataEvent

interface WithRatingDialog {

    val showRatingDialog: LiveData<UiDataEvent<RatingFormModel>>

    val showRatingDialogByUserInitiative: LiveData<UiDataEvent<RatingFormModel>>

    val sendingRatingResult: LiveData<Boolean>

    fun showRatingDialog(
        ratingDialog: RatingFormModel,
        key: String = ActionsConst.RESULT_KEY_RATING_SERVICE
    )

    fun <T> T.sendRating(
        ratingRequest: RatingRequest,
        category: String,
        serviceCode: String
    ) where T : ViewModel, T : WithErrorHandling, T : WithRetryLastAction

    fun <T> T.getRating(
        category: String,
        serviceCode: String,
        screenCode: String? = null
    ) where T : ViewModel, T : WithErrorHandling, T : WithRetryLastAction

}
