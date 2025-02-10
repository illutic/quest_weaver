package gr.questweaver.local.game

import gr.questweaver.local.user.UserLocalDto
import gr.questweaver.local.user.toDto
import gr.questweaver.model.Game
import kotlinx.serialization.Serializable

@Serializable
data class GameLocalAndroidDto(
    override val id: String,
    override val title: String,
    override val dm: UserLocalDto,
    override val imageUri: String?,
    override val players: List<UserLocalDto> = emptyList(),
    override val annotations: List<AnnotationLocalDto> = emptyList(),
) : GameLocalDto

actual fun Game.toDto(): GameLocalDto =
    GameLocalAndroidDto(
        id = id,
        title = title,
        dm = dm.toDto(),
        imageUri = imageUri,
        players = players.map { it.toDto() },
        annotations = annotations.map { it.toDto() },
    )
