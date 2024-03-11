package ua.gov.diia.biometric.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import ua.gov.diia.biometric.rules.MainDispatcherRule
import ua.gov.diia.biometric.store.BiometricRepository
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.ui_base.components.atom.button.BtnPlainAtmData
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryDefaultAtmData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.molecule.text.TextLabelMlcData
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData

@RunWith(MockitoJUnitRunner::class)
class BiometricSetupVMTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var biometricRepository: BiometricRepository

    @Mock
    lateinit var errorHandling: WithErrorHandlingOnFlow

    @Mock
    lateinit var retryLastAction: WithRetryLastAction

    lateinit var viewModel: BiometricSetupVM

    @Before
    fun setUp() {
        viewModel = BiometricSetupVM(
            biometricRepository = biometricRepository,
            errorHandling = errorHandling,
            retryLastAction = retryLastAction,
        )
    }

    @Test
    fun `initial state`() = runTest {
        val snapshot = viewModel.uiData.toList()
        Assert.assertEquals(4, snapshot.size)
        Assert.assertTrue(snapshot[0] is TopGroupOrgData)
        Assert.assertTrue(snapshot[1] is TextLabelMlcData)
        Assert.assertTrue(snapshot[2] is BtnPrimaryDefaultAtmData)
        Assert.assertTrue(snapshot[3] is BtnPlainAtmData)
    }

    @Test
    fun `enable biometric auth`() = runTest {
        viewModel.navigation.test {
            viewModel.onUIAction(UIAction(UIActionKeysCompose.BUTTON_REGULAR))
            Assert.assertEquals(BiometricSetupVM.Navigation.CompletePinCreation, awaitItem())
        }
        verify(biometricRepository).enableBiometricAuth(true)
    }

    @Test
    fun `do not enable biometric auth`() = runTest {
        viewModel.navigation.test {
            viewModel.onUIAction(UIAction(UIActionKeysCompose.BUTTON_ALTERNATIVE))
            Assert.assertEquals(BiometricSetupVM.Navigation.CompletePinCreation, awaitItem())
        }
        verifyNoMoreInteractions(biometricRepository)
    }

    @Test
    fun `test no reaction on wrong action`() = runTest {
        viewModel.onUIAction(UIAction(UIActionKeysCompose.BTN_PLAIN_ATM))
        verifyNoMoreInteractions(biometricRepository)
    }
}