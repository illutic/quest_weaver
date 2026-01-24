package gr.questweaver.ai.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import gr.questweaver.ai.AiEvent
import gr.questweaver.ai.AiState
import gr.questweaver.ai.AiViewModel
import gr.questweaver.ai.QuickAction
import gr.questweaver.ai.UiMessage

@Composable
fun AiAssistantScreen(
    modifier: Modifier = Modifier,
    viewModel: AiViewModel = viewModel(factory = AiViewModel.createFactory()),
) {
    val state by viewModel.state.collectAsState()
    AiAssistantContent(modifier = modifier, state = state, onEvent = viewModel::onEvent)
}

@Composable
fun AiAssistantContent(
    state: AiState,
    onEvent: (AiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        bottomBar = {
            Column(
                modifier =
                    Modifier.fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .imePadding()
            ) {
                if (state.quickActions.isNotEmpty() && state.messages.isEmpty()) {
                    QuickActionsRow(
                        actions = state.quickActions,
                        onActionClick = { onEvent(AiEvent.OnQuickActionClick(it)) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                InputArea(
                    text = state.inputText,
                    onTextChanged = { onEvent(AiEvent.OnInputChanged(it)) },
                    onSendClick = { onEvent(AiEvent.OnSendClick) },
                    onAttachClick = { onEvent(AiEvent.OnAttachClick) },
                    isGenerating = state.isGenerating
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (state.messages.isEmpty()) {
                EmptyState()
            } else {
                MessageList(messages = state.messages)
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "How can I help you today?",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun MessageList(messages: List<UiMessage>) {
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) { items(messages) { message -> MessageBubble(message = message) } }
}

@Composable
private fun MessageBubble(message: UiMessage) {
    val isUser = message.isUser
    val alignment = if (isUser) Alignment.End else Alignment.Start
    val containerColor =
        if (isUser) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.secondaryContainer
    val textColor =
        if (isUser) MaterialTheme.colorScheme.onPrimaryContainer
        else MaterialTheme.colorScheme.onSecondaryContainer

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = alignment) {
        Surface(
            color = containerColor,
            shape =
                RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (isUser) 16.dp else 4.dp,
                    bottomEnd = if (isUser) 4.dp else 16.dp
                ),
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(12.dp),
                color = textColor,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun QuickActionsRow(actions: List<QuickAction>, onActionClick: (QuickAction) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(actions) { action ->
            AssistChip(
                onClick = { onActionClick(action) },
                label = { Text(action.label) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            )
        }
    }
}

@Composable
private fun InputArea(
    text: String,
    onTextChanged: (String) -> Unit,
    onSendClick: () -> Unit,
    onAttachClick: () -> Unit,
    isGenerating: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onAttachClick) {
            Icon(Icons.Default.AttachFile, contentDescription = "Attach")
        }

        OutlinedTextField(
            value = text,
            onValueChange = onTextChanged,
            modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
            placeholder = { Text("Message...") },
            maxLines = 4,
            shape = RoundedCornerShape(24.dp)
        )

        Box(contentAlignment = Alignment.Center) {
            if (isGenerating) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
            } else {
                IconButton(
                    onClick = onSendClick,
                    enabled = text.isNotBlank(),
                    modifier =
                        Modifier.background(
                            if (text.isNotBlank()) MaterialTheme.colorScheme.primary
                            else Color.Transparent,
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint =
                            if (text.isNotBlank()) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
