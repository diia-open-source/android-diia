package ua.gov.diia.documents.ui.gallery

import androidx.fragment.app.Fragment
import ua.gov.diia.documents.ui.DocVM

interface DocGalleryNavigationHelper {

    /**
     * subscribes for navigation events and handles navigation
     */
    fun subscribeForNavigationEvents(fragment: Fragment, viewModel: DocVM)

    /**
     * subscribes for navigation events and handles navigation
     */
    fun subscribeForStackNavigationEvents(fragment: Fragment, viewModel: DocVM)

}