package ua.gov.diia.login.ui

interface PostLoginAction {

    /**
     * This action will be performed after successful user authorization
      */
    suspend fun onPostLogin()
}