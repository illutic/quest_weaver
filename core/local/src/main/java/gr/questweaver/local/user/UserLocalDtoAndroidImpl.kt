package gr.questweaver.local.user

import kotlinx.serialization.Serializable

@Serializable
data class UserLocalDtoAndroidImpl(
    override val id: String,
    override val name: String,
) : UserLocalDto
