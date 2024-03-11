package ua.gov.diia.bankid.ui

import android.os.BaseBundle
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import ua.gov.diia.bankid.BankIdConst
import ua.gov.diia.bankid.R
import ua.gov.diia.verification.ui.VerificationSchema
import java.util.UUID

class VerificationMethodBankIdTest {

    private lateinit var verificationMethod: VerificationMethodBankId

    @Before
    fun before() {
        verificationMethod = VerificationMethodBankId()
        val bundleMock = Mockito.mock(BaseBundle::class.java)
        Mockito.doNothing().`when`(bundleMock).putString(any(), anyOrNull())
    }

    @Test
    fun getName() {
        Assert.assertEquals(BankIdConst.METHOD_BANK_ID, verificationMethod.name)
    }

    @Test
    fun getVerificationRequest() = runTest {
        val schema = VerificationSchema.AUTHORIZATION
        val processId = UUID.randomUUID().toString()
        val request = verificationMethod.getVerificationRequest(
            verificationSchema = schema,
            processId = processId,
        )
        Assert.assertFalse(request.shouldLaunchUrl)
        Assert.assertNull(request.url)
        Assert.assertNotNull(request.navRequest)
        val navDirection = checkNotNull(request.navRequest).getNavDirection(1, "")
        Assert.assertEquals(R.id.action_global_destination_bankSelection, navDirection.actionId)
    }
}