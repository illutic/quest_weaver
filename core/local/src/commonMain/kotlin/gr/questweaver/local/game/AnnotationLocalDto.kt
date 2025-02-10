package gr.questweaver.local.game

import gr.questweaver.model.Annotation
import gr.questweaver.model.File
import gr.questweaver.model.Point
import gr.questweaver.model.TransformationData

sealed interface AnnotationLocalDto {
    interface Drawing : AnnotationLocalDto {
        val id: String
        val color: ULong
        val strokeWidth: Float
        val path: List<PointLocalDto>
        val createdBy: String
        val transformationData: TransformationDataLocalDto
    }

    interface Text : AnnotationLocalDto {
        val id: String
        val text: String
        val color: ULong
        val createdBy: String
        val transformationData: TransformationDataLocalDto
    }

    interface Image : AnnotationLocalDto {
        val id: String
        val file: FileLocalDto
        val width: Float
        val height: Float
        val createdBy: String
        val transformationData: TransformationDataLocalDto
    }
}

interface PointLocalDto {
    val x: Float
    val y: Float
}

interface TransformationDataLocalDto {
    val scale: Float
    val anchor: PointLocalDto
    val rotation: Float
}

interface FileLocalDto {
    val name: String
    val uri: String
}

expect fun Annotation.toDto(): AnnotationLocalDto

expect fun Point.toDto(): PointLocalDto

expect fun File.toDto(): FileLocalDto

expect fun TransformationData.toDto(): TransformationDataLocalDto

fun AnnotationLocalDto.toDomain(): Annotation =
    when (this) {
        is AnnotationLocalDto.Drawing ->
            Annotation.Drawing(
                id = id,
                color = color,
                strokeWidth = strokeWidth,
                path = path.map { it.toDomain() },
                createdBy = createdBy,
                transformationData = transformationData.toDomain(),
            )
        is AnnotationLocalDto.Image ->
            Annotation.Image(
                id = id,
                file = file.toDomain(),
                width = width,
                height = height,
                createdBy = createdBy,
                transformationData = transformationData.toDomain(),
            )

        is AnnotationLocalDto.Text ->
            Annotation.Text(
                id = id,
                text = text,
                color = color,
                createdBy = createdBy,
                transformationData = transformationData.toDomain(),
            )
    }

fun PointLocalDto.toDomain(): Point = Point(x = x, y = y)

fun TransformationDataLocalDto.toDomain(): TransformationData =
    TransformationData(scale = scale, anchor = anchor.toDomain(), rotation = rotation)

fun FileLocalDto.toDomain(): File = File(name = name, uri = uri)
