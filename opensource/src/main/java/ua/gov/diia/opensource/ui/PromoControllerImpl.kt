package ua.gov.diia.opensource.ui

import ua.gov.diia.core.controller.PromoController
import ua.gov.diia.core.models.dialogs.TemplateDialogModelWithProcessCode
import ua.gov.diia.diia_storage.DiiaStorage
import ua.gov.diia.diia_storage.store.Preferences
import javax.inject.Inject

class PromoControllerImpl @Inject constructor(
    private val keyValueStore: DiiaStorage,
) : PromoController {
    override suspend fun checkPromo(callback: (template: TemplateDialogModelWithProcessCode) -> Unit) {
    }

    override suspend fun subscribeToBetaByCode(value: Int?) {
    }

    override suspend fun updatePromoProcessCode(value: Int) {
        keyValueStore.set(Preferences.PromoProcessCode, value)
    }
}