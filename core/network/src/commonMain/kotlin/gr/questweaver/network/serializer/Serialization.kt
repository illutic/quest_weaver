package gr.questweaver.network.serializer

import gr.questweaver.common.serialization.ProtobufSerializer
import gr.questweaver.network.model.NetworkDto
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray

internal inline fun NetworkDto.toByteArray(): ByteArray = ProtobufSerializer.encodeToByteArray(this)

internal fun ByteArray.fromByteArray(): NetworkDto =
    try {
        ProtobufSerializer.decodeFromByteArray(this)
    } catch (e: SerializationException) {
        error("Unable to deserialize bytes: ${decodeToString()}")
    }
