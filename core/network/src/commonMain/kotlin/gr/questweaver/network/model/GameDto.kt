package gr.questweaver.network.model

import gr.questweaver.model.Game
import kotlinx.serialization.Serializable

@Serializable
internal data class GameDto(
    val id: String,
    val title: String,
    val dm: UserDto,
    val imageUri: String? = null,
    val players: List<UserDto> = emptyList(),
    val annotations: List<AnnotationDto> = emptyList(),
    val allowEdits: Boolean = false,
) : NetworkDto

internal fun Game.toDto(): GameDto =
    GameDto(
        id = id,
        title = title,
        dm = dm.toDto(),
        imageUri = imageUri,
        players = players.map { it.toDto() },
        annotations = annotations.map { it.toDto() },
    )

internal fun GameDto.toDomain(): Game =
    Game(
        id = id,
        title = title,
        dm = dm.toDomain(),
        imageUri = imageUri,
        players = players.map { it.toDomain() },
        annotations = annotations.map { it.toDomain() },
    )
