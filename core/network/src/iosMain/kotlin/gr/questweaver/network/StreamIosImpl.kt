package gr.questweaver.network

import gr.questweaver.network.model.Stream
import platform.Foundation.NSInputStream

value class StreamIosImpl(
    val value: NSInputStream,
) : Stream
