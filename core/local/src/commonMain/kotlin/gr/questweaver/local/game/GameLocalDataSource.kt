package gr.questweaver.local.game

import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module

expect fun Module.provideGameLocalDataSource(): KoinDefinition<GameLocalDataSource>

interface GameLocalDataSource {
    suspend fun saveGame(game: GameLocalDto): List<GameLocalDto>

    suspend fun getGame(id: String): GameLocalDto

    suspend fun removeGame(id: String)
}
