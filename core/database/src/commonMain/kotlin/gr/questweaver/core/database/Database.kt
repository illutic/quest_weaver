package gr.questweaver.core.database

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.CoroutineDispatcher

internal fun getRoomDatabase(
    builder: RoomDatabase.Builder<AppDatabase>,
    coroutineDispatcher: CoroutineDispatcher
): AppDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(coroutineDispatcher)
        .build()
}