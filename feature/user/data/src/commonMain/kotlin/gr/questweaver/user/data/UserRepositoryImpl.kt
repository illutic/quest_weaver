package gr.questweaver.user.data

import gr.questweaver.user.data.local.toDomain
import gr.questweaver.user.data.local.toDto
import gr.questweaver.user.domain.User
import gr.questweaver.user.domain.UserRepository

internal class UserRepositoryImpl(
    private val userDataSource: UserDataSource,
) : UserRepository {
    private var cachedUser: User? = null

    override suspend fun getUser(): Result<User> {
        cachedUser?.let {
            return Result.success(it)
        }

        val result = userDataSource.getUserData()
        return result.fold(
            onSuccess = { userDto ->
                val user = userDto.toDomain()
                cachedUser = user
                Result.success(user)
            },
            onFailure = { error ->
                Result.failure(error)
            },
        )
    }

    override suspend fun setUser(user: User): Result<User> {
        val userDto = user.toDto()
        val result = userDataSource.setUserData(userDto)
        return result.fold(
            onSuccess = { updatedUserDto ->
                val updatedUser = updatedUserDto.toDomain()
                cachedUser = updatedUser
                Result.success(updatedUser)
            },
            onFailure = { error ->
                Result.failure(error)
            },
        )
    }
}
