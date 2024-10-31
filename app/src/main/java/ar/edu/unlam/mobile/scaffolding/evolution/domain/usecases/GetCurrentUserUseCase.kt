package ar.edu.unlam.mobile.scaffolding.evolution.domain.usecases

import ar.edu.unlam.mobile.scaffolding.evolution.data.database.UserData
import ar.edu.unlam.mobile.scaffolding.evolution.domain.model.CurrentUserProvider
import javax.inject.Inject

class GetCurrentUserUseCase
    @Inject
    constructor(
        private val currentUserProvider: CurrentUserProvider,
    ) {
        operator fun invoke(): UserData = currentUserProvider.currentUser!!
    }
