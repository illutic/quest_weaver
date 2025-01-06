package gr.questweaver.network.model

import gr.questweaver.model.Annotation
import gr.questweaver.model.File
import gr.questweaver.model.Game
import gr.questweaver.model.Point
import gr.questweaver.model.User
import kotlinx.serialization.Serializable

@Serializable
sealed interface NetworkDto

internal fun NetworkDto.toDomain(): Any =
    when (this) {
        is AnnotationDto -> toDomain()
        is FileDto -> toDomain()
        is GameDto -> toDomain()
        is PointDto -> toDomain()
        is UserDto -> toDomain()
    }

internal fun Any.toNetworkDto(): NetworkDto =
    when (this) {
        is Annotation -> toDto()
        is File -> toDto()
        is Game -> toDto()
        is Point -> toDto()
        is User -> toDto()
        else -> error("Unexpected type: $this")
    }
