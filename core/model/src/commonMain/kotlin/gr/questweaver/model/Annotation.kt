package gr.questweaver.model

sealed interface Annotation {
    data class Drawing(
        val id: String,
        val color: ULong,
        val strokeWidth: Float,
        val path: List<Point>,
        val createdBy: String,
        val transformationData: TransformationData,
    ) : Annotation

    data class Text(
        val id: String,
        val text: String,
        val color: ULong,
        val createdBy: String,
        val transformationData: TransformationData,
    ) : Annotation

    data class Image(
        val id: String,
        val file: File,
        val width: Float,
        val height: Float,
        val createdBy: String,
        val transformationData: TransformationData,
    ) : Annotation
}

data class TransformationData(
    val scale: Float,
    val anchor: Point,
    val rotation: Float,
)
