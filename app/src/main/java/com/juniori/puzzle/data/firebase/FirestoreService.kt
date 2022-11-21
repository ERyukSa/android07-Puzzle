package com.juniori.puzzle.data.firebase

import com.juniori.puzzle.data.firebase.dto.ListDocumentsResponseDTO
import com.juniori.puzzle.data.firebase.dto.RunQueryRequestDTO
import com.juniori.puzzle.data.firebase.dto.RunQueryResponseDTO
import com.juniori.puzzle.data.firebase.dto.VideoDetail
import com.juniori.puzzle.data.firebase.dto.VideoItem
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FirestoreService {
    @GET("databases/(default)/documents/videoReal")
    suspend fun listVideoItemDocuments(
        @Query("pageSize") pageSize: Int,
        @Query("pageToken") pageToken: String,
        @Query("orderBy") orderBy: String
    ): ListDocumentsResponseDTO

    @POST("databases/(default)/documents/videoReal")
    suspend fun createVideoItemDocument(
        @Body fields: Map<String, VideoDetail>
    ): VideoItem

    @POST("databases/(default)/documents:runQuery")
    suspend fun getFirebaseItemByQuery(
        @Body fields: RunQueryRequestDTO
    ): List<RunQueryResponseDTO>
}
