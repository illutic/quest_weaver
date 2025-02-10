package gr.questweaver.local.user

import gr.questweaver.model.User
import kotlinx.serialization.Serializable

@Serializable
data class UserLocalDtoAndroidImpl(
    override val id: String,
    override val name: String,
) : UserLocalDto {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserLocalDtoAndroidImpl) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}

actual fun User.toDto(): UserLocalDto = UserLocalDtoAndroidImpl(id = id, name = name)
