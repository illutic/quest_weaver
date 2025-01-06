package gr.questweaver.network.model

import gr.questweaver.model.Game
import kotlinx.serialization.Serializable

@Serializable
internal data class GameDto(
    val title: String,
    val dm: UserDto,
    val imageUri: String? = null,
    val players: List<UserDto> = emptyList(),
    val annotations: List<AnnotationDto> = emptyList(),
    val allowEdits: Boolean = false,
) : NetworkDto

internal fun Game.toDto(): GameDto =
    GameDto(
        title = title,
        dm = dm.toDto(),
        imageUri = imageUri,
        players = players.map { it.toDto() },
        annotations = annotations.map { it.toDto() },
    )

internal fun GameDto.toDomain(): Game =
    Game(
        title = title,
        dm = dm.toDomain(),
        imageUri = imageUri,
        players = players.map { it.toDomain() },
        annotations = annotations.map { it.toDomain() },
    )
