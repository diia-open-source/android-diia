package ua.gov.diia.doc_manual_options.utils

import ua.gov.diia.core.models.document.DocumentContextMenuAction


enum class DocManualOptionsContextMenuActions(override val action: String) :
    DocumentContextMenuAction {
    AWARD("award"),
    BIRTH_CERTIFICATE("birthCertificate"),
    USER_BIRTH_RECORD("userBirthRecord"),
    CHILD_VACCINATION_CERTIFICATE("childVaccinationCertificate"),
    VACCINATION_CERTIFICATE("vaccinationCertificate"),
    INTERNATIONAL_VACCINATION_CERTIFICATE("internationalVaccinationCertificate"),
    PENSION_CARD("pensionCard"),
    RESIDENCE_PERMIT_PERMANENT("residencePermitPermanent"),
    RESIDENCE_PERMIT_TEMPORARY("residencePermitTemporary"),
    VETERAN_CERTIFICATE("veteranCertificate"),
    UNMARRIED_CERTIFICATE("unmarriedCertificate"),
    WEAPON_PERMIT_CERTIFICATE("weaponPermit");


}