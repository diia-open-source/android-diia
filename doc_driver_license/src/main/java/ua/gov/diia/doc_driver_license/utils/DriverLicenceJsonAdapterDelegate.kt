package ua.gov.diia.doc_driver_license.utils

import ua.gov.diia.core.models.document.DocJsonAdapterDelegate
import ua.gov.diia.doc_driver_license.models.v2.DriverLicenseV2

class DriverLicenceJsonAdapterDelegate :  DocJsonAdapterDelegate<DriverLicenseV2.Data>(
    DriverLicenseV2.Data::class.java,
    "driver_licence_v2"
)