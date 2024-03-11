package ua.gov.diia.ui_base.components.molecule.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.BlackSqueeze
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun DetailsBlockMolecule(
    modifier: Modifier = Modifier,
    data: DetailsBlockMoleculeData,
    onUIAction: (UIAction) -> Unit
) {

    val localData = remember { mutableStateOf(data) }

    LaunchedEffect(key1 = data) {
        localData.value = data
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = White, shape = RoundedCornerShape(8.dp)
            )
    ) {
        localData.value.title?.let {
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp),
                text = localData.value.title ?: "",
                style = DiiaTextStyle.t3TextBody,
                color = BlackAlpha30
            )
        }
        localData.value.items.forEachIndexed { index, item ->
            val itemsModifier: Modifier = Modifier
                .padding(vertical = 16.dp)
                .padding(horizontal = 16.dp)
            when (item) {
                is DetailsTextDescriptionMoleculeData -> {
                    DetailsTextDescriptionMolecule(
                        modifier = itemsModifier,
                        data = item,
                        onUIAction = onUIAction
                    )
                }

                is DetailsTextLabelMoleculeData -> {
                    DetailsTextLabelMolecule(
                        modifier = itemsModifier,
                        data = item,
                        onUIAction = onUIAction
                    )
                }
            }
            if (index != localData.value.items.size - 1) {
                DividerSlimAtom(color = BlackSqueeze)
            }
        }
    }
}

data class DetailsBlockMoleculeData(
    val title: String? = null,
    val items: SnapshotStateList<DetailsText>,
    val isDivided: Boolean = true
)

@Composable
@Preview
fun DetailsBlockMoleculePreview_WithoutTitle() {
    val item1 = DetailsTextDescriptionMoleculeData(
        title = "Title",
        description = "Descrription"
    )
    val item2 = DetailsTextLabelMoleculeData(
        title = "Title",
        description = "Descrription"
    )

    val data = DetailsBlockMoleculeData(
        items = SnapshotStateList<DetailsText>().apply {
            add(item1)
            add(item1)
            add(item2)
            add(item2)
        }
    )

    DetailsBlockMolecule(
        modifier = Modifier.padding(16.dp),
        data = data
    ) {

    }
}

@Composable
@Preview
fun DetailsBlockMoleculePreview_WithTitle() {
    val item1 = DetailsTextDescriptionMoleculeData(
        title = "Title",
        description = "Descrription"
    )
    val item2 = DetailsTextLabelMoleculeData(
        title = "Title",
        description = "Descrription"
    )

    val data = DetailsBlockMoleculeData(
        title = "Title",
        items = SnapshotStateList<DetailsText>().apply {
            add(item1)
            add(item1)
            add(item2)
            add(item2)
        },
        isDivided = false
    )

    DetailsBlockMolecule(
        modifier = Modifier.padding(16.dp),
        data = data
    ) {

    }
}
