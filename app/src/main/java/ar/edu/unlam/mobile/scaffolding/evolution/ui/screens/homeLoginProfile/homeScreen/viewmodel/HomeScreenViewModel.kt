package ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.homeScreen.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffolding.evolution.domain.usecases.CanAccessToAppUseCase
import ar.edu.unlam.mobile.scaffolding.evolution.domain.usecases.GetUserDataFromFireStoreUseCase
import ar.edu.unlam.mobile.scaffolding.evolution.domain.usecases.GetWallpaperLogosUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel
    @Inject
    constructor(
        private val getWallpaperLogosUseCase: GetWallpaperLogosUseCase,
        firebaseAuth: FirebaseAuth,
        private val getUserDataFromFireStoreUseCase: GetUserDataFromFireStoreUseCase,
        private val canAccessToAppUseCase: CanAccessToAppUseCase,
    ) : ViewModel() {
        private val _blockVersion = MutableStateFlow(false)
        val blockVersion = _blockVersion.asStateFlow()

        private val _isLoading = MutableStateFlow(true)
        val isLoading = _isLoading.asStateFlow()

        private val _logos = MutableStateFlow(0)
        val logos = _logos.asStateFlow()

        private val _auth = MutableStateFlow(firebaseAuth)
        val auth = _auth.asStateFlow()

        init {
            viewModelScope.launch {
                checkUserVersion()
                val logosList = getWallpaperLogosUseCase()
                _logos.value = logosList[0]
                _isLoading.value = _logos.value == 0
                Log.i("LOGOSREADY", "$logosList")
                if (_auth.value.currentUser != null) {
                    initUserData()
                }
                initRandomLogo(logosList)
            }
        }

        private suspend fun initRandomLogo(logosList: List<Int>) {
            while (true) {
                var randomLogo: Int
                do {
                    randomLogo = logosList.random()
                } while (randomLogo == _logos.value)
                delay(3000)
                _logos.value = randomLogo
            }
        }

        private fun checkUserVersion() {
            viewModelScope.launch {
                val result =
                    withContext(Dispatchers.IO) {
                        canAccessToAppUseCase()
                    }
                _blockVersion.value = !result
            }
        }

        private suspend fun initUserData() {
            withContext(Dispatchers.IO) {
                getUserDataFromFireStoreUseCase()
            }
        }

        override fun onCleared() {
            super.onCleared()
            viewModelScope.cancel()
        }

        fun navigateToPlayStore(context: Context) {
            val appPackage = context.packageName
            val intent =
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$appPackage"),
                ).apply {
                    if (resolveActivity(context.packageManager) == null) {
                        data = Uri.parse("https://play.google.com/store/apps/details?id=$appPackage")
                    }
                }
            context.startActivity(intent)
        }
    }
