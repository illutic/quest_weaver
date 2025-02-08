package gr.questweaver.local.user

import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.dsl.module

val userLocalDataSourceModule: Module =
    module {
        provideUserLocalDataSource()
    }

internal expect fun Module.provideUserLocalDataSource(): KoinDefinition<UserLocalDataSource>

interface UserLocalDataSource {
    suspend fun getUser(): UserLocalDto?

    suspend fun saveUser(user: UserLocalDto): UserLocalDto
}
