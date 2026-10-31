package com.example.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CharacterResponse(
    @Json(name = "characters") val characters: List<NinjaCharacter> = emptyList(),
    @Json(name = "currentPage") val currentPage: Int? = 1,
    @Json(name = "pageSize") val pageSize: Int? = 20,
    @Json(name = "total") val total: Int? = 0
)

@JsonClass(generateAdapter = true)
data class NinjaCharacter(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "images") val images: List<String> = emptyList(),
    @Json(name = "debut") val debut: DebutInfo? = null,
    @Json(name = "family") val family: Map<String, String>? = null,
    @Json(name = "jutsu") val jutsu: List<String> = emptyList(),
    @Json(name = "natureType") val natureType: List<String> = emptyList(),
    @Json(name = "personal") val personal: PersonalInfo? = null,
    @Json(name = "tools") val tools: List<String> = emptyList()
) {
    fun getPrimaryImage(): String? {
        return images.firstOrNull { it.isNotBlank() }
    }

    fun getVillage(): String {
        val aff = personal?.affiliation
        return when (aff) {
            is List<*> -> aff.filterIsInstance<String>().firstOrNull() ?: "Desconhecida"
            is String -> aff
            else -> "Konohagakure"
        }
    }

    fun getClanName(): String {
        val clan = personal?.clan
        return when (clan) {
            is List<*> -> clan.filterIsInstance<String>().firstOrNull() ?: "Sem Clã Registrado"
            is String -> clan
            else -> "Sem Clã Registrado"
        }
    }

    fun getKekkeiGenkai(): List<String> {
        val kg = personal?.kekkeiGenkai
        return when (kg) {
            is List<*> -> kg.filterIsInstance<String>()
            is String -> listOf(kg)
            else -> emptyList()
        }
    }

    fun getAffiliations(): List<String> {
        val aff = personal?.affiliation
        return when (aff) {
            is List<*> -> aff.filterIsInstance<String>()
            is String -> listOf(aff)
            else -> emptyList()
        }
    }
}

@JsonClass(generateAdapter = true)
data class DebutInfo(
    @Json(name = "manga") val manga: String? = null,
    @Json(name = "anime") val anime: String? = null,
    @Json(name = "novel") val novel: String? = null,
    @Json(name = "movie") val movie: String? = null,
    @Json(name = "game") val game: String? = null,
    @Json(name = "appearsIn") val appearsIn: String? = null
)

@JsonClass(generateAdapter = true)
data class PersonalInfo(
    @Json(name = "birthdate") val birthdate: String? = null,
    @Json(name = "sex") val sex: String? = null,
    @Json(name = "status") val status: String? = null,
    @Json(name = "classification") val classification: Any? = null,
    @Json(name = "occupation") val occupation: Any? = null,
    @Json(name = "affiliation") val affiliation: Any? = null,
    @Json(name = "team") val team: Any? = null,
    @Json(name = "clan") val clan: Any? = null,
    @Json(name = "kekkeiGenkai") val kekkeiGenkai: Any? = null,
    @Json(name = "titles") val titles: Any? = null
)

@JsonClass(generateAdapter = true)
data class ClanResponse(
    @Json(name = "clans") val clans: List<ClanItem> = emptyList()
)

@JsonClass(generateAdapter = true)
data class ClanItem(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "characters") val characters: List<Int> = emptyList()
)

@JsonClass(generateAdapter = true)
data class VillageResponse(
    @Json(name = "villages") val villages: List<VillageItem> = emptyList()
)

@JsonClass(generateAdapter = true)
data class VillageItem(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "characters") val characters: List<Int> = emptyList()
)
