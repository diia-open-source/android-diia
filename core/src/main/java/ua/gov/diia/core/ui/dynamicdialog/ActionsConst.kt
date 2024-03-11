package ua.gov.diia.core.ui.dynamicdialog

object ActionsConst {

    //diia custom actions
    const val DIALOG_ACTION_CODE_CLOSE = "ua.gov.diia.app.close"
    const val GENERAL_RETRY = "ua.gov.diia.app.retry"
    const val ERROR_DIALOG_DEAL_WITH_IT = "ua.gov.diia.app.deal_with_it"
    const val DIALOG_ACTION_ENABLE_CLICKS = "ua.gov.diia.app.enable_clicks"


    const val ACTION_PAYMENT_COMPLETED = "payment_completed"
    const val ACTION_PAYMENT_POSTCONDITION = "payment_postcondition"
    const val ACTION_NAVIGATE_BACK = "back"
    const val ACTION_CRIMINAL_CERT = "criminalRecordCertificate"
    const val ACTION_SIGNATURE_GENERATED = "signature_generated"
    const val ACTION_SIGNATURE_PASS_APPROVED = "signature_pass_approved"
    const val DIALOG_ACTION_CODE_SKIP = "skip"
    const val DIALOG_ACTION_CODE_PROLONG = "authMethods"
    const val DIALOG_DEAL_WITH_IT = "ok"
    const val DIALOG_ACTION_REFRESH = "refresh"
    const val DIALOG_ACTION_EXIT = "exit"
    const val DIALOG_ACTION_CONFIRM = "confirm"
    const val DIALOG_ACTION_EXIT_CONFIRM = "exitConfirm"
    const val DIALOG_ACTION_CLOSE = "close"
    const val DIALOG_ACTION_PAY = "toPay"
    const val DIALOG_ACTION_SHARE = "share"
    const val DIALOG_ACTION_SIGN_AGAIN = "signing"
    const val DIALOG_ACTION_SHARING = "sharing"
    const val DIALOG_ACTION_CANCEL = "cancel"
    const val DIALOG_ACTION_CODE_LOGOUT = "logout"
    const val DIALOG_ACTION_CODE_REPEAT = "repeat"
    const val DIALOG_ACTION_REMOVE_SIGNATURE = "removeSignature"
    const val DIALOG_ACTION_RESUME = "resume"
    const val RESULT_KEY_DOWNLOAD_PDF = "RESULT_KEY_DOWNLOAD_PDF"
    const val DIALOG_ACTION_GET_MILITARY_ID = "getMilitaryId"
    const val DIALOG_ACTION_CODE_DELETE = "delete"
    const val DIALOG_ACTION_RESIDENCE_CERT_ORDER = "residenceCert"
    const val DIALOG_ACTION_RESIDENCE_CERT_STATUS = "residenceCertStatus"
    const val DIALOG_ACTION_RESIDENCE_CERT_ORDER_CHILD = "residenceCertChildren"
    const val DIALOG_ACTION_RESIDENCE_CERT_STATUS_CHILD = "residenceCertChildrenStatus"
    const val DIALOG_ACTION_CRIMINAL_RECORD_CERTIFICATE = "criminalRecordCertificate"
    const val DIALOG_ACTION_PUBLIC_SERVICES = "publicServices"
    const val DIALOG_ACTION_PREV_SCREEN = "previousScreen"
    const val DIALOG_ACTION_OPEN_EXTERNAL_LINK = "externalLink"

    const val DIALOG_ACTION_CANCEL_APPLICATION = "cancelApplication"
    const val DIALOG_ACTION_CHANGE_SELECTION = "changeSelection"
    const val DIALOG_ACTION_BACK = "back"
    const val DIALOG_ACTION_OPEN_BIRTH_CERT = "birthCertificate"


    //proper user
    const val DIALOG_ACTION_PROPER_USER_CANCEL_APPLICATION = "cancelApplication"
    const val DIALOG_ACTION_PROPER_USER_APPLICATION_CANCELED = "properUserCanceling"
    const val DIALOG_ACTION_PROPER_USER_APPLICATION_SIGNED = "properUser"

    const val KEY_GLOBAL_PROCESSING =
        "ua.gov.diia.app.ui.fragments.dialogs.dynamicdialog.action_global_processing"

