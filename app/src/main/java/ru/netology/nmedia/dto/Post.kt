package ru.netology.nmedia.dto

import java.io.File

data class Post(
    val id: Long,
    val author: String,
    val authorAvatar: String?,
    val content: String,
    val published: Long,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val attachment: Attachment? = null,
)

enum class AttachmentType {
    IMAGE
}
data class Attachment(
    val url: String,
    val type: AttachmentType,
)
// это дата класс который необходим для отправки на сервер картинки
// объект который мы будем ждать от сервера
data class Media(
    val id: String // Поле "id" из ответа сервера
)
data class MediaUpload(val file: File)

