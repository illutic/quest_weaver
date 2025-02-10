package gr.questweaver.local.game

import gr.questweaver.model.Annotation
import gr.questweaver.model.File
import gr.questweaver.model.Point
import gr.questweaver.model.TransformationData
import kotlinx.serialization.Serializable

@Serializable
data class DrawingAndroid(
    override val id: String,
    override val color: ULong,
    override val strokeWidth: Float,
    override val path: List<PointLocalDto>,
    override val createdBy: String,
    override val transformationData: TransformationDataAndroidDto,
) : AnnotationLocalDto.Drawing

@Serializable
data class TextAndroid(
    override val id: String,
    override val text: String,
    override val color: ULong,
    override val createdBy: String,
    override val transformationData: TransformationDataAndroidDto,
) : AnnotationLocalDto.Text

@Serializable
data class ImageAndroid(
    override val id: String,
    override val file: FileAndroidDto,
    override val width: Float,
    override val height: Float,
    override val createdBy: String,
    override val transformationData: TransformationDataAndroidDto,
) : AnnotationLocalDto.Image

@Serializable
data class PointLocalAndroidDto(
    override val x: Float,
    override val y: Float,
) : PointLocalDto

@Serializable
data class TransformationDataAndroidDto(
    override val scale: Float,
    override val anchor: PointLocalAndroidDto,
    override val rotation: Float,
) : TransformationDataLocalDto

@Serializable
data class FileAndroidDto(
    override val name: String,
    override val uri: String,
) : FileLocalDto

actual fun Annotation.toDto(): AnnotationLocalDto =
    when (this) {
        is Annotation.Drawing ->
            DrawingAndroid(
                id = id,
                color = color,
                strokeWidth = strokeWidth,
                path = path.map { it.toDto() },
                createdBy = createdBy,
                transformationData = transformationData.toDto() as TransformationDataAndroidDto,
            )
        is Annotation.Image ->
            ImageAndroid(
                id = id,
                file = file.toDto() as FileAndroidDto,
                width = width,
                height = height,
                createdBy = createdBy,
                transformationData = transformationData.toDto() as TransformationDataAndroidDto,
            )

        is Annotation.Text ->
            TextAndroid(
                id = id,
                text = text,
                color = color,
                createdBy = createdBy,
                transformationData = transformationData.toDto() as TransformationDataAndroidDto,
            )
    }

actual fun Point.toDto(): PointLocalDto = PointLocalAndroidDto(x = x, y = y)

actual fun TransformationData.toDto(): TransformationDataLocalDto =
    TransformationDataAndroidDto(
        scale = scale,
        anchor = anchor.toDto() as PointLocalAndroidDto,
        rotation = rotation,
    )

actual fun File.toDto(): FileLocalDto =
    FileAndroidDto(
        uri = uri,
        name = name,
    )
