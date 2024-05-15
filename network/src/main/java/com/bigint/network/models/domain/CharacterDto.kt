package com.bigint.network.models.domain

data class CharacterDto(
    val created: String,
    val episodeIds: List<Int>,
    val gender: CharacterGender,
    val id: Int,
    val imageUrl: String,
    val location: Location,
    val name: String,
    val origin: Origin,
    val species: String,
    val status: CharacterStatus,
    val type: String
)