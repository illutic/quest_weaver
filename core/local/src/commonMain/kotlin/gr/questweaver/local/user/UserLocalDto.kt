package gr.questweaver.local.user

import gr.questweaver.model.User

interface UserLocalDto {
    val id: String
    val name: String
}

fun UserLocalDto.toDomain(): User = User(id = id, name = name)

expect fun User.toDto(): UserLocalDto
