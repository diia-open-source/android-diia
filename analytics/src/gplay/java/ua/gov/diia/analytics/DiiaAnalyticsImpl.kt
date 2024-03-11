package ua.gov.diia.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import ua.gov.diia.analytics.DiiaAnalytics.Companion.ACTION
import ua.gov.diia.analytics.DiiaAnalytics.Companion.BANK_ID
import ua.gov.diia.analytics.DiiaAnalytics.Companion.EXTRA_DATA
import ua.gov.diia.analytics.DiiaAnalytics.Companion.FACE_RECO_INIT
import ua.gov.diia.analytics.DiiaAnalytics.Companion.FACE_RECO_RESULT
import ua.gov.diia.analytics.DiiaAnalytics.Companion.INIT_LOGIN_BY_BANK_APP
import ua.gov.diia.analytics.DiiaAnalytics.Companion.INIT_LOGIN_BY_BANK_ID
import ua.gov.diia.analytics.DiiaAnalytics.Companion.INIT_LOGIN_BY_ID_CARD
import ua.gov.diia.analytics.DiiaAnalytics.Companion.MESSAGE_DATA
import ua.gov.diia.analytics.DiiaAnalytics.Companion.NETWORK_INIT_API_CALL
import ua.gov.diia.analytics.DiiaAnalytics.Companion.NETWORK_RESULT_API_CALL
import ua.gov.diia.analytics.DiiaAnalytics.Companion.NFC_READING_INIT
import ua.gov.diia.analytics.DiiaAnalytics.Companion.NFC_READING_RESULT
import ua.gov.diia.analytics.DiiaAnalytics.Companion.NOTIFICATION_RECEIVED
import ua.gov.diia.analytics.DiiaAnalytics.Companion.PUSH_NOTIFICATION_ID
import ua.gov.diia.analytics.DiiaAnalytics.Companion.PUSH_NOTIFICATION_RECEIVED
import ua.gov.diia.analytics.DiiaAnalytics.Companion.PUSH_NOTIFICATION_SHOWN
import ua.gov.diia.analytics.DiiaAnalytics.Companion.PUSH_TOKEN_PROPERTY
import ua.gov.diia.analytics.DiiaAnalytics.Companion.PUSH_TOKEN_RECEIVED
import ua.gov.diia.analytics.DiiaAnalytics.Companion.RESULT
import ua.gov.diia.analytics.DiiaAnalytics.Companion.RESULT_FAIL
import ua.gov.diia.analytics.DiiaAnalytics.Companion.RESULT_LOGIN_BY_BANK_APP
import ua.gov.diia.analytics.DiiaAnalytics.Companion.RESULT_LOGIN_BY_BANK_ID
import ua.gov.diia.analytics.DiiaAnalytics.Companion.RESULT_LOGIN_BY_ID_CARD
import ua.gov.diia.analytics.DiiaAnalytics.Companion.RESULT_SUCCESS
import ua.gov.diia.analytics.DiiaAnalytics.Companion.SELECTED_OPTION
import ua.gov.diia.analytics.DiiaAnalytics.Companion.STATE
import ua.gov.diia.analytics.DiiaAnalytics.Companion.TOKEN
import ua.gov.diia.analytics.DiiaAnalytics.Companion.UUID

internal class DiiaAnalyticsImpl(context: Context) : DiiaAnalytics {

    private val firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    override fun setUserId(userId: String) {
        firebaseAnalytics.setUserId(userId)
    }

    override fun setPushToken(pushToken: String) {
        firebaseAnalytics.setUserProperty(PUSH_TOKEN_PROPERTY, pushToken)
    }

    override fun networkRequest(action: String) {
        val bundle = newBundle()
        bundle.putString(ACTION, action)
        firebaseAnalytics.logEvent(NETWORK_INIT_API_CALL, bundle)
    }

    override fun networkResponse(action: String, success: Boolean, reasonFail: String?) {
        val bundle = newResultBundle(success, reasonFail)
        bundle.putString(ACTION, action)
        firebaseAnalytics.logEvent(NETWORK_RESULT_API_CALL, bundle)
    }

