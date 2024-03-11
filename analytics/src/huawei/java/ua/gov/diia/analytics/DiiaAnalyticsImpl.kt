package ua.gov.diia.analytics

import android.content.Context
import android.os.Bundle
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.analytics.HiAnalyticsInstance
import ua.gov.diia.analytics.DiiaAnalytics.Companion.ACTION
import ua.gov.diia.analytics.DiiaAnalytics.Companion.BANK_ID
import ua.gov.diia.analytics.DiiaAnalytics.Companion.EXTRA_DATA
import ua.gov.diia.analytics.DiiaAnalytics.Companion.INIT_LOGIN_BY_BANK_APP
import ua.gov.diia.analytics.DiiaAnalytics.Companion.INIT_LOGIN_BY_BANK_ID
import ua.gov.diia.analytics.DiiaAnalytics.Companion.INIT_LOGIN_BY_ID_CARD
import ua.gov.diia.analytics.DiiaAnalytics.Companion.MESSAGE_DATA
import ua.gov.diia.analytics.DiiaAnalytics.Companion.NETWORK_INIT_API_CALL
import ua.gov.diia.analytics.DiiaAnalytics.Companion.NETWORK_RESULT_API_CALL
import ua.gov.diia.analytics.DiiaAnalytics.Companion.NOTIFICATION_RECEIVED
import ua.gov.diia.analytics.DiiaAnalytics.Companion.PUSH_NOTIFICATION_RECEIVED
import ua.gov.diia.analytics.DiiaAnalytics.Companion.PUSH_TOKEN_PROPERTY
import ua.gov.diia.analytics.DiiaAnalytics.Companion.PUSH_TOKEN_RECEIVED
import ua.gov.diia.analytics.DiiaAnalytics.Companion.RESULT
import ua.gov.diia.analytics.DiiaAnalytics.Companion.RESULT_FAIL
import ua.gov.diia.analytics.DiiaAnalytics.Companion.RESULT_LOGIN_BY_BANK_APP
import ua.gov.diia.analytics.DiiaAnalytics.Companion.RESULT_LOGIN_BY_BANK_ID
import ua.gov.diia.analytics.DiiaAnalytics.Companion.RESULT_LOGIN_BY_ID_CARD
import ua.gov.diia.analytics.DiiaAnalytics.Companion.RESULT_SUCCESS
import ua.gov.diia.analytics.DiiaAnalytics.Companion.SELECTED_OPTION
import ua.gov.diia.analytics.DiiaAnalytics.Companion.TOKEN
import ua.gov.diia.analytics.DiiaAnalytics.Companion.UUID

internal class DiiaAnalyticsImpl(context: Context) : DiiaAnalytics {

    private val huaweiAnalytics: HiAnalyticsInstance = HiAnalytics.getInstance(context)

    override fun setUserId(userId: String) {
        huaweiAnalytics.setUserId(userId)
    }

    override fun setPushToken(pushToken: String) {
        huaweiAnalytics.setUserProfile(PUSH_TOKEN_PROPERTY, pushToken)
    }

    override fun networkRequest(action: String) {
        val bundle = newBundle()
        bundle.putString(ACTION, action)
        huaweiAnalytics.onEvent(NETWORK_INIT_API_CALL, bundle)
    }

    override fun networkResponse(action: String, success: Boolean, reasonFail: String?) {
        val bundle = newResultBundle(success, reasonFail)
        bundle.putString(ACTION, action)
        huaweiAnalytics.onEvent(NETWORK_RESULT_API_CALL, bundle)
    }

    override fun initLoginByBankApp(selectedOption: String) {
        val bundle = newBundle()
        bundle.putString(SELECTED_OPTION, selectedOption)
        huaweiAnalytics.onEvent(INIT_LOGIN_BY_BANK_APP, bundle)
    }

    override fun resultLoginByBankApp(
        selectedOption: String,
        success: Boolean,
        reasonFail: String?
    ) {
        val bundle = newResultBundle(success, reasonFail)
        bundle.putString(SELECTED_OPTION, selectedOption)
        huaweiAnalytics.onEvent(RESULT_LOGIN_BY_BANK_APP, bundle)
    }

    override fun initLoginByBankId(bankId: String) {
        val bundle = newBundle()
        bundle.putString(BANK_ID, bankId)
        huaweiAnalytics.onEvent(INIT_LOGIN_BY_BANK_ID, bundle)
    }

    override fun resultLoginByBankId(bankId: String, success: Boolean, reasonFail: String?) {
        val bundle = newResultBundle(success, reasonFail)
        bundle.putString(BANK_ID, bankId)
        huaweiAnalytics.onEvent(RESULT_LOGIN_BY_BANK_ID, bundle)
    }

    override fun initLoginByIdCard() {
        val bundle = newBundle()
        huaweiAnalytics.onEvent(INIT_LOGIN_BY_ID_CARD, bundle)
    }

    override fun resultLoginByIdCard(success: Boolean, reasonFail: String?) {
        val bundle = newResultBundle(success, reasonFail)
        huaweiAnalytics.onEvent(RESULT_LOGIN_BY_ID_CARD, bundle)
    }

    override fun refreshToken(mobileUid: String, pushToken: String) {
        val bundle = newBundle()
        bundle.putString(UUID, mobileUid)
        bundle.putString(TOKEN, pushToken)
        huaweiAnalytics.onEvent(PUSH_TOKEN_RECEIVED, bundle)
    }

    override fun notificationReceived(messageBody: String) {
        val bundle = newBundle()
        bundle.putString(MESSAGE_DATA, messageBody)
        huaweiAnalytics.onEvent(NOTIFICATION_RECEIVED, bundle)
    }

    override fun pushReceived(notificationId: String) {
        val bundle = newBundle()
        bundle.putString(DiiaAnalytics.PUSH_NOTIFICATION_ID, notificationId)
        huaweiAnalytics.onEvent(PUSH_NOTIFICATION_RECEIVED, bundle)
    }

    override fun pushShown(notificationId: String) {
        val bundle = newBundle()
        bundle.putString(DiiaAnalytics.PUSH_NOTIFICATION_ID, notificationId)
        huaweiAnalytics.onEvent(DiiaAnalytics.PUSH_NOTIFICATION_SHOWN, bundle)
    }

    private fun newBundle(): Bundle {
        return Bundle()
    }

    private fun newResultBundle(success: Boolean, reasonFail: String?): Bundle {
        val bundle = newBundle()
        bundle.putString(RESULT, if (success) RESULT_SUCCESS else RESULT_FAIL)
        reasonFail?.let { bundle.putString(EXTRA_DATA, it) }
        return bundle
    }

    override fun nfcReadingInit(mobileUid: String, action: String) {
    }

    override fun nfcReadingResult(
        mobileUid: String,
        action: String,
        success: Boolean,
        reasonFail: String?
    ) {
    }

    override fun faceRecognitionInit() {
    }

    override fun faceRecognitionResult(success: Boolean, reasonFail: String?) {
    }
}
