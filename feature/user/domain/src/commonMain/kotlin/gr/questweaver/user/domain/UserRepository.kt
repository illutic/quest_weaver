package gr.questweaver.user.domain

interface UserRepository {
    suspend fun getUser(): Result<User>
    suspend fun setUser(user: User): Result<User>
}