package gr.questweaver.user.data.local

import gr.questweaver.user.data.UserDataSource

internal class LocalUserDataSource(
    private val dao: UserDao
) : UserDataSource {
    override suspend fun getUserData(): Result<UserDatabaseDto> = runCatching {
        val user = dao.getUser() ?: throw NoSuchElementException("No user data found")
        user
    }

    override suspend fun setUserData(userDatabaseDto: UserDatabaseDto): Result<UserDatabaseDto> =
        runCatching {
            dao.insertUser(userDatabaseDto)
            dao.getUser() ?: error("Failed to retrieve user after insertion")
        }
}