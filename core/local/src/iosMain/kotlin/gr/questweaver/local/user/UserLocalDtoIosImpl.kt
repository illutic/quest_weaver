package gr.questweaver.local.user

import platform.CoreData.NSManagedObject

data class UserLocalDtoIosImpl(
    override val id: String,
    override val name: String,
) : NSManagedObject(),
    UserLocalDto
