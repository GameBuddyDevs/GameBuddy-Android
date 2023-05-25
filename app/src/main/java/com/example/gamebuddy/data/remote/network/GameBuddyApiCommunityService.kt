package com.example.gamebuddy.data.remote.network

import com.example.gamebuddy.data.remote.model.basic.BasicResponse
import com.example.gamebuddy.data.remote.model.comment.CommentResponse
import com.example.gamebuddy.data.remote.model.joincommunity.JoinCommunityResponse
import com.example.gamebuddy.data.remote.model.post.PostResponse
import com.example.gamebuddy.data.remote.request.CreateCommentRequest
import com.example.gamebuddy.util.Api
import com.example.gamebuddy.util.ApiType
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface GameBuddyApiCommunityService {

    @POST
    @Api(ApiType.COMMUNITY)
    suspend fun getCommunities(): JoinCommunityResponse

    @POST("like/post/{postId}")
    @Api(ApiType.COMMUNITY)
    suspend fun likePost(
        @Header("Authorization") token: String,
        @Path("postId") postId: String,
    ): BasicResponse

    @POST("like/comment/{commentId}")
    @Api(ApiType.COMMUNITY)
    suspend fun likeComment(
        @Header("Authorization") token: String,
        @Path("commentId") commentId: String,
    ): BasicResponse

    @POST("create/comment")
    @Api(ApiType.COMMUNITY)
    suspend fun createComment(
        @Header("Authorization") token: String,
        @Body comment: CreateCommentRequest,
    ): BasicResponse

    @GET("get/post/comments/{postId}")
    @Api(ApiType.COMMUNITY)
    suspend fun getComments(
        @Path("postId") postId: String,
    ): CommentResponse

    @GET("get/communities/posts")
    @Api(ApiType.COMMUNITY)
    suspend fun getPosts(
        @Header("Authorization") token: String,
        @Path("communityId") communityId: String,
    ): PostResponse

    @GET("get/joined/posts")
    @Api(ApiType.COMMUNITY)
    suspend fun getJoinedPosts(
        @Header("Authorization") token: String,
    ): PostResponse

}