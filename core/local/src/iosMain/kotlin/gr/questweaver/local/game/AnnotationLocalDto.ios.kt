package gr.questweaver.local.game

import gr.questweaver.model.Annotation
import gr.questweaver.model.File
import gr.questweaver.model.Point
import gr.questweaver.model.TransformationData
import platform.CoreData.NSManagedObject

data class DrawingIos(
    override val id: String,
    override val color: ULong,
    override val strokeWidth: Float,
    override val path: List<PointLocalDto>,
    override val createdBy: String,
    override val transformationData: TransformationDataIosDto,
) : NSManagedObject(),
    AnnotationLocalDto.Drawing

data class TextIos(
    override val id: String,
    override val text: String,
    override val color: ULong,
    override val createdBy: String,
    override val transformationData: TransformationDataIosDto,
) : NSManagedObject(),
    AnnotationLocalDto.Text

data class ImageIos(
    override val id: String,
    override val file: FileIosDto,
    override val width: Float,
    override val height: Float,
    override val createdBy: String,
    override val transformationData: TransformationDataIosDto,
) : NSManagedObject(),
    AnnotationLocalDto.Image

data class PointLocalIosDto(
    override val x: Float,
    override val y: Float,
) : NSManagedObject(),
    PointLocalDto

data class TransformationDataIosDto(
    override val scale: Float,
    override val anchor: PointLocalIosDto,
    override val rotation: Float,
) : NSManagedObject(),
    TransformationDataLocalDto

data class FileIosDto(
    override val name: String,
    override val uri: String,
) : NSManagedObject(),
    FileLocalDto

actual fun Annotation.toDto(): AnnotationLocalDto =
    when (this) {
        is Annotation.Drawing ->
            DrawingIos(
                id = id,
                color = color,
                strokeWidth = strokeWidth,
                path = path.map { it.toDto() },
                createdBy = createdBy,
                transformationData = transformationData.toDto() as TransformationDataIosDto,
            )
        is Annotation.Image ->
            ImageIos(
                id = id,
                file = file.toDto() as FileIosDto,
                width = width,
                height = height,
                createdBy = createdBy,
                transformationData = transformationData.toDto() as TransformationDataIosDto,
            )

        is Annotation.Text ->
            TextIos(
                id = id,
                text = text,
                color = color,
                createdBy = createdBy,
                transformationData = transformationData.toDto() as TransformationDataIosDto,
            )
    }

actual fun Point.toDto(): PointLocalDto = PointLocalIosDto(x = x, y = y)

actual fun TransformationData.toDto(): TransformationDataLocalDto =
    TransformationDataIosDto(
        scale = scale,
        anchor = anchor.toDto() as PointLocalIosDto,
        rotation = rotation,
    )

actual fun File.toDto(): FileLocalDto =
    FileIosDto(
        uri = uri,
        name = name,
    )
