package gr.questweaver.network.model

import gr.questweaver.model.Point
import kotlinx.serialization.Serializable

@Serializable
data class PointDto(
    val x: Float,
    val y: Float,
) : NetworkDto

fun Point.toDto(): PointDto =
    PointDto(
        x = x,
        y = y,
    )

fun PointDto.toDomain(): Point =
    Point(
        x = x,
        y = y,
    )
