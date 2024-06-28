package ua.gov.diia.core.util

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat

class GradientBuilder(
    private val colorsList: List<Int>,
    private val colorExtractor: ColorExtractor,
) {

    fun getDocumentPreviewGradient(
        position: Int,
        positionOffset: Float
    ): ColorDrawable {
        val leftColor: Int
        var middleColor: Int
        val rightColor: Int

        when {
            position == 0 -> {
                leftColor = colorExtractor.getColor(colorsList[position])
                middleColor = leftColor
                rightColor = colorExtractor.getColor(colorsList[position + 1])
            }
            colorsList.size - position == 1 -> {
                rightColor = colorExtractor.getColor(colorsList[position])
                middleColor = rightColor
            }
            else -> {
                rightColor = colorExtractor.getColor(colorsList[position + 1])
                middleColor = colorExtractor.getColor(colorsList[position])
            }
        }
        if (positionOffset != 0.0f) {
            middleColor = ArgbEvaluator().evaluate(
                positionOffset,
                middleColor,
                rightColor
            ) as Int
        }
        return ColorDrawable(middleColor)
    }

    interface ColorExtractor {

        fun getColor(color: Int): Int
    }

    class ResourceColorExtractor(private val context: Context) :
        ColorExtractor {
        override fun getColor(color: Int) = ContextCompat.getColor(context, color)
    }

    class RawColorExtractor : ColorExtractor {
        override fun getColor(color: Int) = color
    }
}