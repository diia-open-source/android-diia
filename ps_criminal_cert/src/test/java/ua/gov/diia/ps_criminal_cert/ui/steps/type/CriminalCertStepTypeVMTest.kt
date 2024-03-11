package ua.gov.diia.ps_criminal_cert.ui.steps.type

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asFlow
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import ua.gov.diia.core.models.dialogs.TemplateDialogButton
import ua.gov.diia.core.models.dialogs.TemplateDialogData
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.delegation.WithRatingDialog
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertType
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertTypes
import ua.gov.diia.ps_criminal_cert.network.ApiCriminalCert
import ua.gov.diia.ps_criminal_cert.rules.MainDispatcherRule
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.ps_criminal_cert.util.StubContextMenu
import ua.gov.diia.ps_criminal_cert.util.StubErrorHandler
import ua.gov.diia.ps_criminal_cert.util.awaitEvent
import ua.gov.diia.publicservice.helper.PSNavigationHelper

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CriminalCertStepTypeVMTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var api: ApiCriminalCert

    @Mock
    private lateinit var retryLastAction: WithRetryLastAction

    @Mock
    private lateinit var withRatingDialog: WithRatingDialog

    @Mock
    private lateinit var navigationHelper: PSNavigationHelper

    private lateinit var viewModel: CriminalCertStepTypeVM

    @Before
    fun before() {
        viewModel = CriminalCertStepTypeVM(
            api = api,
            contextMenuDelegate = StubContextMenu(),
            errorHandlingDelegate = StubErrorHandler(),
            retryActionDelegate = retryLastAction,
            withRatingDialog = withRatingDialog,
            navigationHelper = navigationHelper
        )
    }

    @Test
    fun `get data success`() = runTest {
        val types = CriminalCertTypes(
            title = "title",
            subtitle = "subtitle",
            types = listOf(
                CriminalCertTypes.Type(
                    code = CriminalCertType.FULL,
                    name = "name 0",
                    description = "description 0",
                    isSelected = false
                ),
                CriminalCertTypes.Type(
                    code = CriminalCertType.SHORT,
                    name = "name 1",
                    description = "description 1",
                    isSelected = false
                )
            )
        )

        whenever(api.getCriminalCertTypes()) doReturn types

        viewModel.loadContent()
        viewModel.isLoading.asFlow().first { !it }

        Assert.assertEquals(types, viewModel.state.value)
    }

    @Test
    fun `refresh data success`() = runTest {
        val types = CriminalCertTypes(
            title = "title",
            subtitle = "subtitle",
            types = listOf(
                CriminalCertTypes.Type(
                    code = CriminalCertType.FULL,
                    name = "name 0",
                    description = "description 0",
                    isSelected = false
                ),
                CriminalCertTypes.Type(
                    code = CriminalCertType.SHORT,
                    name = "name 1",
                    description = "description 1",
                    isSelected = false
                )
            )
        )

        whenever(api.getCriminalCertTypes()) doReturn types

        viewModel.loadContent()
        viewModel.state.asFlow().first()
        viewModel.selectType(
            CriminalCertTypes.Type(
                code = CriminalCertType.SHORT,
                name = "name 1",
                description = "description 1",
                isSelected = false
            )
        )
        viewModel.loadContent()

        Assert.assertEquals(
            CriminalCertTypes(
                title = "title",
                subtitle = "subtitle",
                types = listOf(
                    CriminalCertTypes.Type(
                        code = CriminalCertType.FULL,
                        name = "name 0",
                        description = "description 0",
                        isSelected = false
                    ),
                    CriminalCertTypes.Type(
                        code = CriminalCertType.SHORT,
                        name = "name 1",
                        description = "description 1",
                        isSelected = true
                    )
                )
            ),
            viewModel.state.asFlow().first()
        )
    }

    @Test
    fun `get data error`() = runTest {
        val template = template()
        val types = CriminalCertTypes(
            title = "title",
            subtitle = "subtitle",
            types = listOf(
                CriminalCertTypes.Type(
                    code = CriminalCertType.FULL,
                    name = "name 0",
                    description = "description 0",
                    isSelected = false
                ),
                CriminalCertTypes.Type(
                    code = CriminalCertType.SHORT,
                    name = "name 1",
                    description = "description 1",
                    isSelected = false
                )
            ),
            template = template
        )

        whenever(api.getCriminalCertTypes()) doReturn types

        viewModel.loadContent()

        Assert.assertEquals(template, viewModel.showTemplateDialog.awaitEvent())
    }

    @Test
    fun `select item and go next`() = runTest {
        val types = CriminalCertTypes(
            title = "title",
            subtitle = "subtitle",
            types = listOf(
                CriminalCertTypes.Type(
                    code = CriminalCertType.FULL,
                    name = "name 0",
                    description = "description 0",
                    isSelected = false
                ),
                CriminalCertTypes.Type(
                    code = CriminalCertType.SHORT,
                    name = "name 1",
                    description = "description 1",
                    isSelected = false
                )
            )
        )

        whenever(api.getCriminalCertTypes()) doReturn types

        viewModel.onNext()
        Assert.assertNull(viewModel.onNextEvent.value?.getContentIfNotHandled())

        viewModel.loadContent()
        viewModel.state.asFlow().first()
        viewModel.selectType(
            CriminalCertTypes.Type(
                code = CriminalCertType.SHORT,
                name = "name 1",
                description = "description 1",
                isSelected = false
            )
        )
        Assert.assertTrue(viewModel.isNextAvailable.value == true)
        viewModel.onNext()

        Assert.assertEquals(
            CriminalCertType.SHORT,
            viewModel.onNextEvent.value?.getContentIfNotHandled()
        )
    }

    @Test
    fun `type selection`() = runTest {
        val types = CriminalCertTypes(
            title = "title",
            subtitle = "subtitle",
            types = listOf(
                CriminalCertTypes.Type(
                    code = CriminalCertType.FULL,
                    name = "name 0",
                    description = "description 0",
                    isSelected = false
                ),
                CriminalCertTypes.Type(
                    code = CriminalCertType.SHORT,
                    name = "name 1",
                    description = "description 1",
                    isSelected = false
                )
            )
        )

        whenever(api.getCriminalCertTypes()) doReturn types

        viewModel.selectType(checkNotNull(types.types)[0])
        Assert.assertNull(viewModel.state.value)
        Assert.assertFalse(viewModel.isNextAvailable.value == true)
        viewModel.loadContent()
        viewModel.isLoading.asFlow().first { !it }
        val state = viewModel.state.asFlow().filterNotNull().first()

        viewModel.selectType(checkNotNull(state.types)[0])
        viewModel.selectType(checkNotNull(state.types)[0])
        viewModel.selectType(checkNotNull(state.types)[1])
        Assert.assertTrue(viewModel.isNextAvailable.value == true)
    }

    @Test
    fun `no types`() = runTest {
        val types = CriminalCertTypes(
            title = "title",
            subtitle = "subtitle",
            types = null,
        )

        whenever(api.getCriminalCertTypes()) doReturn types
        viewModel.loadContent()
        viewModel.state.asFlow().filterNotNull().first()
        Assert.assertFalse(viewModel.isNextAvailable.value == true)

        viewModel.selectType(
            CriminalCertTypes.Type(
                code = CriminalCertType.SHORT,
                name = "name 1",
                description = "description 1",
                isSelected = false
            )
        )
        Assert.assertFalse(viewModel.isNextAvailable.value == true)
    }

    @Test
    fun rating() = runTest {
        viewModel.getRatingForm()
        verify(withRatingDialog) {
            with(viewModel) {
                getRating(
                    CriminalCertConst.RATING_SERVICE_CATEGORY,
                    CriminalCertConst.RATING_SERVICE_CODE
                )
            }
        }
        val request = mock<RatingRequest>()
        viewModel.sendRatingRequest(request)
        verify(withRatingDialog) {
            with(viewModel) {
                sendRating(
                    request,
                    CriminalCertConst.RATING_SERVICE_CATEGORY,
                    CriminalCertConst.RATING_SERVICE_CODE
                )
            }
        }
    }

    private fun template() = TemplateDialogModel(
        key = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY,
        type = "",
        isClosable = false,
        data = TemplateDialogData(
            icon = null,
            title = "",
            description = null,
            mainButton = TemplateDialogButton(
                name = null,
                icon = null,
                action = ""
            ),
            alternativeButton = null
        )
    )
}
