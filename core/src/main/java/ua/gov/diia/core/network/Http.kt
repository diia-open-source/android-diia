package ua.gov.diia.core.network


object Http {
    const val HTTP_200 = 200
    const val HTTP_201 = 201
    const val HTTP_204 = 204
    const val HTTP_400 = 400
    const val HTTP_401 = 401
    const val HTTP_404 = 404
    const val HTTP_403 = 403
    const val HTTP_422 = 422
    const val HTTP_500 = 500
    const val HTTP_503 = 503

    const val HTTP_1010 = 1010
    const val HTTP_1011 = 1011
    const val HTTP_1012 = 1012
    const val HTTP_1013 = 1013
    const val HTTP_1014 = 1014
    const val HTTP_1015 = 1015
    const val HTTP_1016 = 1016
    const val HTTP_1017 = 1017
    const val HTTP_1020 = 1020
    const val HTTP_1050 = 1050
    const val HTTP_2501 = 2501
    const val HTTP_2502 = 2502
    const val HTTP_2503 = 2503





    const val COVID_CERT_IN_PROGRESS_STATUS = 2019
    const val NEED_UPDATE_STATUS = 5555
}

/**
 * Set of docStatuses that should be displayed in Documents
 */
fun getValidStatusesToDisplay(): Set<Int> {
    return setOf(
        Http.HTTP_200,
        Http.HTTP_400,
        Http.HTTP_404,
        Http.HTTP_1010,
        Http.HTTP_1011,
        Http.HTTP_1012,
        Http.HTTP_1013,
        Http.HTTP_1014,
        Http.HTTP_1015,
        Http.HTTP_1016,
        Http.HTTP_1017,
        Http.HTTP_1020,
        Http.HTTP_1050,
        Http.HTTP_2501,
        Http.HTTP_2502,
        Http.HTTP_2503,
    )
}