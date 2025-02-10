package gr.questweaver.local.game

import gr.questweaver.local.user.UserLocalDtoIosImpl
import gr.questweaver.local.user.toDto
import gr.questweaver.model.Game
import platform.CoreData.NSManagedObject

data class GameLocalIosDto(
    override val id: String,
    override val title: String,
    override val dm: UserLocalDtoIosImpl,
    override val imageUri: String?,
    override val players: List<UserLocalDtoIosImpl> = emptyList(),
    override val annotations: List<AnnotationLocalDto> = emptyList(),
) : NSManagedObject(),
    GameLocalDto

actual fun Game.toDto(): GameLocalDto =
    GameLocalIosDto(
        id = id,
        title = title,
        dm = dm.toDto() as UserLocalDtoIosImpl,
        imageUri = imageUri,
        players = players.map { it.toDto() as UserLocalDtoIosImpl },
        annotations = annotations.map { it.toDto() },
    )
