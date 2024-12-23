package ar.edu.unlam.mobile.scaffolding.evolution.data.network.service

import ar.edu.unlam.mobile.scaffolding.evolution.data.local.SuperHeroItem
import ar.edu.unlam.mobile.scaffolding.evolution.data.network.SuperHeroApiClient
import javax.inject.Inject

class SuperHeroService
    @Inject
    constructor(
        private val superHeroApiClient: SuperHeroApiClient,
    ) {
        suspend fun getSuperHeroList(query: String): List<SuperHeroItem> {
            val response = superHeroApiClient.getHeroByName(query)
            return response.body()?.superheroes ?: emptyList()
        }
    }