    override fun initLoginByBankApp(selectedOption: String) {
        val bundle = newBundle()
        bundle.putString(SELECTED_OPTION, selectedOption)
        firebaseAnalytics.logEvent(INIT_LOGIN_BY_BANK_APP, bundle)
    }

    override fun resultLoginByBankApp(
        selectedOption: String,
        success: Boolean,
        reasonFail: String?
    ) {
        val bundle = newResultBundle(success, reasonFail)
        bundle.putString(SELECTED_OPTION, selectedOption)
        firebaseAnalytics.logEvent(RESULT_LOGIN_BY_BANK_APP, bundle)
    }

    override fun initLoginByBankId(bankId: String) {
        val bundle = newBundle()
        bundle.putString(BANK_ID, bankId)
        firebaseAnalytics.logEvent(INIT_LOGIN_BY_BANK_ID, bundle)
    }

    override fun resultLoginByBankId(bankId: String, success: Boolean, reasonFail: String?) {
        val bundle = newResultBundle(success, reasonFail)
        bundle.putString(BANK_ID, bankId)
        firebaseAnalytics.logEvent(RESULT_LOGIN_BY_BANK_ID, bundle)
    }

    override fun initLoginByIdCard() {
        val bundle = newBundle()
        firebaseAnalytics.logEvent(INIT_LOGIN_BY_ID_CARD, bundle)
    }

    override fun resultLoginByIdCard(success: Boolean, reasonFail: String?) {
        val bundle = newResultBundle(success, reasonFail)
        firebaseAnalytics.logEvent(RESULT_LOGIN_BY_ID_CARD, bundle)
    }

    override fun refreshToken(mobileUid: String, pushToken: String) {
        val bundle = newBundle()
        bundle.putString(UUID, mobileUid)
        bundle.putString(TOKEN, pushToken)
        firebaseAnalytics.logEvent(PUSH_TOKEN_RECEIVED, bundle)
    }

    override fun notificationReceived(messageBody: String) {
        val bundle = newBundle()
        bundle.putString(MESSAGE_DATA, messageBody)
        firebaseAnalytics.logEvent(NOTIFICATION_RECEIVED, bundle)
    }

    override fun nfcReadingInit(mobileUid: String, action: String) {
        firebaseAnalytics.logEvent(
            NFC_READING_INIT,
            newBundle().apply {
                putString(UUID, mobileUid)
                putString(STATE, action)
            }
        )
    }

    override fun nfcReadingResult(
        mobileUid: String,
        action: String,
        success: Boolean,
        reasonFail: String?
    ) {
        firebaseAnalytics.logEvent(
            NFC_READING_RESULT,
            newResultBundle(success, reasonFail).apply {
                putString(UUID, mobileUid)
                putString(STATE, action)
            }
        )
    }

    override fun faceRecognitionInit() {
        firebaseAnalytics.logEvent(FACE_RECO_INIT, newBundle())
    }

    override fun faceRecognitionResult(success: Boolean, reasonFail: String?) {
        firebaseAnalytics.logEvent(FACE_RECO_RESULT, newResultBundle(success, reasonFail))
    }

    override fun pushReceived(notificationId: String) {
        val bundle = newBundle()
        bundle.putString(PUSH_NOTIFICATION_ID, notificationId)
        firebaseAnalytics.logEvent(PUSH_NOTIFICATION_RECEIVED, bundle)
    }

    override fun pushShown(notificationId: String) {
        val bundle = newBundle()
        bundle.putString(PUSH_NOTIFICATION_ID, notificationId)
        firebaseAnalytics.logEvent(PUSH_NOTIFICATION_SHOWN, bundle)
    }

    private fun newBundle(): Bundle {
        return Bundle()
    }

    private fun newResultBundle(success: Boolean, reasonFail: String?): Bundle {
        val bundle = newBundle()
        bundle.putString(RESULT, if (success) RESULT_SUCCESS else RESULT_FAIL)
        reasonFail?.let {
            bundle.putString(EXTRA_DATA, reasonFail)
        }
        return bundle
    }
}
