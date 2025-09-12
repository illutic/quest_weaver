package gr.questweaver.core.data

import gr.questweaver.core.model.User

interface UserRepository {
    suspend fun get(userId: String): Result<User>

    suspend fun save(user: User): Result<Unit>
}
