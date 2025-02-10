package gr.questweaver.local.game

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import gr.questweaver.common.serialization.ProtobufSerializer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import java.io.InputStream
import java.io.OutputStream

actual fun Module.provideGameLocalDataSource(): KoinDefinition<GameLocalDataSource> =
    single {
        GameLocalDataSourceAndroidImpl(
            context = get(),
            coroutineDispatcher = get(),
        )
    }

private class GameLocalDataSourceAndroidImpl(
    private val context: Context,
    private val coroutineDispatcher: CoroutineDispatcher,
) : GameLocalDataSource {
    private val Context.dataStore: DataStore<List<GameLocalAndroidDto>> by dataStore(
        fileName = "game.pb",
        serializer = GameLocalDtoSerializer(),
    )

    override suspend fun saveGame(game: GameLocalDto): List<GameLocalDto> {
        require(game is GameLocalAndroidDto) {
            "Game must be of type GameLocalAndroidDto"
        }
        return context.dataStore.updateData {
            it + game
        }
    }

    override suspend fun getGame(id: String): GameLocalDto =
        context.dataStore.data
            .first()
            .find { it.id == id }
            ?: error("Game with id $id not found")

    override suspend fun removeGame(id: String) {
        context.dataStore.updateData {
            it.filter { it.id != id }
        }
    }

    private inner class GameLocalDtoSerializer : Serializer<List<GameLocalAndroidDto>> {
        override val defaultValue: List<GameLocalAndroidDto> = emptyList()

        @OptIn(ExperimentalSerializationApi::class)
        override suspend fun readFrom(input: InputStream): List<GameLocalAndroidDto> =
            withContext(coroutineDispatcher) {
                input.use {
                    ProtobufSerializer.decodeFromByteArray(input.readBytes())
                }
            }

        @OptIn(ExperimentalSerializationApi::class)
        override suspend fun writeTo(
            t: List<GameLocalAndroidDto>,
            output: OutputStream,
        ) {
            withContext(coroutineDispatcher) {
                output.use {
                    it.write(ProtobufSerializer.encodeToByteArray(t))
                }
            }
        }
    }
}
