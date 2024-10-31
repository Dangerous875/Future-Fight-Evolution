package ar.edu.unlam.mobile.scaffolding.evolution.domain.usecases

import ar.edu.unlam.mobile.scaffolding.evolution.data.repository.SuperHeroRepository
import ar.edu.unlam.mobile.scaffolding.evolution.domain.model.CurrentUserProvider
import javax.inject.Inject

class GetUserDataFromFireStoreUseCase
    @Inject
    constructor(
        private val repository: SuperHeroRepository,
        private val currentUserProvider: CurrentUserProvider,
    ) {
        suspend operator fun invoke() {
            val userFireStore = repository.getUserDataFromFireStore()
            userFireStore.collect {
                currentUserProvider.currentUser = it
            }
        }
    }
