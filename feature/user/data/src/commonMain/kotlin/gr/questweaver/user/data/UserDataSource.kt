package gr.questweaver.user.data

import gr.questweaver.user.data.local.UserDatabaseDto

internal interface UserDataSource {
    suspend fun getUserData(): Result<UserDatabaseDto>
    suspend fun setUserData(userDatabaseDto: UserDatabaseDto): Result<UserDatabaseDto>
}