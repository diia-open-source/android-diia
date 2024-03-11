package ua.gov.diia.doc_driver_license

import ua.gov.diia.documents.data.datasource.local.DocJsonAdapterDelegate

class DriverLicenceJsonAdapterDelegate :  DocJsonAdapterDelegate<DriverLicenseV2.Data>(
    DriverLicenseV2.Data::class.java,
    "driver_licence_v2"
)