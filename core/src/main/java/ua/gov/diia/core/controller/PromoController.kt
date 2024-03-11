package ua.gov.diia.core.controller


import ua.gov.diia.core.models.dialogs.TemplateDialogModelWithProcessCode

interface PromoController {
    /**
     * Validate current promo code with one from API
     */
    suspend fun checkPromo(callback: (template: TemplateDialogModelWithProcessCode) -> Unit)

    /**
     * Subscribe for promo news
     */
    suspend fun subscribeToBetaByCode(value: Int?)

    /**
     * Save process code to the storage
     */
    suspend fun updatePromoProcessCode(value: Int)
}