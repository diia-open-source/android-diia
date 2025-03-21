package ua.gov.diia.documents.ui

import ua.gov.diia.core.util.delegation.WithRetryLastAction

interface RootDocVm  : WithRetryLastAction {
    fun validateDocPresent(type: String)
}