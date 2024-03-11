package ua.gov.diia.ui_base.mappers

import ua.gov.diia.core.models.TextWithParameters
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextParameter
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersData
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText

fun TextWithParameters?.toComposeTextWithParameters(actionKey: String = UIActionKeysCompose.TEXT_WITH_PARAMETERS): TextWithParametersData? {
    val entity = this
    if (entity?.text == null) return null
    return entity.text?.let { text ->
        TextWithParametersData(
            actionKey = actionKey,
            text = UiText.DynamicString(text),
            parameters = if (entity.parameters != null) {
                mutableListOf<TextParameter>().apply {
                    entity.parameters?.forEach {
                        add(
                            TextParameter(
                                data = TextParameter.Data(
                                    name = it.data?.name?.let { n ->
                                        UiText.DynamicString(
                                            n
                                        )
                                    },
                                    resource = it.data?.resource?.let { r ->
                                        UiText.DynamicString(
                                            r
                                        )
                                    },
                                    alt = it.data?.alt?.let { a ->
                                        UiText.DynamicString(
                                            a
                                        )
                                    },
                                ),
                                type = it.type
                            )
                        )
                    }
                }
            } else {
                emptyList()
            }
        )
    }
}