package gr.questweaver.model

data class Device(
    val id: String,
    val name: String,
    val state: DeviceState,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Device) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}

enum class DeviceState {
    Idle,
    Connecting,
    Connected,
    Error,
}
