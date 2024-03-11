package ua.gov.diia.publicservice.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import app.cash.turbine.ReceiveTurbine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import ua.gov.diia.core.models.common.menu.ContextMenuItem
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.publicservice.models.CategoryStatus
import ua.gov.diia.publicservice.models.PublicService
import ua.gov.diia.publicservice.models.PublicServiceCategory
import ua.gov.diia.publicservice.models.PublicServiceTab
import ua.gov.diia.publicservice.models.PublicServicesCategories

suspend fun <T> ReceiveTurbine<UiDataEvent<T>>.awaitUiEvent(): T {
    return checkNotNull(awaitItem().getContentIfNotHandled())
}

suspend fun <T> LiveData<UiDataEvent<T>>.awaitEvent(): T {
    return asFlow().mapNotNull { it.getContentIfNotHandled() }.first()
}

fun testCategories(status: CategoryStatus = CategoryStatus.active) = PublicServicesCategories(
    categories = listOf(
        PublicServiceCategory(
            code = "officeWorkspace",
            sortOrder = 95,
            icon = "👨‍💻",
            name = "Воркспейс",
            status = CategoryStatus.active,
            visibleSearch = false,
            publicServices = listOf(
                PublicService(
                    sortOrder = 252,
                    search = "Воркспейс",
                    code = "officeOfficialWorkspace",
                    name = "Воркспейс",
                    status = status,
                    contextMenu = listOf(
                        ContextMenuItem(
                            type = "supportServiceScreen",
                            name = "Служба підтримки",
                            code = null
                        ),
                        ContextMenuItem(
                            type = "faqCategory",
                            name = "Питання та відповіді",
                            code = "ServicesStateServant"
                        ),
                        ContextMenuItem(
                            type = "rating",
                            name = "Оцінити послугу",
                            code = null
                        )
                    )
                )
            ),
            tabCode = "office"
        ),
        PublicServiceCategory(
            code = "carServices",
            sortOrder = 600,
            icon = "🚗",
            name = "Водієві",
            status = CategoryStatus.active,
            visibleSearch = false,
            tabCode = "citizen",
            publicServices = listOf(
                PublicService(
                    sortOrder = 610,
                    search = "Штрафи ПДР",
                    code = "penalties",
                    name = "Штрафи ПДР",
                    status = CategoryStatus.active,
                    contextMenu = null
                ),
                PublicService(
                    sortOrder = 620,
                    search = "Належний користувач",
                    code = "properUser",
                    name = "Належний користувач",
                    status = CategoryStatus.active,
                    contextMenu = listOf(
                        ContextMenuItem(
                            type = "faqCategory",
                            name = "Питання та відповіді",
                            code = "properUser"
                        ),
                        ContextMenuItem(
                            type = "supportServiceScreen",
                            name = "Служба підтримки",
                            code = null
                        ),
                        ContextMenuItem(
                            type = "rating",
                            name = "Оцінити послугу",
                            code = null
                        )
                    )
                ),
                PublicService(
                    sortOrder = 630,
                    search = "Заміна посвідчення водія",
                    code = "replacementDriverLicense",
                    name = "Заміна посвідчення водія",
                    status = CategoryStatus.active,
                    contextMenu = listOf(
                        ContextMenuItem(
                            type = "faqCategory",
                            name = "Питання та відповіді",
                            code = "replacementDriverLicense"
                        ),
                        ContextMenuItem(
                            type = "supportServiceScreen",
                            name = "Служба підтримки",
                            code = null
                        )
                    )
                ),
                PublicService(
                    sortOrder = 640,
                    search = "Продати транспортний засіб",
                    code = "vehicleReRegistration",
                    name = "Продати транспортний засіб",
                    status = CategoryStatus.inactive,
                    contextMenu = listOf()
                )
            )
        )
    ),
    tabs = listOf(
        PublicServiceTab(
            code = "office",
            name = "Держслужбовцям",
            isChecked = false
        ),
        PublicServiceTab(
            code = "citizen",
            name = "Громадянам",
            isChecked = false
        )
    )
)
