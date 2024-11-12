package ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.cameraScreen.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CameraScreenViewModel
    @Inject
    constructor() : ViewModel() {
        private val _permissionCamera = MutableStateFlow(false)
        val permissionCamera = _permissionCamera.asStateFlow()

        fun setPermissionCamera(granted: Boolean) {
            _permissionCamera.value = granted
        }
    }
