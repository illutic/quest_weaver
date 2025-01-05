package gr.questweaver.network

import gr.questweaver.network.model.Stream
import java.io.InputStream

@JvmInline
value class StreamAndroidImpl(
    val value: InputStream,
) : Stream
