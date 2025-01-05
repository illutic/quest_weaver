package gr.questweaver.network.model

import kotlin.jvm.JvmInline

interface Payload

interface Stream : Payload

@JvmInline
value class Message(
    val value: ByteArray,
) : Payload

class File(
    val meta: FileMetadata,
    val uri: String,
) : Payload
