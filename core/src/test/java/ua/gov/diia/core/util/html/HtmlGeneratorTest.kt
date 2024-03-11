package ua.gov.diia.core.util.html

import org.junit.Assert
import org.junit.Test

class HtmlGeneratorTest {
    @Test
    fun `convertToLink generate link html tag`() {
        Assert.assertEquals(
            convertToLink("url", "name"),
            "<a href=\"url\"><u>name</u></a>"
        )
    }

    @Test
    fun `convertToPhone generate phone html tag`() {
        Assert.assertEquals(
            convertToPhone(
                "tel",
                "name"
            ), "<a href=\"tel:tel\"><u>name</u></a>"
        )
    }

    @Test
    fun `convertToMail generate mail html tag`() {
        Assert.assertEquals(
            convertToMail(
                "mail",
                "name"
            ), "<a href=\"mailto:mail\"><u>name</u></a>"
        )
    }
}