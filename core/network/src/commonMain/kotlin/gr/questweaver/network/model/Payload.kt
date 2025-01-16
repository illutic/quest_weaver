package gr.questweaver.network.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

internal interface Payload

internal interface Stream : Payload

@Serializable
@JvmInline
internal value class Message(
    val dto: NetworkDto,
) : Payload

internal data class File(
    val uri: String,
    val name: String,
) : Payload

internal fun File.toDto(): FileDto = FileDto(uri = uri, name = name)
