package ar.edu.unlam.mobile.scaffolding.evolution.domain.usecases

import ar.edu.unlam.mobile.scaffolding.evolution.data.repository.SuperHeroRepository
import javax.inject.Inject

class CanAccessToAppUseCase
    @Inject
    constructor(
        private val repository: SuperHeroRepository,
    ) {
        suspend operator fun invoke(): Boolean {
            val currentVersion = repository.getCurrentVersion()
            val minAllowedVersion = repository.minAllowedVersion()

            for ((currentPart, minVersionPart) in currentVersion.zip(minAllowedVersion)) {
                if (currentPart != minVersionPart) {
                    return currentPart > minVersionPart
                }
            }
            return true
        }
    }
