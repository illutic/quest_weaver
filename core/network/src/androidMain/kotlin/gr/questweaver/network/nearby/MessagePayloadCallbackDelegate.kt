package gr.questweaver.network.nearby

import com.google.android.gms.nearby.connection.Payload
import gr.questweaver.network.model.Message
import gr.questweaver.network.serializer.fromByteArray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

internal class MessagePayloadCallbackDelegate(
    coroutineScope: CoroutineScope,
    private val incomingPayloadsChannel: Channel<gr.questweaver.network.model.Payload>,
) : CoroutineScope by coroutineScope {
    fun onPayloadReceived(data: Payload) {
        launch {
            incomingPayloadsChannel.send(Message(dto = data.asBytes()!!.fromByteArray()))
        }
    }
}
