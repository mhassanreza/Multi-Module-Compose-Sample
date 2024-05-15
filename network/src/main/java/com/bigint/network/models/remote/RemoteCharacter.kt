package com.bigint.network.models.remote

import com.bigint.network.models.domain.CharacterDto
import com.bigint.network.models.domain.CharacterGender
import com.bigint.network.models.domain.CharacterStatus
import com.bigint.network.models.domain.Location
import com.bigint.network.models.domain.Origin
import kotlinx.serialization.Serializable

@Serializable
data class RemoteCharacter(
    val created: String,
    val episode: List<String>,
    val gender: String,
    val id: Int,
    val image: String,
    val location: Location,
    val name: String,
    val origin: Origin,
    val species: String,
    val status: String,
    val type: String,
    val url: String
) {
    @Serializable
    data class Location(
        val name: String,
        val url: String
    )

    @Serializable
    data class Origin(
        val name: String,
        val url: String
    )
}

fun RemoteCharacter.toDomainCharacter(): CharacterDto {
    val characterGender = when (gender.lowercase()) {
        "female" -> CharacterGender.Female
        "male" -> CharacterGender.Male
        "genderless" -> CharacterGender.Genderless
        else -> CharacterGender.Unknown
    }
    val characterStatus = when (status.lowercase()) {
        "alive" -> CharacterStatus.Alive
        "dead" -> CharacterStatus.Dead
        else -> CharacterStatus.Unknown
    }
    return CharacterDto(
        created = created,
        episodeIds = episode.map { it.substring(it.lastIndexOf("/") + 1).toInt() },
        gender = characterGender,
        id = id,
        imageUrl = image,
        location = Location(name = location.name, url = location.url),
        name = name,
        origin = Origin(name = origin.name, url = origin.url),
        species = species,
        status = characterStatus,
        type = type
    )
}