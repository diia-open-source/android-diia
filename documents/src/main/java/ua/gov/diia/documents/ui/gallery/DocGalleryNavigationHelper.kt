package ua.gov.diia.documents.ui.gallery

import androidx.fragment.app.Fragment
import kotlinx.coroutines.flow.MutableSharedFlow
import ua.gov.diia.core.models.common.BackStackEvent

interface DocGalleryNavigationHelper {

    /**
     * subscribes for navigation events and handles navigation
     */
    fun subscribeForNavigationEvents(
        fragment: Fragment,
        navigationBackStackEventFlow: MutableSharedFlow<BackStackEvent>
    )

    /**
     * subscribes for navigation events and handles navigation
     */
    fun subscribeForStackNavigationEvents(
        fragment: Fragment,
        navigationBackStackEventFlow: MutableSharedFlow<BackStackEvent>
    )

}