    //Template dialog result actions
    const val FRAGMENT_USER_ACTION_RESULT_KEY = "fragment_template_action_result"
    const val DIALOG_USER_ACTION_RESULT_KEY = "dialog_template_action_result"

    //system dialog actions
    const val SYSTEM_DIALOG_POSITIVE = "SYSTEM_DIALOG_POSITIVE"
    const val SYSTEM_DIALOG_SINGLE_POSITIVE = "SYSTEM_DIALOG_SINGLE_POSITIVE"
    const val SYSTEM_DIALOG_NEGATIVE = "SYSTEM_DIALOG_NEGATIVE"

    //action item selection
    const val ACTION_ITEM_SELECTED = "SELECTED_ACTION_ITEM"

    //context menu
    const val FAQ_CATEGORY = "faqCategory"
    const val SUPPORT_SERVICE = "supportServiceScreen"
    const val CREATE_CONTENT = "createContent"
    const val TIPS = "tips"
    const val COMMUNITY_CONTACTS = "communityContacts"
    const val FUND_DETAILS = "fundDetails"
    const val RATING = "rating"
    const val TYPE_USER_INITIATIVE = "userInitiative"
    const val RATING_TYPE_REQUESTED = "byRequest"
    const val DOCUMENTS_CODE = "document"

    //nav const
    const val RESULT_KEY_NAVIGATION = "RESULT_KEY_NAVIGATION"
    const val RESULT_KEY_NAV_TO_ADD_BIRTH_CERT = "RESULT_KEY_NAV_TO_ADD_BIRTH_CERT"
    const val RESULT_KEY_NAV_TO_ADD_COVID_CERT = "RESULT_KEY_NAV_TO_ADD_COVID_CERT"
    const val RESULT_KEY_NAV_TO_ADD_CHILD_COVID_CERT = "RESULT_KEY_NAV_TO_ADD_CHILD_COVID_CERT"
    const val RESULT_KEY_NAV_TO_ADD_PROPER_USER = "RESULT_KEY_NAV_TO_ADD_PROPER_USER"
    const val RESULT_KEY_NAV_TO_RESIDENCE_PERMIT_PERMANENT = "RESULT_KEY_NAV_TO_RESIDENCE_PERMIT_PERMANENT"
    const val RESULT_KEY_NAV_TO_RESIDENCE_PERMIT_TEMPORARY = "RESULT_KEY_NAV_TO_RESIDENCE_PERMIT_TEMPORARY"
    const val RESULT_KEY_NAV_TO_MILITARY_BOND = "RESULT_KEY_NAV_TO_MILITARY_BOND"
    const val RESULT_KEY_NAV_TO_VEHICLE_RE_REGISTRATION = "RESULT_KEY_NAV_TO_VEHICLE_RE_REGISTRATION"
    const val RESULT_KEY_REMOVE_MILITARY_BOND = "RESULT_KEY_REMOVE_MILITARY_BOND"
    const val RESULT_KEY_NAV_TO_HOUSING_CERTIFICATES = "RESULT_KEY_NAV_TO_HOUSING_CERTIFICATES"
    const val RESULT_KEY_NAV_TO_HC_FOUNDING_REQUEST = "RESULT_KEY_NAV_TO_HC_FOUNDING_REQUEST"


    const val RESULT_KEY_OPEN_LINK = "RESULT_KEY_OPEN_LINK"
    const val RESULT_KEY_NAV_TO_IDP_EDIT_ADDRESS = "RESULT_KEY_NAV_TO_IDP_EDIT_ADDRESS"
    const val RESULT_KEY_NAV_TO_IDP_CERT_CANCEL = "RESULT_KEY_NAV_TO_IDP_CERT_CANCEL"
    const val RESULT_KEY_RATING_SERVICE = "rating"
    const val RESULT_KEY_RATE_DOCUMENT = "rate_document"
    const val RESULT_KEY_REMOVE_DOCUMENT = "remove_document"
    const val RESULT_KEY_VERIFICATION_CODE = "verification_code"
    const val RESULT_KEY_QR_CODE = "verification_code_qr"
    const val RESULT_KEY_EAN13_CODE = "verification_code_ean13"

    const val RESULT_KEY_UPDATE_DOCUMENT = "update_document"

    const val RESULT_KEY_NAV_TO_PACKAGE_STATUS_REFRESH = "refresh"

    const val DEEP_LINK_ACTION = "deep_link_action"

}
