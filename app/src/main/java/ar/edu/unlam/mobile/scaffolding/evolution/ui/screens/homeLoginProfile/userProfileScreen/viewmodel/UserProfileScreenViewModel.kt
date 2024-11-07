package ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.userProfileScreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffolding.evolution.data.database.UserData
import ar.edu.unlam.mobile.scaffolding.evolution.domain.model.UpdateUserDataDto
import ar.edu.unlam.mobile.scaffolding.evolution.domain.usecases.GetCurrentUserUseCase
import ar.edu.unlam.mobile.scaffolding.evolution.domain.usecases.GetUserAvatarUrlUseCase
import ar.edu.unlam.mobile.scaffolding.evolution.domain.usecases.SetUserDataFireStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileScreenViewModel
    @Inject
    constructor(
        private val getCurrentUserUseCase: GetCurrentUserUseCase,
        private val setUserDataFireStoreUseCase: SetUserDataFireStoreUseCase,
        private val getUserAvatarUrlUseCase: GetUserAvatarUrlUseCase,
    ) : ViewModel() {
        private val _showUpdateData = MutableStateFlow(false)
        val showUpdateData = _showUpdateData.asStateFlow()

        private val _isLoading = MutableStateFlow(true)
        val isLoading = _isLoading.asStateFlow()

        private var _userData = MutableStateFlow<UserData?>(null)
        val userData = _userData.asStateFlow()

        private var _avatarUrl = MutableStateFlow("")
        val avatarUrl = _avatarUrl.asStateFlow()

        init {
            viewModelScope.launch {
                _avatarUrl.value = getUserAvatarUrlUseCase()
                _userData.value = getCurrentUserUseCase()
                if (_userData.value != null) {
                    _isLoading.value = false
                }
            }
        }

        fun addUpdateData(
            name: String,
            nickname: String,
            infoUser: String,
        ) {
            val dto: UpdateUserDataDto? = prepareDataTransferObject(name, nickname, infoUser)
            val userUpdate: UserData? =
                _userData.value?.copy(
                    name = dto?.name,
                    nickname = dto?.nickname,
                    infoUser = dto?.infoUser,
                )
            userUpdate?.let {
                viewModelScope.launch {
                    setUserDataFireStoreUseCase(it)
                    _userData.value = it
                }
            }
            dismissUpdateDataSelected()
        }

        fun updateDataSelected() {
            _showUpdateData.value = true
        }

        fun dismissUpdateDataSelected() {
            _showUpdateData.value = false
        }

        private fun prepareDataTransferObject(
            name: String,
            nickname: String,
            infoUser: String,
        ): UpdateUserDataDto? =
            if (name.isBlank() || nickname.isBlank() || infoUser.isBlank()) {
                null
            } else {
                UpdateUserDataDto(name, nickname, infoUser)
            }

        fun updateAllData(
            newName: String,
            newNickname: String,
            newInfoUser: String,
        ) {
            val userUpdate = _userData.value?.copy(name = newName, nickname = newNickname, infoUser = newInfoUser)
            userUpdate?.let {
                _userData.value = it
                viewModelScope.launch {
                    setUserDataFireStoreUseCase(it)
                }
            }
        }

        fun updateNickName(newnickname: String) {
            // Aca agarras el _userData.value? actual y haces una copia con los datos nuevos
            val userUpdate = _userData.value?.copy(nickname = newnickname)

            userUpdate?.let {
                viewModelScope.launch {
                    // Actualizas la DB en el Hilo secundario
                    setUserDataFireStoreUseCase(it)
                    // Actualizas la vista manualmente para no tener que esperar otra llamada de la BD
                    // La proxima ves que se recargue la pantalla si va a trar los datos actualizados
                    _userData.value = it // Asignamos el nuevo valor a _userData
                }
            }
        }
    }
