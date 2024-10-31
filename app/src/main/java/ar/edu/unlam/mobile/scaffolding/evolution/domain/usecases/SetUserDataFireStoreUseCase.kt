package ar.edu.unlam.mobile.scaffolding.evolution.domain.usecases

import ar.edu.unlam.mobile.scaffolding.evolution.data.database.UserData
import ar.edu.unlam.mobile.scaffolding.evolution.data.repository.SuperHeroRepository
import javax.inject.Inject

class SetUserDataFireStoreUseCase
    @Inject
    constructor(
        private val repository: SuperHeroRepository,
    ) {
        suspend operator fun invoke(userData: UserData) {
            repository.setUserDataFromFireStore(userData)
        }
    }
