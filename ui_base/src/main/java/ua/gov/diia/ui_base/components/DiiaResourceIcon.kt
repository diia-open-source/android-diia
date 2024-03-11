package ua.gov.diia.ui_base.components

interface DiiaResourceIconProvider {
    fun getResourceId(code: String): Int

    fun getContentDescription(code: String): Int

    companion object {
        fun forPreview(): DiiaResourceIconProvider{
            return object : DiiaResourceIconProvider{
                override fun getResourceId(code: String): Int = -1

                override fun getContentDescription(code: String): Int = -1
            }
        }
    }
}

class DiiaResourceIcon(
    val code: String,
    val drawableResourceId: Int,
    val contentDescriptionResourceId: Int
)