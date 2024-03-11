package ua.gov.diia.ui_base.components.molecule.input

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.PeriwinkleGray
import ua.gov.diia.ui_base.components.theme.PeriwinkleGrayAlpha30
import ua.gov.diia.ui_base.components.theme.Violet
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun QuantityInputMolecule(
    modifier: Modifier = Modifier,
    data: QuantityInputMoleculeData,
    onUIAction: (UIAction) -> Unit
) {
    val localData = remember { mutableStateOf(data) }

    var min by remember { mutableStateOf(data.min) }
    var max by remember { mutableStateOf(data.max) }
    var value by remember { mutableStateOf(data.value) }
    var valueAsString by remember { mutableStateOf(data.value.toString()) }

    LaunchedEffect(key1 = value) {
        onUIAction(
            UIAction(
                actionKey = localData.value.actionKey,
                data = value.toString()
            )
        )
    }

    LaunchedEffect(key1 = data) {
        localData.value = data
        min = data.min
        max = data.max
        value = data.value
        valueAsString = data.value.toString()
    }

    Row(
        modifier = modifier
            .background(
                color = White, shape = RoundedCornerShape(8.dp)
            )
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        IconButton(
            modifier = Modifier
                .padding(16.dp)
                .size(42.dp)
                .background(
                    color = if (value > min) {
                        PeriwinkleGray
                    } else {
                        White
                    }, shape = RoundedCornerShape(7.dp)
                )
                .border(
                    color = PeriwinkleGrayAlpha30, width = 2.dp, shape = RoundedCornerShape(7.dp)
                ),
            enabled = value > min,
            onClick = {
                value -= 1
                valueAsString = value.toString()
            }
        ) {
            Icon(
                modifier = Modifier.width(18.dp),
                painter = painterResource(id = R.drawable.diia_icon_minus),
                contentDescription = stringResource(R.string.decrease_value),
                tint = if (value > min) {
                    White
                } else {
                    PeriwinkleGrayAlpha30
                }
            )
        }
        Column(
            modifier = modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BasicTextField(
                modifier = Modifier.padding(top = 16.dp, bottom = 3.dp),
                value = valueAsString,
                onValueChange = {
                    if (it.isEmpty()) {
                        value = 0
                        valueAsString = ""
                    } else {
                        try {
                            if (it.toInt() in min..max) {
                                value = it.toInt()
                                valueAsString = it
                            }
                        } catch (error: Throwable) {
                        }
                    }
                },
                textStyle = DiiaTextStyle.h1Heading.copy(textAlign = TextAlign.Center),
                singleLine = true,
                cursorBrush = SolidColor(Violet),
                decorationBox = @Composable { innerTextField ->
                    Box(modifier = Modifier.weight(1f)) {
                        innerTextField()
                    }
                }
            )
            localData.value.label?.let {
                Text(
                    text = localData.value.label ?: "",
                    color = Black,
                    style = DiiaTextStyle.t4TextSmallDescription
                )
            }
        }
        IconButton(
            modifier = Modifier
                .padding(16.dp)
                .size(42.dp)
                .background(
                    color = if (value < max) {
                        PeriwinkleGray
                    } else {
                        White
                    }, shape = RoundedCornerShape(7.dp)
                )
                .border(
                    color = PeriwinkleGrayAlpha30, width = 2.dp, shape = RoundedCornerShape(7.dp)
                ),
            enabled = value < max,
            onClick = {
                value += 1
                valueAsString = value.toString()
            }
        ) {
            Icon(
                modifier = Modifier.width(18.dp),
                painter = painterResource(id = R.drawable.diia_icon_plus),
                contentDescription = stringResource(R.string.increase_value),
                tint = if (value < max) {
                    White
                } else {
                    PeriwinkleGrayAlpha30
                }
            )
        }
    }
}

data class QuantityInputMoleculeData(
    val actionKey: String = UIActionKeysCompose.QUANTITY_INPUT_MOLECULE,
    val value: Int,
    val min: Int = 0,
    val max: Int = Int.MAX_VALUE,
    val label: String?
) : UIElementData

@Composable
@Preview
fun QuantityInputMoleculePreview() {
    QuantityInputMolecule(
        data = QuantityInputMoleculeData(value = 1, min = 0, max = 999999, label = "кількість облігацій")
    ) {
    }
}