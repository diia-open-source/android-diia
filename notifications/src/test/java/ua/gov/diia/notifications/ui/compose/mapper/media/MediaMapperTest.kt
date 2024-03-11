package ua.gov.diia.notifications.ui.compose.mapper.media

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MediaMapperTest {

    @Test
    fun `test toComposeArticlePic`() {
        val url = "url"
        val result = url.toComposeArticlePic()

        assertEquals("", result!!.id)
        assertEquals(url, result.url)

        val nullUrl: String? = null
        val nullResult = nullUrl.toComposeArticlePic()

        assertEquals(null, nullResult)
    }

    @Test
    fun `test toComposeArticleVideo`() {
        val url = "url"
        val result = url.toComposeArticleVideo()
        assertEquals(url, result!!.url)

        val nullUrl: String? = null
        val nullResult = nullUrl.toComposeArticleVideo()

        assertEquals(null, nullResult)
    }
}