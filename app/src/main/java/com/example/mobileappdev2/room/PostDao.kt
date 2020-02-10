package com.example.mobileappdev2.room

import androidx.room.*
import com.example.mobileappdev2.models.PostModel

@Dao
interface  PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(postModel: PostModel)

    @Query("SELECT * FROM PostModel")
    fun findAll(): List<PostModel>

    @Query("select * from PostModel where id = :id")
    fun findById(id: Long): PostModel

    @Update
    fun update(post: PostModel)

    @Delete
    fun deletePost(post: PostModel)

}
