package ua.gov.diia.ui_base.fragments.errordialog

import androidx.annotation.Keep
import retrofit2.HttpException
import ua.gov.diia.core.network.Http.HTTP_400
import ua.gov.diia.core.network.Http.HTTP_403
import ua.gov.diia.core.network.Http.HTTP_404
import ua.gov.diia.core.network.Http.HTTP_422
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

enum class DialogErrorFlow {
    GENERAL, ADD_DOC, PAYMENT, ASSIST
}

@Keep
enum class DialogError {
    CHECK_TIMEOUT,
    REQUEST_NOT_REGISTERED,
    UNKNOWN_ERROR_RECOVERABLE,
    UNKNOWN_ERROR_UNRECOVERABLE,
    NO_INTERNET,
    PAYMENT_ERROR,
    PAYMENT_FORBIDDEN,
    DRIVER_LICENSE_REPLACE,
    CRITICAL_ERROR_DEEP_LINK_NOT_REGISTERED,
    REQUESTED_DOCUMENTS_NOT_FOUND,
    DOCUMENT_REQUEST_EXPIRED,
    SPECIFIED_DOCUMENT_NOT_FOUND,
    SERVICE_UNAVAILABLE,
    ADD_DOC_TOO_MANY_ATTEMPTS;

    companion object {
       private const val MAX_TRY_COUNT_TO_REPEAT_REQUEST = 1


        fun fromException(
            e: Exception,
            tryCountTracker: RequestTryCountTracker = RequestTryCountTracker(),
            flow: DialogErrorFlow = DialogErrorFlow.GENERAL
        ): DialogError {
            return if (e is HttpException) {
                when (e.code()) {
                    HTTP_400 -> {
                        CHECK_TIMEOUT
                    }
                    HTTP_404 -> {
                        if (flow == DialogErrorFlow.ADD_DOC) {
                            SPECIFIED_DOCUMENT_NOT_FOUND
                        } else {
                            REQUEST_NOT_REGISTERED
                        }
                    }
                    HTTP_422 -> {
                        if (flow == DialogErrorFlow.ADD_DOC) {
                            SPECIFIED_DOCUMENT_NOT_FOUND
                        } else {
                            returnUndefinedError(tryCountTracker)
                        }
                    }
                    HTTP_403 -> {
                        when (flow) {
                            DialogErrorFlow.PAYMENT -> {
                                PAYMENT_FORBIDDEN
                            }
                            DialogErrorFlow.ADD_DOC -> {
                                UNKNOWN_ERROR_RECOVERABLE
                            }
                            DialogErrorFlow.ASSIST -> {
                                ADD_DOC_TOO_MANY_ATTEMPTS
                            }
                            else -> {
                                returnUndefinedError(tryCountTracker)
                            }
                        }
                    }
                    else -> {
                        returnUndefinedError(tryCountTracker)
                    }
                }
            } else {
                return if (e is TimeoutException || e is UnknownHostException || e is ConnectException) {
                    NO_INTERNET
                } else {
                    if (tryCountTracker.tryCount < MAX_TRY_COUNT_TO_REPEAT_REQUEST) {
                        tryCountTracker.increment()
                        UNKNOWN_ERROR_RECOVERABLE
                    } else {
                        UNKNOWN_ERROR_UNRECOVERABLE
                    }
                }
            }
        }

        private fun returnUndefinedError(tryCountTracker: RequestTryCountTracker): DialogError {
            return if (tryCountTracker.tryCount < MAX_TRY_COUNT_TO_REPEAT_REQUEST) {
                tryCountTracker.increment()
                UNKNOWN_ERROR_RECOVERABLE
            } else {
                UNKNOWN_ERROR_UNRECOVERABLE
            }
        }
    }
}