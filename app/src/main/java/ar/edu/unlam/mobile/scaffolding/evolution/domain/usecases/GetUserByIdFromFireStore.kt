package ar.edu.unlam.mobile.scaffolding.evolution.domain.usecases

import ar.edu.unlam.mobile.scaffolding.evolution.data.database.UserRanked
import ar.edu.unlam.mobile.scaffolding.evolution.data.repository.SuperHeroRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserByIdFromFireStore
    @Inject
    constructor(
        private val repository: SuperHeroRepository,
    ) {
        suspend operator fun invoke(userId: String): Flow<UserRanked?> = repository.getUserByIdFromFirestore(userId)
    }
