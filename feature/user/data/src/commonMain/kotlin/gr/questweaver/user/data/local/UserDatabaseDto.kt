package gr.questweaver.user.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import gr.questweaver.user.domain.User

@Entity
data class UserDatabaseDto(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
)

internal fun UserDatabaseDto.toDomain(): User =
    User(
        id = this.id.toString(),
        name = this.name,
    )

internal fun User.toDto(): UserDatabaseDto =
    UserDatabaseDto(
        id = this.id.toLongOrNull() ?: 0,
        name = this.name,
    )