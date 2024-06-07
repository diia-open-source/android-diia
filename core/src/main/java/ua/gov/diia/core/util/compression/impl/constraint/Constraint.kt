package ua.gov.diia.core.util.compression.impl.constraint

import java.io.File

interface Constraint {
    fun isSatisfied(imageFile: File): Boolean

    fun satisfy(imageFile: File): File
}