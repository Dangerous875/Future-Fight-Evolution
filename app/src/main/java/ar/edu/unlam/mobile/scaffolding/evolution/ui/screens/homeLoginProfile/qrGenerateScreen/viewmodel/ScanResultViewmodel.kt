package ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.qrGenerateScreen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffolding.evolution.data.database.UserRanked
import ar.edu.unlam.mobile.scaffolding.evolution.domain.usecases.GetUserIDFromUserDataUseCase
import ar.edu.unlam.mobile.scaffolding.evolution.domain.usecases.GetUserRankedByUserIDUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanResultViewModel
    @Inject
    constructor(
        private val getUserIDFromUserDataUseCase: GetUserIDFromUserDataUseCase,
        private val getUserRankedByUserIDUseCase: GetUserRankedByUserIDUseCase,
    ) : ViewModel() {
        private val _user = MutableStateFlow<UserRanked?>(null)
        val user: StateFlow<UserRanked?> = _user

        fun setScanResult(result: String?) {
            result?.let { userDataId ->
                viewModelScope.launch {
                    getUserIDFromUserDataUseCase(userDataId).collect { userId ->
                        if (userId != null) {
                            getUserRankedByUserIDUseCase(userId).collect { userRanked ->

                                _user.value = userRanked
                            }
                        } else {
                            Log.d("ScanResultViewModel", "UserID no encontrado")
                        }
                    }
                }
            }
        }
    }
