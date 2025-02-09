package gr.questweaver.local.user

import gr.questweaver.model.User
import platform.CoreData.NSManagedObject

class UserLocalDtoIosImpl(
    override val id: String,
    override val name: String,
) : NSManagedObject(),
    UserLocalDto {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserLocalDtoIosImpl) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}

actual fun User.toDto(): UserLocalDto = UserLocalDtoIosImpl(id = id, name = name)
