package ru.netology.nmedia.repository

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.dto.Media
import ru.netology.nmedia.dto.Post

interface PostRepository {
    val data: Flow<List<Post>>
    suspend fun fetchAndSaveInitialPosts()

    suspend fun refreshPosts()
    suspend fun markAllOldPostsAsNew()
    suspend fun getAll()
    fun getNewerCount(id: Long): Flow<Int>
    suspend fun save(post: Post)
    suspend fun saveWithImage(post: Post, imageUri: Uri)

    suspend fun uploadImage(imageUri: Uri): Media



    suspend fun removeById(id: Long)
    suspend fun likeById(id: Long)
}

