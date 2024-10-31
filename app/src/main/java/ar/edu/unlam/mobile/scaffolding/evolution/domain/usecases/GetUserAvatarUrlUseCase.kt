package ar.edu.unlam.mobile.scaffolding.evolution.domain.usecases

import ar.edu.unlam.mobile.scaffolding.evolution.data.repository.SuperHeroRepository
import javax.inject.Inject

class GetUserAvatarUrlUseCase
    @Inject
    constructor(
        private val repository: SuperHeroRepository,
    ) {
        suspend operator fun invoke(): String = repository.getUserDataAvatarUrl()
    }
