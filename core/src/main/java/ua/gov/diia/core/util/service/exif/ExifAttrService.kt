package ua.gov.diia.core.util.service.exif

import ua.gov.diia.core.models.select_location.Coordinate

interface ExifAttrService {

    suspend fun getAttr(attrKey: String, imgUrl: String): String?

    suspend fun getLocationMetadata(imgUrl: String): Coordinate?
}