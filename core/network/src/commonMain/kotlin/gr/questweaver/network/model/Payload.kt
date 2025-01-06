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

internal class File(
    val meta: FileMetadata,
    val uri: String,
) : Payload
