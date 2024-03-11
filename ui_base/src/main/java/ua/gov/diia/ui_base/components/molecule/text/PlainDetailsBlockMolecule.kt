package ua.gov.diia.ui_base.components.molecule.text

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle

@Composable
fun PlainDetailsBlockMolecule(
    modifier: Modifier = Modifier,
    data: PlainDetailsBlockMoleculeData,
    onUIAction: (UIAction) -> Unit
) {

    val localData = remember { mutableStateOf(data) }

    LaunchedEffect(key1 = data) {
        localData.value = data
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp)
    ) {
        localData.value.title?.let {
            Text(
                text = localData.value.title ?: "",
                style = DiiaTextStyle.t3TextBody,
                color = Black
            )
        }
        localData.value.items.forEachIndexed { index, item ->
            when (item) {
                is DetailsTextDescriptionMoleculeData -> {
                    DetailsTextDescriptionMolecule(
                        data = item,
                        onUIAction = onUIAction
                    )
                }

                is DetailsTextLabelMoleculeData -> {
                    DetailsTextLabelMolecule(
                        data = item,
                        onUIAction = onUIAction
                    )
                }
            }
            if (index != localData.value.items.size - 1) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

data class PlainDetailsBlockMoleculeData(
    val title: String? = null,
    val items: SnapshotStateList<DetailsText>,
    val isDivided: Boolean = true
): UIElementData

@Composable
@Preview
fun PlainDetailsBlockMoleculePreview_WithoutTitle() {
    val item1 = DetailsTextDescriptionMoleculeData(
        title = "Title",
        description = "Descrription"
    )
    val item2 = DetailsTextLabelMoleculeData(
        title = "Title",
        description = "Descrription"
    )

    val data = PlainDetailsBlockMoleculeData(
        items = SnapshotStateList<DetailsText>().apply {
            add(item1)
            add(item1)
            add(item2)
            add(item2)
        }
    )

    PlainDetailsBlockMolecule(
        data = data
    ) {

    }
}

@Composable
@Preview
fun PlainDetailsBlockMoleculePreview_WithTitle() {
    val item1 = DetailsTextDescriptionMoleculeData(
        title = "Title",
        description = "Descrription"
    )
    val item2 = DetailsTextLabelMoleculeData(
        title = "Title",
        description = "Descrription"
    )

    val data = PlainDetailsBlockMoleculeData(
        title = "Title",
        items = SnapshotStateList<DetailsText>().apply {
            add(item1)
            add(item1)
            add(item2)
            add(item2)
        },
        isDivided = false
    )

    PlainDetailsBlockMolecule(
        data = data
    ) {

    }
}
