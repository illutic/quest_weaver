package gr.questweaver.local.user

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import gr.questweaver.common.coroutines.provideIoDispatcher
import gr.questweaver.common.serialization.ProtobufSerializer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import org.koin.android.ext.koin.androidApplication
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import java.io.InputStream
import java.io.OutputStream

actual fun Module.provideUserLocalDataSource(): KoinDefinition<UserLocalDataSource> =
    single {
        UserLocalDataSourceAndroidImpl(
            context = androidApplication(),
            coroutineDispatcher = provideIoDispatcher(),
        )
    }

private class UserLocalDataSourceAndroidImpl(
    private val context: Context,
    private val coroutineDispatcher: CoroutineDispatcher,
) : UserLocalDataSource {
    private val Context.dataStore: DataStore<UserLocalDto> by dataStore(
        fileName = "user.pb",
        serializer = UserLocalDtoSerializer(),
    )

    override suspend fun getUser(): UserLocalDto? = context.dataStore.data.first()

    override suspend fun saveUser(user: UserLocalDto): UserLocalDto = context.dataStore.updateData { user }

    override suspend fun clearUser() {
        context.dataStore.updateData {
            UserLocalDtoAndroidImpl(
                id = "",
                name = "",
            )
        }
    }

    private inner class UserLocalDtoSerializer : Serializer<UserLocalDto> {
        override val defaultValue: UserLocalDto =
            UserLocalDtoAndroidImpl(
                id = "",
                name = "",
            )

        @OptIn(ExperimentalSerializationApi::class)
        override suspend fun readFrom(input: InputStream): UserLocalDto =
            withContext(coroutineDispatcher) {
                input.use {
                    ProtobufSerializer.decodeFromByteArray(input.readBytes())
                }
            }

        @OptIn(ExperimentalSerializationApi::class)
        override suspend fun writeTo(
            t: UserLocalDto,
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
