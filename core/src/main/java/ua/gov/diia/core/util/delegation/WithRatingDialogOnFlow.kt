package ua.gov.diia.core.util.delegation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.SharedFlow
import ua.gov.diia.core.models.rating_service.RatingFormModel
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.event.UiDataEvent

interface WithRatingDialogOnFlow {

    val showRatingDialog: SharedFlow<UiDataEvent<RatingFormModel>>

    val showRatingDialogByUserInitiative: SharedFlow<UiDataEvent<RatingFormModel>>

    val sendingRatingResult: SharedFlow<Boolean>

    fun showRatingDialog(
        ratingDialog: RatingFormModel,
        key: String = ActionsConst.RESULT_KEY_RATING_SERVICE
    )

    fun <T> T.sendRating(
        ratingRequest: RatingRequest,
        category: String,
        serviceCode: String
    ) where T : ViewModel, T : WithErrorHandlingOnFlow, T : WithRetryLastAction

    fun <T> T.getRating(
        category: String,
        serviceCode: String
    ) where T : ViewModel, T : WithErrorHandlingOnFlow, T : WithRetryLastAction

}
