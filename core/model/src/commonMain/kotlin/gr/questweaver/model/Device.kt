package gr.questweaver.model

data class Device(
    val id: String,
    val name: String,
    val state: DeviceState,
)

enum class DeviceState {
    Idle,
    Connecting,
    Connected,
    Found,
    Error
}