package gr.questweaver.data.game

import ScopeExecutorImpl
import gr.questweaver.common.coroutines.provideIoDispatcher
import gr.questweaver.domain.repository.GameRepository
import gr.questweaver.local.game.GameLocalDataSource
import gr.questweaver.local.game.provideGameLocalDataSource
import gr.questweaver.local.game.toDomain
import gr.questweaver.local.game.toDto
import gr.questweaver.model.Game
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.dsl.module

val gameModule =
    module {
        provideGameLocalDataSource()
        single<GameRepository> {
            GameRepositoryImpl(
                gameLocalDataSource = get(),
                dispatcher = provideIoDispatcher(),
            )
        }
    }

class GameRepositoryImpl(
    private val gameLocalDataSource: GameLocalDataSource,
    dispatcher: CoroutineDispatcher,
) : GameRepository,
    ScopeExecutor by ScopeExecutorImpl(dispatcher) {
    override suspend fun saveGame(game: Game): Result<List<Game>> =
        tryExecuteInScope {
            gameLocalDataSource.saveGame(game.toDto()).map { it.toDomain() }
        }

    override suspend fun getGame(id: String): Result<Game> =
        tryExecuteInScope {
            gameLocalDataSource.getGame(id).toDomain()
        }

    override suspend fun removeGame(id: String): Result<Unit> =
        tryExecuteInScope {
            gameLocalDataSource.removeGame(id)
        }
}
