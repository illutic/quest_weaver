package gr.questweaver.domain.repository

import gr.questweaver.model.Game

interface GameRepository {
    suspend fun createGame(game: Game): Result<Game>

    suspend fun saveGame(game: Game): Result<Game>

    suspend fun getGame(id: String): Result<Game>

    suspend fun removeGame(id: String): Result<Unit>
}
