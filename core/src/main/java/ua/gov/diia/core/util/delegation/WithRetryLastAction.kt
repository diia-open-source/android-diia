package ua.gov.diia.core.util.delegation


interface WithRetryLastAction {

    fun retryLastAction()

    fun setLastAction(action: () -> Unit)

}