package gr.questweaver.network.serializer

import gr.questweaver.network.model.NetworkDto
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf

@OptIn(ExperimentalSerializationApi::class)
internal val ProtobufSerializer = ProtoBuf

internal inline fun NetworkDto.toByteArray(): ByteArray = ProtobufSerializer.encodeToByteArray(this)

internal fun ByteArray.fromByteArray(): NetworkDto = ProtobufSerializer.decodeFromByteArray(this)
