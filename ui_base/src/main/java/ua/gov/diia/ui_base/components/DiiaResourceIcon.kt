package ua.gov.diia.ui_base.components

import ua.gov.diia.ui_base.R

enum class DiiaResourceIcon(
    val code: String,
    val drawableResourceId: Int,
    val contentDescriptionResourceId: Int
) {
    UA("ua", R.drawable.ic_doc_translate_ua, R.string.context_menu),
    EN("en", R.drawable.ic_translate_eng, R.string.context_menu),
    MENU("menu", R.drawable.diia_ellipse_menu, R.string.context_menu),
    BACK("back", R.drawable.diia_back_arrow, R.string.not_handled_icon),
    ELLIPSE_ARROW_RIGHT(
        "ellipseArrowRight",
        R.drawable.ellipse_arrow_right,
        R.string.not_handled_icon
    ),
    ELLIPSE_CHECK("ellipseCheck", R.drawable.ellipse_check, R.string.not_handled_icon),
    ELLIPSE_WHITE_ARROW_RIGHT(
        "ellipseWhiteArrowRight",
        R.drawable.ellipse_white_arrow_right,
        R.string.not_handled_icon
    ),
    PN("pn", R.drawable.pn, R.string.not_handled_icon),
    SHELTER("shelter", R.drawable.shelter, R.string.not_handled_icon),
    SAFETY("safety", R.drawable.safety, R.string.not_handled_icon),
    SAFETY_LARGE("safetyLarge", R.drawable.safety_large, R.string.not_handled_icon),
    DOUBLE_ICON("doubleIcon", R.drawable.double_icon, R.string.not_handled_icon),
    DEFAULT("default-stub", R.drawable.diia_check, R.string.not_handled_icon),
    ELLIPSE_KEBAB("ellipseKebab", R.drawable.diia_ellipse_menu_black, R.string.not_handled_icon),
    QR_SCAN("qrScan", R.drawable.qr_scan, R.string.not_handled_icon),
    QR("qr", R.drawable.ic_doc_verification, R.string.not_handled_icon),
    QR_WHITE("qrWhite", R.drawable.ic_qr_white, R.string.not_handled_icon),
    BARCODE("barcode", R.drawable.ic_barcode, R.string.not_handled_icon),
    BARCODE_WHITE("barcodeWhite", R.drawable.ic_barcode_white, R.string.not_handled_icon),
    QR_SCAN_WHITE("qrScanWhite", R.drawable.qr_scan_white, R.string.not_handled_icon),
    TRIDENT("trident", R.drawable.trident, R.string.not_handled_icon),
    TRIDENT_WHITE("tridentWhite", R.drawable.trident_white, R.string.not_handled_icon),
    POLICE("police", R.drawable.ic_police, R.string.not_handled_icon),
    TARGET("target", R.drawable.target, R.string.not_handled_icon),
    TARGET_WHITE("targetWhite", R.drawable.target_white, R.string.not_handled_icon),
    STACK("stack", R.drawable.diia_ic_doc_stack, R.string.not_handled_icon),
    NOTIFICATION_NEW("notificationNew", R.drawable.notification_new, R.string.not_handled_icon),
    ELLIPSE_KEBAB_WHITE(
        "ellipseKebabWhite",
        R.drawable.ic_ellipse_white,
        R.string.not_handled_icon
    ),
    ELLIPSE_SETTINGS(
        "ellipseSettings",
        R.drawable.ic_notifications_top_bar_right,
        R.string.not_handled_icon
    ),
    ELLIPSE_CROSS("ellipseCross", R.drawable.ic_ellipse_cross, R.string.close),
    ELLIPSE_ATTENTION(
        "ellipseAttention",
        R.drawable.ic_ellipse_attention,
        R.string.not_handled_icon
    ),
    STACK_WHITE("stackWhite", R.drawable.ic_stack_white, R.string.not_handled_icon),
    SHARE("share", R.drawable.ic_share_rounded, R.string.not_handled_icon),
    HEATING("heating", R.drawable.diia_heating, R.string.not_handled_icon),
    GENERATOR("generator", R.drawable.diia_generator, R.string.not_handled_icon),
    CHARGING("charging", R.drawable.diia_charging, R.string.not_handled_icon),
    WATER("water", R.drawable.diia_water, R.string.not_handled_icon),
    INTERNET_ACCESS("internetAccess", R.drawable.diia_internet, R.string.not_handled_icon),
    CELLULAR_CONNECTION("cellularConnection", R.drawable.diia_cellular, R.string.not_handled_icon),
    MEDICINE("medicine", R.drawable.diia_medicine, R.string.not_handled_icon),
    AID_KIT("aidKit", R.drawable.ic_aid_kit, R.string.not_handled_icon),
    EDIT("edit", R.drawable.ic_doc_edit_adress, R.string.not_handled_icon),
    CANCEL("cancel", R.drawable.ic_doc_cancel, R.string.not_handled_icon),
    BAG("bag", R.drawable.ic_bag, R.string.not_handled_icon),
    DELIVERY("delivery", R.drawable.ic_delivery, R.string.not_handled_icon),
    DOC_INFO("docInfo", R.drawable.ic_doc_info, R.string.not_handled_icon),
    HISTORY("history", R.drawable.ic_history, R.string.not_handled_icon),
    HOME_DOC("homedoc", R.drawable.ic_homedoc, R.string.not_handled_icon),
    HOME("home", R.drawable.ic_home, R.string.not_handled_icon),
    RATING("rating", R.drawable.ic_rating, R.string.not_handled_icon),
    SEARCH("search", R.drawable.ic_search_black, R.string.not_handled_icon),
    WALLET("wallet", R.drawable.ic_wallet, R.string.not_handled_icon),
    INFO("info", R.drawable.ic_info_about, R.string.not_handled_icon),
    DRAG("drag", R.drawable.drag, R.string.not_handled_icon),
    MESSAGE("message", R.drawable.ic_message, R.string.not_handled_icon),
    NEW_MESSAGE("newMessage", R.drawable.new_message, R.string.not_handled_icon),

    //TODO change name for NOTIFICATION_MESSAGE
    NOTIFICATION_MESSAGE(
        "notificationMessage",
        R.drawable.ic_menu_notifications_action,
        R.string.not_handled_icon
    ),
    KEY("key", R.drawable.ic_key, R.string.not_handled_icon),
    SETTINGS("settings", R.drawable.ic_settings, R.string.not_handled_icon),
    REFRESH("refresh", R.drawable.ic_doc_refresh, R.string.not_handled_icon),
    REORDER("reorder", R.drawable.ic_doc_reorder, R.string.not_handled_icon),
    DEVICE("device", R.drawable.ic_device, R.string.not_handled_icon),
    COPY("copy", R.drawable.ic_copy_settings, R.string.not_handled_icon),
    COPY_WHITE("copy_white", R.drawable.ic_copy_settings_white, R.string.not_handled_icon),
    FAQ("faq", R.drawable.ic_faq, R.string.not_handled_icon),
    ADD("add", R.drawable.ic_add, R.string.not_handled_icon),
    DOWNLOAD("download", R.drawable.ic_download, R.string.not_handled_icon),
    DELETE("delete", R.drawable.ic_doc_delete, R.string.not_handled_icon),
    UPDATE("update", R.drawable.ic_update, R.string.not_handled_icon),
    //TODO change name for SOME_DOCS
    SOME_DOCS("someDocs", R.drawable.ic_menu_history, R.string.not_handled_icon),
    OUT_LINK("outLink", R.drawable.ic_arrow_top, R.string.not_handled_icon),
    SYRINGE("syringe", R.drawable.ic_syringe, R.string.not_handled_icon),

    RETRY_WHITE("retry", R.drawable.ic_retry, R.string.not_handled_icon),

    USER_MALE("userMale", R.drawable.usermale, R.string.not_handled_icon),
    USER_FEMALE("userFemale", R.drawable.userfemale, R.string.not_handled_icon),
    ARROW_MIN_RIGHT("arrowMinRight", R.drawable.arrow_min_right, R.string.not_handled_icon),
    ARROW_MIN_LEFT("arrowMinLeft", R.drawable.arrow_min_left, R.string.not_handled_icon),
    MIC_ON("micOnWhite", R.drawable.ic_mic_on, R.string.not_handled_icon),
    MIC_OFF("micOffWhite", R.drawable.ic_mic_off, R.string.not_handled_icon),
    SPEAKER_ON("speakerOnWhite", R.drawable.ic_speaker_on, R.string.not_handled_icon),
    SPEAKER_OFF("speakerOffWhite", R.drawable.ic_speaker_off, R.string.not_handled_icon),
    HEADPHONES("headphonesWhite", R.drawable.ic_headphones, R.string.not_handled_icon),
    BLUETOOTH("bluetoothWhite", R.drawable.ic_bluetooth, R.string.not_handled_icon),
    CLOSE("closeWhite", R.drawable.ic_close, R.string.not_handled_icon),
    ;


    companion object {
        fun getResourceId(code: String): Int {
            return DiiaResourceIcon.values().firstOrNull { code == it.code }?.drawableResourceId
                ?: DEFAULT.drawableResourceId
        }

        fun getContentDescription(code: String): Int {
            return DiiaResourceIcon.values()
                .firstOrNull { code == it.code }?.contentDescriptionResourceId
                ?: DEFAULT.contentDescriptionResourceId
        }
    }

}


