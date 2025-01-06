package gr.questweaver.network.model

import gr.questweaver.model.Annotation
import gr.questweaver.model.TransformationData
import kotlinx.serialization.Serializable

@Serializable
internal sealed interface AnnotationDto : NetworkDto

@Serializable
internal data class DrawingDto(
    val id: String,
    val color: ULong,
    val strokeWidth: Float,
    val path: List<PointDto>,
    val createdBy: String,
    val transformationData: TransformationDataDto,
) : AnnotationDto

@Serializable
internal data class TextDto(
    val id: String,
    val text: String,
    val color: ULong,
    val createdBy: String,
    val transformationData: TransformationDataDto,
) : AnnotationDto

@Serializable
internal data class ImageDto(
    val id: String,
    val width: Float,
    val height: Float,
    val createdBy: String,
    val transformationData: TransformationDataDto,
    val file: FileDto,
) : AnnotationDto

@Serializable
internal data class TransformationDataDto(
    val scale: Float,
    val anchor: PointDto,
    val rotation: Float,
)

internal fun Annotation.toDto(): AnnotationDto =
    when (this) {
        is Annotation.Drawing ->
            DrawingDto(
                id = id,
                color = color,
                strokeWidth = strokeWidth,
                path = path.map { it.toDto() },
                createdBy = createdBy,
                transformationData = transformationData.toDto(),
            )
        is Annotation.Text ->
            TextDto(
                id = id,
                text = text,
                color = color,
                createdBy = createdBy,
                transformationData = transformationData.toDto(),
            )
        is Annotation.Image ->
            ImageDto(
                id = id,
                width = width,
                height = height,
                createdBy = createdBy,
                transformationData = transformationData.toDto(),
                file = file.toDto(),
            )
    }

internal fun AnnotationDto.toDomain(): Annotation =
    when (this) {
        is DrawingDto ->
            Annotation.Drawing(
                id = id,
                color = color,
                strokeWidth = strokeWidth,
                path = path.map { it.toDomain() },
                createdBy = createdBy,
                transformationData = transformationData.toDomain(),
            )
        is TextDto ->
            Annotation.Text(
                id = id,
                text = text,
                color = color,
                createdBy = createdBy,
                transformationData = transformationData.toDomain(),
            )
        is ImageDto ->
            Annotation.Image(
                id = id,
                width = width,
                height = height,
                createdBy = createdBy,
                transformationData = transformationData.toDomain(),
                file = file.toDomain(),
            )
    }

private fun TransformationData.toDto(): TransformationDataDto =
    TransformationDataDto(
        scale = scale,
        anchor = anchor.toDto(),
        rotation = rotation,
    )

private fun TransformationDataDto.toDomain(): TransformationData =
    TransformationData(
        scale = scale,
        anchor = anchor.toDomain(),
        rotation = rotation,
    )
