package com.bigint.multimodulesample.repositories

import com.bigint.network.ApiOperation
import com.bigint.network.KtorClient
import com.bigint.network.models.domain.CharacterDto
import com.bigint.network.models.domain.CharacterPage
import javax.inject.Inject

class CharacterRepository @Inject constructor(private val ktorClient: KtorClient) {
    suspend fun fetchCharacterPage(page: Int): ApiOperation<CharacterPage> {
        return ktorClient.getCharacterByPage(pageNumber = page)
    }

    suspend fun fetchCharacter(characterId: Int): ApiOperation<CharacterDto> {
        return ktorClient.getCharacter(characterId)
    }
}