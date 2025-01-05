package gr.questweaver.network.nearby

import com.google.android.gms.nearby.connection.Payload
import gr.questweaver.network.StreamAndroidImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class StreamPayloadCallbackDelegate(
    coroutineScope: CoroutineScope,
    private val incomingPayloadsChannel: Channel<gr.questweaver.network.model.Payload>,
) : CoroutineScope by coroutineScope {
    fun onPayloadReceived(data: Payload) {
        launch {
            data.asStream()?.asInputStream()?.let {
                incomingPayloadsChannel.send(StreamAndroidImpl(it))
            }
        }
    }
}
