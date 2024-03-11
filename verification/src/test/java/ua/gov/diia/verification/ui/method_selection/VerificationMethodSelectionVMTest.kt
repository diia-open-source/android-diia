package ua.gov.diia.verification.ui.method_selection

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ua.gov.diia.verification.R
import ua.gov.diia.verification.model.VerificationMethodView
import ua.gov.diia.verification.model.VerificationMethodsView
import ua.gov.diia.verification.ui.VerificationSchema

class VerificationMethodSelectionVMTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: VerificationMethodSelectionVM

    @Before
    fun setUp() {
        viewModel = VerificationMethodSelectionVM()
    }

    @Test
    fun `color scheme`() {
        val expectedValues = mapOf(
            VerificationSchema.AUTHORIZATION to R.color.green_light,
            VerificationSchema.PROLONG to R.color.colorPrimary,
            VerificationSchema.GENERATE_SIGNATURE to R.color.colorPrimary,
            VerificationSchema.SIGNING to R.color.colorPrimary,
        )
        expectedValues.forEach { (schema, color) ->
            viewModel.doInit(VerificationMethodsView(null, emptyList(), "", schema))
            Assert.assertEquals(color, viewModel.themeColor.value)
        }
    }

    @Test
    fun `methods list`() {
        val methods = listOf(
            VerificationMethodView("method1", 0),
            VerificationMethodView("method2", 0),
            VerificationMethodView("method3", 0),
        )
        viewModel.doInit(
            VerificationMethodsView(
                "Title",
                methods,
                "Button",
                VerificationSchema.AUTHORIZATION
            )
        )
        Assert.assertEquals(methods.reversed(), viewModel.methods.value?.peekContent())
        Assert.assertEquals("Title", viewModel.header.value)
    }

    @Test
    fun `method selection`() {
        viewModel.doOnMethodSelected.invoke("test")
        Assert.assertEquals("test", viewModel.sendResult.value?.peekContent())
    }
}