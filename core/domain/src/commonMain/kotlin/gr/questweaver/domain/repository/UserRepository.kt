package gr.questweaver.domain.repository

import gr.questweaver.model.User

interface UserRepository {
    suspend fun getUser(): Result<User>

    suspend fun createUser(user: User): Result<User>

    suspend fun updateUser(user: User): Result<User>

    suspend fun removeUser(id: String): Result<Unit>
}