package ua.gov.diia.ui_base.components.organism.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.utils.dp
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.message.DraggableMessageMolecule
import ua.gov.diia.ui_base.components.molecule.message.MessageMoleculeData
import ua.gov.diia.ui_base.components.molecule.message.StubMessageMlc
import ua.gov.diia.ui_base.components.molecule.message.StubMessageMlcData
import ua.gov.diia.ui_base.components.subatomic.icon.IconWithBadge

const val CARD_OFFSET = -56f

@Composable
fun MessageListOrganism(
    modifier: Modifier = Modifier,
    data: MessageListOrganismData,
    onUIAction: (UIAction) -> Unit,
) {
    val items = data.items.collectAsLazyPagingItems()
    val loadState = items.loadState
    val finishedLoading =
        loadState.refresh !is LoadState.Loading &&
                loadState.prepend !is LoadState.Loading &&
                loadState.append !is LoadState.Loading
    val revealedItems = remember { mutableStateListOf<String>() }
    val deletedItems = remember { mutableStateListOf<String>() }

    if (items.itemCount == 0 && finishedLoading || deletedItems.size == items.itemCount && finishedLoading) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
            contentAlignment = Center
        ) {
            StubMessageMlc(
                modifier = modifier
                    .wrapContentSize(),
                data = data.emptyData,
                onUIAction = onUIAction
            )
        }
    } else {
        LazyColumn(Modifier.padding(bottom = 16.dp)) {
            items(
                count = items.itemCount,
                key = items.itemKey(key = { it.id }),
                contentType = items.itemContentType()
            ) { index ->
                val item = items[index]
                item?.let {
                    AnimatedVisibility(
                        visible = !deletedItems.contains(item.id),
                        enter = expandVertically(),
                        exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut(targetAlpha = 0.2f)
                    ) {
                        Box(Modifier.fillMaxWidth()) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .clickable {
                                        item.notificationId?.let {
                                            deletedItems.add(item.id)
                                        }
                                        onUIAction.invoke(
                                            UIAction(
                                                actionKey = "removeNotification",
                                                data = item.notificationId
                                            )
                                        )
                                    }
                            ) {
                                AnimatedVisibility(
                                    visible = revealedItems.contains(item.id),
                                    enter = fadeIn(),
                                    exit = shrinkOut(shrinkTowards = Center) + fadeOut(
                                        animationSpec = tween(
                                            durationMillis = 100,
                                            delayMillis = 0,
                                            easing = FastOutSlowInEasing
                                        )
                                    )
                                ) {
                                    IconWithBadge(
                                        modifier = Modifier
                                            .padding(top = 16.dp, end = 24.dp)
                                            .size(40.dp),
                                        image = UiText.StringResource(R.drawable.ic_button_remove),
                                    )
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 24.dp, end = 24.dp)
                                    .align(Alignment.BottomEnd)
                            ) {
                                DraggableMessageMolecule(
                                    data = item,
                                    isRevealed = revealedItems.contains(item.id),
                                    cardOffset = CARD_OFFSET.dp(),
                                    onExpand = { revealedItems.add(item.id) },
                                    onCollapse = { revealedItems.remove(item.id) },
                                    onUIAction = onUIAction
                                )
                            }
                        }
                    }
                    if (index == items.itemCount - 1) {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }

}

data class MessageListOrganismData(
    val items: Flow<PagingData<MessageMoleculeData>>,
    val emptyData: StubMessageMlcData
) : UIElementData

class MessageListState {
    val revealedItems = mutableStateListOf<String>()
    val deletedItems = mutableStateListOf<String>()

    fun deleteItem(itemId: String) {
        deletedItems.add(itemId)
    }
}


fun LazyListScope.loadMessageListOrganism(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<MessageMoleculeData>,
    onUIAction: (UIAction) -> Unit,
    state: MessageListState
) {
    val loadState = items.loadState
    val finishedLoading =
        loadState.refresh !is LoadState.Loading &&
                loadState.prepend !is LoadState.Loading &&
                loadState.append !is LoadState.Loading

    if (items.itemCount == 0 && finishedLoading || state.deletedItems.size == items.itemCount && finishedLoading) {
        item {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f),
                contentAlignment = Center
            ) {
                StubMessageMlc(
                    modifier = modifier.wrapContentSize(),
                    data = StubMessageMlcData(
                        icon = UiText.StringResource(R.string.error_message_notifications_icon),
                        title = UiText.StringResource(R.string.error_message_notifications_empty)
                    ),
                    onUIAction = onUIAction
                )
            }
        }
    } else {
        items(
            count = items.itemCount,
            key = items.itemKey(key = { it.id }),
            contentType = items.itemContentType()
        ) { index ->
            val item = items[index]
            item?.let {
                AnimatedVisibility(
                    visible = !state.deletedItems.contains(item.id),
                    enter = expandVertically(),
                    exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut(targetAlpha = 0.2f)
                ) {
                    Box(Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .clickable {
                                    item.notificationId?.let {
                                        state.deleteItem(item.id)
                                    }
                                    onUIAction.invoke(
                                        UIAction(
                                            actionKey = "removeNotification",
                                            data = item.notificationId
                                        )
                                    )
                                }
                        ) {
                            AnimatedVisibility(
                                visible = state.revealedItems.contains(item.id),
                                enter = fadeIn(),
                                exit = shrinkOut(shrinkTowards = Center) + fadeOut(
                                    animationSpec = tween(
                                        durationMillis = 100,
                                        delayMillis = 0,
                                        easing = FastOutSlowInEasing
                                    )
                                )
                            ) {
                                IconWithBadge(
                                    modifier = Modifier
                                        .padding(top = 16.dp, end = 24.dp)
                                        .size(40.dp),
                                    image = UiText.StringResource(R.drawable.ic_button_remove),
                                )
                            }
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 24.dp, end = 24.dp)
                                .align(Alignment.BottomEnd)
                        ) {
                            DraggableMessageMolecule(
                                data = item,
                                isRevealed = state.revealedItems.contains(item.id),
                                cardOffset = CARD_OFFSET.dp(),
                                onExpand = { state.revealedItems.add(item.id) },
                                onCollapse = { state.revealedItems.remove(item.id) },
                                onUIAction = onUIAction
                            )
                        }
                    }
                }
                if (index == items.itemCount - 1) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}


@Composable
@Preview
fun MessageListOrganismPreview() {
    val list = mutableListOf<MessageMoleculeData>()
    list.add(
        MessageMoleculeData(
            id = "1",
            title = "Title1",
            shortText = "shortText1",
            creationDate = "date for 1",
        )
    )
    list.add(
        MessageMoleculeData(
            id = "2",
            title = "Title2",
            shortText = "shortText2",
            creationDate = "date for 2",
        )
    )
    val items: Flow<PagingData<MessageMoleculeData>> = flowOf(PagingData.from(list))
    MessageListOrganism(
        data = MessageListOrganismData(
            items, emptyData = StubMessageMlcData(
                icon = UiText.StringResource(R.string.error_message_notifications_icon),
                title = UiText.StringResource(R.string.error_message_notifications_empty)
            )
        ),
        onUIAction = {
            val id = it.data
            list.find { ix -> ix.notificationId == id }?.let { a ->
                list.remove(a)
            }
        }
    )
}
