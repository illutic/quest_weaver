package gr.questweaver.network.model

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.google.android.gms.nearby.connection.Payload
import gr.questweaver.model.File

private fun Uri.openFileDescriptor(context: Context) = context.contentResolver.openFileDescriptor(this, "r")

internal fun File.toFilePayload(context: Context): Payload =
    uri
        .toUri()
        .openFileDescriptor(context)
        ?.let(Payload::fromFile)
        ?.apply { setFileName(name) }
        ?: error("")
