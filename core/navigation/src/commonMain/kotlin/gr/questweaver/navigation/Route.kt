package gr.questweaver.navigation

interface Route {
    val path: String
    val id: String
    val popBackStack: Boolean get() = false
}

interface SheetRoute {
    val path: String
    val id: String
}
