package gr.questweaver.navigation

interface Route {
    val path: String
    val id: String
}

interface SheetRoute : Route {
    override val path: String
    override val id: String
}
