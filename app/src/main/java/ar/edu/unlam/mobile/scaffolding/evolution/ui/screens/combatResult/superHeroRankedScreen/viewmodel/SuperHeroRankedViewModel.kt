package ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.combatResult.superHeroRankedScreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffolding.evolution.data.database.UserRanked
import ar.edu.unlam.mobile.scaffolding.evolution.domain.usecases.GetUsersRankingFireStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SuperHeroRankedViewModel
    @Inject
    constructor(
        private val getUsersRankingFireStore: GetUsersRankingFireStore,
    ) : ViewModel() {
        private val _usersRanked = MutableStateFlow<List<UserRanked>>(emptyList())
        val usersRanked = _usersRanked.asStateFlow()

        private val _isLoading = MutableStateFlow(true)
        val isLoading = _isLoading.asStateFlow()

        init {
            viewModelScope.launch {
                val usersFlow: Flow<List<UserRanked>> = getUsersRankingFireStore()
                usersFlow.collect { users ->
                    _usersRanked.value = users.sortedByDescending { it.userVictories }
                    _isLoading.value = false
                }
            }
        }
    }
