package gr.questweaver.network.model

import gr.questweaver.model.File
import kotlinx.serialization.Serializable

@Serializable
internal data class FileDto(
    val uri: String,
    val name: String,
) : NetworkDto

internal fun File.toDto(): FileDto = FileDto(uri = uri, name = name)

internal fun FileDto.toDomain(): File =
    File(
        uri = uri,
        name = name,
    )
