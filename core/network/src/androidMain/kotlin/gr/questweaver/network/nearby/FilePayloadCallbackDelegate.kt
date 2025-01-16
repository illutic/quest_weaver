package gr.questweaver.network.nearby

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import gr.questweaver.network.model.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

internal class FilePayloadCallbackDelegate(
    coroutineScope: CoroutineScope,
    private val context: Context,
    private val incomingPayloadsChannel: Channel<gr.questweaver.network.model.Payload>,
) : CoroutineScope by coroutineScope {
    private val pending = hashMapOf<Long, Payload>()

    fun contains(id: Long) = pending.contains(id)

    fun onPayloadReceived(data: Payload) {
        pending[data.id] = data
    }

    fun onPayloadTransferUpdate(data: PayloadTransferUpdate) {
        when (data.status) {
            PayloadTransferUpdate.Status.SUCCESS -> {
                val uri = pending.remove(data.payloadId)?.asUri() ?: return
                val fileName = uri.lastPathSegment ?: return

                uri.copyToCache(fileName)
                uri.delete()

                val cacheUri = getFile(fileName).toUri().toString()

                launch {
                    incomingPayloadsChannel.send(
                        File(uri = cacheUri, name = fileName),
                    )
                }
            }

            PayloadTransferUpdate.Status.FAILURE -> {
                pending
                    .remove(data.payloadId)
                    ?.asUri()
                    ?.delete()
            }
        }
    }

    private fun Uri.delete() = context.contentResolver.delete(this, null, null)

    private fun Uri.copyToCache(fileName: String) =
        getFile(fileName)
            .outputStream()
            .use { writer ->
                context.contentResolver.openInputStream(this).use { it?.copyTo(writer) }
            }

    private fun getFile(fileName: String) =
        context
            .cacheDir
            .resolve(fileName)

    private fun Payload.asUri() = asFile()?.asUri()
}
