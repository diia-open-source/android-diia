package ua.gov.diia.verification.ui.methods

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.verification.di.ProviderVerifiedClient
import ua.gov.diia.verification.network.ApiVerification
import javax.inject.Inject

abstract class VerificationMethod {

    /**
     * Name of verification method. Should be the same as provided from api
     */
    abstract val name: String

    /**
     * Whether this method is available to use or not. For instance, this property may check
     * if corresponding application installed on device or hardware feature is present
     */
    abstract val isAvailable: Boolean

    /**
     * Return if this method can be used for authorization
     */
    open val isAvailableForAuth: Boolean = true

    /**
     * Resource id of an icon that shown in method selection list
     */
    @get:DrawableRes
    abstract val iconResId: Int

    /**
     * Resource id of a title that shown in method selection list
     */
    @get:StringRes
    abstract val titleResId: Int

    /**
     * Resource id of description that shown in method selection list
     */
    @get:StringRes
    abstract val descriptionResId: Int

    @ProviderVerifiedClient
    @Inject
    lateinit var api: ApiVerification
        internal set

    /**
     * Usually obtain verification request uses [api]
     */
    abstract suspend fun getVerificationRequest(
        verificationSchema: String,
        processId: String,
    ): VerificationRequest

    /**
     * Returns dialog model that will be use in case of only this method is present but not available
     */
    open fun getUnavailabilityDialog(): TemplateDialogModel? = null

    /**
     * Extract the request id from url
     */
    protected fun String.getRequestId() = substringAfterLast('/').takeUnless { it.isEmpty() }
}