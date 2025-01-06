package gr.questweaver.network.model

import gr.questweaver.model.User
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val name: String,
    val id: String,
) : NetworkDto

fun User.toDto(): UserDto =
    UserDto(
        name = name,
        id = id,
    )

fun UserDto.toDomain(): User =
    User(
        name = name,
        id = id,
    )
