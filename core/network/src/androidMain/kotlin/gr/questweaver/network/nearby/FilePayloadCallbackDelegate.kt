package gr.questweaver.network.nearby

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import gr.questweaver.network.model.File
import gr.questweaver.network.model.FileMetadata
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

internal class FilePayloadCallbackDelegate(
    coroutineScope: CoroutineScope,
    private val context: Context,
    private val incomingPayloadsChannel: Channel<gr.questweaver.network.model.Payload>,
) : CoroutineScope by coroutineScope {
    private val pending = hashMapOf<Long, TransferData>()

    fun contains(id: Long) = pending.contains(id)

    fun updateMetadata(
        id: Long,
        fileMetadata: FileMetadata,
    ) {
        val transferData = pending[id] ?: return
        pending[id] = transferData.copy(fileMetadata = fileMetadata)
    }

    fun onPayloadReceived(data: Payload) {
        pending[data.id] =
            TransferData(
                payload = data,
                fileMetadata = FileMetadata(name = null, mimeType = null),
            )
    }

    fun onPayloadTransferUpdate(data: PayloadTransferUpdate) {
        when (data.status) {
            PayloadTransferUpdate.Status.SUCCESS -> {
                val transferData = pending.remove(data.payloadId) ?: return
                checkNotNull(transferData.fileMetadata.name) {
                    "We should have a name for a file payload by the time it's finished"
                }

                transferData.payload.asUri()?.let {
                    it.copyToCache(transferData.fileMetadata.name)
                    it.delete()
                }

                val cacheUri = getFile(transferData.fileMetadata.name).toUri().toString()

                launch {
                    incomingPayloadsChannel.send(
                        File(
                            uri = cacheUri,
                            meta = transferData.fileMetadata,
                        ),
                    )
                }
            }

            PayloadTransferUpdate.Status.FAILURE -> {
                pending
                    .remove(data.payloadId)
                    ?.payload
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

    private data class TransferData(
        val payload: Payload,
        val fileMetadata: FileMetadata,
    )
}

internal fun Uri.openFileDescriptor(context: Context) = context.contentResolver.openFileDescriptor(this, "r")
