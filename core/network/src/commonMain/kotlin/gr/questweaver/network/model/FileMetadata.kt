package gr.questweaver.network.model

import gr.questweaver.network.serializer.ProtobufSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromByteArray

@Serializable
internal data class FileMetadata(
    val name: String?,
    val mimeType: String?,
)

internal fun ByteArray.metadataOrNull() =
    try {
        ProtobufSerializer.decodeFromByteArray<FileMetadata>(this)
    } catch (e: Exception) {
        null
    }
