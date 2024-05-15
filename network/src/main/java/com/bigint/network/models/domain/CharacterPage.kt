package com.bigint.network.models.domain

data class CharacterPage(
    val info: Info,
    val characters: List<CharacterDto>
) {
    data class Info(
        val count: Int,
        val pages: Int,
        val next: String?,
        val prev: String?
    )
}
