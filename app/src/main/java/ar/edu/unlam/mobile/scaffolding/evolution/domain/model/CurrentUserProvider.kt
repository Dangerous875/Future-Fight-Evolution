package ar.edu.unlam.mobile.scaffolding.evolution.domain.model

import ar.edu.unlam.mobile.scaffolding.evolution.data.database.UserData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentUserProvider
    @Inject
    constructor() {
        var currentUser: UserData? = null
    }
