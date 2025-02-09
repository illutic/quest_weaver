package gr.questweaver.local.user

import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module

expect fun Module.provideUserLocalDataSource(): KoinDefinition<UserLocalDataSource>

interface UserLocalDataSource {
    suspend fun getUser(): UserLocalDto?

    suspend fun saveUser(user: UserLocalDto): UserLocalDto

    suspend fun clearUser()
}
