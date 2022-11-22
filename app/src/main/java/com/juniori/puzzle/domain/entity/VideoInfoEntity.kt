package com.juniori.puzzle.domain.entity

data class VideoInfoEntity(
    val ownerUid: String,
    val videoUrl: String,
    val thumbnailUrl: String,
    val isPrivate: Boolean,
    val likedCount: Int,
    val likedUserUidList: List<String>,
    val updateTime: Long,
    val location: String,
    val memo: String
)
