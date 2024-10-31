package ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.combatResult.superHeroCombatResultScreen.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffolding.evolution.data.database.LocationUser
import ar.edu.unlam.mobile.scaffolding.evolution.data.database.UserRanked
import ar.edu.unlam.mobile.scaffolding.evolution.data.local.ResultDataScreen
import ar.edu.unlam.mobile.scaffolding.evolution.domain.usecases.GetCurrentUserUseCase
import ar.edu.unlam.mobile.scaffolding.evolution.domain.usecases.GetResultDataScreenUseCase
import ar.edu.unlam.mobile.scaffolding.evolution.domain.usecases.GetUserAvatarUrlUseCase
import ar.edu.unlam.mobile.scaffolding.evolution.domain.usecases.UpdateUserRankingFireStore
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class CombatResultViewModel
    @Inject
    constructor(
        private val getResultDataScreen: GetResultDataScreenUseCase,
        private val updateUserRankingFireStore: UpdateUserRankingFireStore,
        private val firebaseAuth: FirebaseAuth,
        private val fusedLocationProviderClient: FusedLocationProviderClient,
        private val getCurrentUserUseCase: GetCurrentUserUseCase,
        private val getUserAvatarUrlUseCase: GetUserAvatarUrlUseCase,
    ) : ViewModel() {
        private val _result = MutableStateFlow<ResultDataScreen?>(null)
        val result = _result.asStateFlow()

        private val _isLoading = MutableStateFlow(true)
        val isLoading = _isLoading.asStateFlow()

        private val _playerWin = MutableStateFlow(false)
        val playerWin = _playerWin.asStateFlow()

        private val _permissionLocation = MutableStateFlow(false)
        val permissionLocation = _permissionLocation.asStateFlow()

        init {
            viewModelScope.launch {
                _result.value = getResultDataScreen()
                _playerWin.value = checkIfPlayerWin(_result.value!!)
                if (_playerWin.value && firebaseAuth.currentUser != null) {
                    updateUserRanking()
                }
                _isLoading.value = false
            }
        }

        private fun checkIfPlayerWin(resultDataScreen: ResultDataScreen): Boolean {
            val playerLife = resultDataScreen.resultDataScreen!!.superHeroPlayer.life
            val comLife = resultDataScreen.resultDataScreen!!.superHeroCom.life
            return playerLife > comLife
        }

        fun resetLife() {
            val resultData = _result.value ?: return
            resultData.resultDataScreen!!.superHeroPlayer.life = resultData.resultDataScreen!!.lifePlay
            resultData.resultDataScreen!!.superHeroCom.life = resultData.resultDataScreen!!.lifeCom
        }

        fun setPermissionCamera(granted: Boolean) {
            _permissionLocation.value = granted
        }

        private fun updateUserRanking() {
            viewModelScope.launch {
                val userID = firebaseAuth.currentUser?.uid ?: ""
                val userLocation = getLocation()
                val imageUrl = getUserAvatarUrlUseCase()
                Log.i("LOCATIONRULES", "$userLocation")
                val userRanked =
                    UserRanked(
                        userID = firebaseAuth.currentUser?.uid,
                        userName = getCurrentUserUseCase().nickname!!,
                        userLocation =
                            LocationUser(
                                latitude = userLocation!!.latitude,
                                longitude = userLocation.longitude,
                            ),
                        userVictories = 1,
                        avatarUrl = imageUrl,
                    )
                updateUserRankingFireStore(userRanked)
            }
        }

        @SuppressLint("MissingPermission")
        private suspend fun getLocation(): LocationUser? =
            try {
                val location = fusedLocationProviderClient.lastLocation.await()
                location?.let {
                    LocationUser(latitude = it.latitude, longitude = it.longitude)
                }
            } catch (e: Exception) {
                null
            }
    }
