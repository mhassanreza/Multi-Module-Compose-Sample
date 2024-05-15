package com.bigint.network

import com.bigint.network.models.domain.CharacterDto
import com.bigint.network.models.domain.CharacterPage
import com.bigint.network.models.domain.Episode
import com.bigint.network.models.remote.RemoteCharacter
import com.bigint.network.models.remote.RemoteCharacterPage
import com.bigint.network.models.remote.RemoteEpisode
import com.bigint.network.models.remote.toDomainCharacter
import com.bigint.network.models.remote.toDomainCharacterPage
import com.bigint.network.models.remote.toDomainEpisode
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class KtorClient {
    private val client = HttpClient(OkHttp) {
        defaultRequest { url(ApiURL.BASE_URL) }
        install(Logging) {
            logger = Logger.SIMPLE
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    private var characterCache = mutableMapOf<Int, CharacterDto>()

    suspend fun getCharacter(id: Int): ApiOperation<CharacterDto> {
        characterCache[id]?.let { return ApiOperation.Success(it) }

        val url = "${ApiURL.URL_CHARACTER}/$id"
        return safeApiCall {
            client.get(url)
                .body<RemoteCharacter>()
                .toDomainCharacter()
                .also { characterCache[id] = it }
        }
    }

    suspend fun getCharacterByPage(pageNumber: Int): ApiOperation<CharacterPage> {
        return safeApiCall {
            client.get("character/?page=$pageNumber")
                .body<RemoteCharacterPage>()
                .toDomainCharacterPage()
        }
    }

    private suspend fun getEpisode(episodeId: Int): ApiOperation<Episode> {
        val url = "${ApiURL.URL_EPISODE}/$episodeId"
        return safeApiCall {
            client.get(url)
                .body<RemoteEpisode>()
                .toDomainEpisode()
        }
    }

    suspend fun getEpisodes(ids: List<Int>): ApiOperation<List<Episode>> {
        return if (ids.size == 1) {
            getEpisode(episodeId = ids[0]).mapSuccess {
                listOf(it)
            }
        } else {
            val idsCommaSeparated = ids.joinToString(separator = ",")
            val url = "${ApiURL.URL_EPISODE}/$idsCommaSeparated"
            safeApiCall {
                client.get(url)
                    .body<List<RemoteEpisode>>()
                    .map { it.toDomainEpisode() }
            }
        }
    }

    private inline fun <T> safeApiCall(apiCall: () -> T): ApiOperation<T> {
        return try {
            ApiOperation.Success(data = apiCall())
        } catch (e: Exception) {
            ApiOperation.Failure(exception = e)
        }
    }
}

sealed interface ApiOperation<out T> {
    data class Success<out T>(val data: T) : ApiOperation<T>
    data class Failure<T>(val exception: Exception) : ApiOperation<T>

    fun <R> mapSuccess(transform: (T) -> R): ApiOperation<R> {
        return when (this) {
            is Success -> Success(transform(data))
            is Failure -> Failure(exception)
        }
    }

    fun onSuccess(block: (T) -> Unit): ApiOperation<T> {
        if (this is Success) block(data)
        return this
    }

    fun onFailure(block: (Exception) -> Unit): ApiOperation<T> {
        if (this is Failure) block(exception)
        return this
    }

}