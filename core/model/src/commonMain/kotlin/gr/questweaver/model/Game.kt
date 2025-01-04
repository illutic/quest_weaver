package gr.questweaver.model

data class Game(
    val title: String,
    val dm: User,
    val imageUri: String? = null,
    val players: List<User> = emptyList(),
    val annotations: List<Annotation> = emptyList(),
    val allowEdits: Boolean = false,
)
