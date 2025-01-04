package gr.questweaver.model

import kotlin.math.sqrt

data class Point(
    val x: Float,
    val y: Float,
) {
    init {
        require(x in 0f..1f) { "x must be in [0, 1], but was $x" }
        require(y in 0f..1f) { "y must be in [0, 1], but was $y" }
    }

    fun distanceTo(other: Point): Float {
        val dx = x - other.x
        val dy = y - other.y
        return sqrt((dx * dx + dy * dy).toDouble()).toFloat()
    }
}

fun List<Point>.optimize(): List<Point> =
    runningReduce { acc, point ->
        if (acc.distanceTo(point) > POINT_TOLERANCE) point else acc
    }

private const val POINT_TOLERANCE = 0.01f
