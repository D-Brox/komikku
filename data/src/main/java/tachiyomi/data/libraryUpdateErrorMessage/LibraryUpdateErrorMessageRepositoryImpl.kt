package tachiyomi.data.libraryUpdateErrorMessage

import kotlinx.coroutines.flow.Flow
import tachiyomi.data.DatabaseHandler
import tachiyomi.domain.libraryUpdateErrorMessage.model.LibraryUpdateErrorMessage
import tachiyomi.domain.libraryUpdateErrorMessage.repository.LibraryUpdateErrorMessageRepository

class LibraryUpdateErrorMessageRepositoryImpl(
    private val handler: DatabaseHandler,
) : LibraryUpdateErrorMessageRepository {

    override suspend fun getAll(): List<LibraryUpdateErrorMessage> {
        return handler.awaitList {
            libraryUpdateErrorMessageQueries.getAllErrorMessages(
                LibraryUpdateErrorMessageMapper,
            )
        }
    }

    override fun getAllAsFlow(): Flow<List<LibraryUpdateErrorMessage>> {
        return handler.subscribeToList {
            libraryUpdateErrorMessageQueries.getAllErrorMessages(
                LibraryUpdateErrorMessageMapper,
            )
        }
    }

    override suspend fun deleteAll() {
        return handler.await { libraryUpdateErrorMessageQueries.deleteAllErrorMessages() }
    }

    override suspend fun get(message: String): Long? {
        return handler.awaitOneOrNullExecutable {
            libraryUpdateErrorMessageQueries.getErrorMessages(message) { id, _ -> id }
        }
    }

    override suspend fun insert(libraryUpdateErrorMessage: LibraryUpdateErrorMessage): Long {
        return handler.awaitOneExecutable(inTransaction = true) {
            libraryUpdateErrorMessageQueries.insertAndGet(libraryUpdateErrorMessage.message)
        }
    }

    override suspend fun insertAll(
        libraryUpdateErrorMessages: List<LibraryUpdateErrorMessage>,
    ): List<Pair<Long, String>> {
        return handler.await(inTransaction = true) {
            libraryUpdateErrorMessages.map {
                libraryUpdateErrorMessageQueries.insertAndGet(it.message).executeAsOne() to it.message
            }
        }
    }
}
