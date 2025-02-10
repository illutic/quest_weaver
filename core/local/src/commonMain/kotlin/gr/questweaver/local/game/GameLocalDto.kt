package gr.questweaver.local.game

import gr.questweaver.local.user.UserLocalDto
import gr.questweaver.local.user.toDomain
import gr.questweaver.model.Game

interface GameLocalDto {
    val id: String
    val title: String
    val dm: UserLocalDto
    val imageUri: String?
    val players: List<UserLocalDto>
    val annotations: List<AnnotationLocalDto>
}

expect fun Game.toDto(): GameLocalDto

fun GameLocalDto.toDomain(): Game =
    Game(
        id = id,
        title = title,
        dm = dm.toDomain(),
        imageUri = imageUri,
        players = players.map { it.toDomain() },
        annotations = annotations.map { it.toDomain() },
    )
