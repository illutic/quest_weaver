package gr.questweaver.local.game

import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module

actual fun Module.provideGameLocalDataSource(): KoinDefinition<GameLocalDataSource> =
    single {
        GameLocalDataSourceIosImpl()
    }

private class GameLocalDataSourceIosImpl : GameLocalDataSource {
    override suspend fun saveGame(game: GameLocalDto): List<GameLocalDto> {
        TODO("Not yet implemented")
    }

    override suspend fun getGame(id: String): GameLocalDto {
        TODO("Not yet implemented")
    }

    override suspend fun removeGame(id: String) {
        TODO("Not yet implemented")
    }
}
