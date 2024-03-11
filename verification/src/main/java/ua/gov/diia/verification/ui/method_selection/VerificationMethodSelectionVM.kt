package ua.gov.diia.verification.ui.method_selection

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.extensions.lifecycle.asLiveData
import ua.gov.diia.verification.R
import ua.gov.diia.verification.model.VerificationMethodView
import ua.gov.diia.verification.model.VerificationMethodsView
import ua.gov.diia.verification.ui.VerificationSchema
import javax.inject.Inject

@HiltViewModel
internal class VerificationMethodSelectionVM @Inject constructor() : ViewModel() {

    private val _header = MutableLiveData<String?>()
    val header = _header.asLiveData()

    private val _themeColor = MutableLiveData<Int>()
    val themeColor = _themeColor.asLiveData()

    private val _methods = MutableLiveData<UiDataEvent<List<VerificationMethodView>>>()
    val methods = _methods.asLiveData()

    private val _sendResult = MutableLiveData<UiDataEvent<String>>()
    val sendResult = _sendResult.asLiveData()

    val doOnMethodSelected = { code: String -> _sendResult.value = UiDataEvent(code) }

    fun doInit(data: VerificationMethodsView) {
        _header.value = data.title
        adjustThemeColor(data.schema)
        _methods.value = UiDataEvent(data.methods.reversed())
    }

    private fun adjustThemeColor(schema: String) {
        _themeColor.value = when (schema) {
            VerificationSchema.AUTHORIZATION -> R.color.green_light
            else -> R.color.colorPrimary
        }
    }
}