package ru.netology.nmedia.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Attachment
import ru.netology.nmedia.dto.AttachmentType
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val authorAvatar: String?,
    val content: String,
    val published: Long,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val isNew: Boolean = true,
    // Встраиваемое вложение
    @Embedded(prefix = "attachment_") // Добавим префикс, чтобы избежать конфликтов имён
    var attachment: AttachmentEmbeddable?
) {

    data class AttachmentEmbeddable(
        var url: String,
        var type: AttachmentType
    ) {

        fun toDto() = Attachment(url, type)

        companion object {
            // Преобразование из DTO
            fun fromDto(dto: Attachment?) = dto?.let {
                AttachmentEmbeddable(it.url, it.type)
            }
        }
    }

    fun toDto() =
        Post(id, author, authorAvatar, content, published, likedByMe, likes, attachment?.toDto())

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(
                dto.id,
                dto.author,
                dto.authorAvatar,
                dto.content,
                dto.published,
                dto.likedByMe,
                dto.likes,
                attachment = AttachmentEmbeddable.fromDto(dto.attachment)
            )

    }
}

fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)
fun List<Post>.toEntity(): List<PostEntity> = map {
    // Используем существующий метод fromDto и сразу меняем флаг
    PostEntity.fromDto(it).copy(isNew = false)
}
