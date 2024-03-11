package ua.gov.diia.analytics

interface DiiaAnalytics {

    fun setUserId(userId: String)

    fun setPushToken(pushToken: String)

    fun networkRequest(action: String)

    fun networkResponse(action: String, success: Boolean, reasonFail: String? = null)

    fun initLoginByBankApp(selectedOption: String)

    fun resultLoginByBankApp(selectedOption: String, success: Boolean, reasonFail: String? = null)

    fun initLoginByBankId(bankId: String)

    fun resultLoginByBankId(bankId: String, success: Boolean, reasonFail: String? = null)

    fun initLoginByIdCard()

    fun resultLoginByIdCard(success: Boolean, reasonFail: String? = null)

    fun refreshToken(mobileUid: String, pushToken: String)

    fun notificationReceived(messageBody: String)

    fun nfcReadingInit(mobileUid: String, action: String)

    fun nfcReadingResult(
        mobileUid: String,
        action: String,
        success: Boolean,
        reasonFail: String? = null
    )

    fun faceRecognitionInit()

    fun faceRecognitionResult(success: Boolean, reasonFail: String? = null)

    fun pushReceived(notificationId: String)

    fun pushShown(notificationId: String)

    companion object {

        const val PUSH_TOKEN_PROPERTY = "PUSH_TOKEN"

        const val NETWORK_INIT_API_CALL = "NETWORK_INIT_API_CALL"
        const val NETWORK_RESULT_API_CALL = "NETWORK_RESULT_API_CALL"

        const val ACTION = "ACTION"
        const val RESULT = "RESULT"
        const val EXTRA_DATA = "EXTRA_DATA"

        const val RESULT_SUCCESS = "success"
        const val RESULT_FAIL = "fail"

        const val SELECTED_OPTION = "SELECTED_OPTION"
        const val INIT_LOGIN_BY_BANK_APP = "INIT_LOGIN_BY_BANK_APP"
        const val RESULT_LOGIN_BY_BANK_APP = "RESULT_LOGIN_BY_BANK_APP"
        const val BANK_ID = "BANK_ID"
        const val INIT_LOGIN_BY_BANK_ID = "INIT_LOGIN_BY_BANK_ID"
        const val RESULT_LOGIN_BY_BANK_ID = "RESULT_LOGIN_BY_BANK_ID"
        const val INIT_LOGIN_BY_ID_CARD = "INIT_LOGIN_BY_ID_CARD"
        const val RESULT_LOGIN_BY_ID_CARD = "RESULT_LOGIN_BY_ID_CARD"

        const val NFC_READING_INIT = "NFC_READING_INIT"
        const val NFC_READING_RESULT = "NFC_READING_RESULT"

        const val NOTIFICATION_RECEIVED = "NOTIFICATION_RECEIVED"
        const val MESSAGE_DATA = "MESSAGE_DATA"
        const val PUSH_TOKEN_RECEIVED = "PUSH_TOKEN_RECEIVED"
        const val UUID = "UUID"
        const val TOKEN = "TOKEN"
        const val STATE = "STATE"

        const val FACE_RECO_INIT = "FACE_RECO_INIT"
        const val FACE_RECO_RESULT = "FACE_RECO_RESULT"

        const val PUSH_NOTIFICATION_RECEIVED  = "NOTIFICATION_RECEIVED"
        const val PUSH_NOTIFICATION_SHOWN = "NOTIFICATION_SHOWN"
        const val PUSH_NOTIFICATION_ID = "NOTIFICATION_ID"

    }
}