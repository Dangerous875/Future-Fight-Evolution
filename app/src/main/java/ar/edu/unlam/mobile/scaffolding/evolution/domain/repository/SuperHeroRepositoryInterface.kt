package ar.edu.unlam.mobile.scaffolding.evolution.domain.repository

import ar.edu.unlam.mobile.scaffolding.evolution.data.database.UserData
import ar.edu.unlam.mobile.scaffolding.evolution.data.database.UserRanked
import ar.edu.unlam.mobile.scaffolding.evolution.data.local.Background
import ar.edu.unlam.mobile.scaffolding.evolution.data.local.ResultDataScreen
import ar.edu.unlam.mobile.scaffolding.evolution.data.local.SuperHeroItem
import ar.edu.unlam.mobile.scaffolding.evolution.domain.model.SuperHeroCombat
import kotlinx.coroutines.flow.Flow

interface SuperHeroRepositoryInterface {
    suspend fun getSuperHeroListByName(query: String): List<SuperHeroItem>

    fun getHeroDetail(): SuperHeroItem?

    fun setHeroDetail(hero: SuperHeroItem)

    fun getCombatBackGround(): List<Background>

    fun getResultDataScreen(): ResultDataScreen

    fun setResultDataScreen(
        superHeroPlayer: SuperHeroCombat,
        superHeroCom: SuperHeroCombat,
        lifePlayer: Int,
        lifeCom: Int,
    )

    suspend fun getUserByIdFromFireStore(userId: String): Flow<UserRanked?>

    suspend fun getAllUsersFromFireStore(): Flow<List<UserRanked>>

    suspend fun addUserFireStore(user: UserRanked)

    suspend fun updateUserRankingFireStore(user: UserRanked)

    suspend fun getUserDataFromFireStore(): Flow<UserData>

    suspend fun setUserDataFromFireStore(userData: UserData)

    suspend fun addUserDataFireStore(user: UserData)

    suspend fun getUserDataAvatarUrl(): String

    suspend fun getCurrentVersion(): List<Int>

    suspend fun minAllowedVersion(): List<Int>

    suspend fun getSpecialEvent(): String
}
