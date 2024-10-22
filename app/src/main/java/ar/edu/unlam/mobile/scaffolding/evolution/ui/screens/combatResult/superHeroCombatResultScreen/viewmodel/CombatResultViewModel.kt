package ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.combatResult.superHeroCombatResultScreen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffolding.R
import ar.edu.unlam.mobile.scaffolding.evolution.data.database.ResultDataScreen
import ar.edu.unlam.mobile.scaffolding.evolution.domain.usecases.GetResultDataScreenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CombatResultViewModel
    @Inject
    constructor(
        private val getResultDataScreen: GetResultDataScreenUseCase,
    ) : ViewModel() {
        private val _result = MutableStateFlow<ResultDataScreen?>(null)
        val result = _result.asStateFlow()
        private val _isLoading = MutableStateFlow(true)
        val isLoading = _isLoading.asStateFlow()
        private val _playerWin = MutableStateFlow(false)
        val playerWin = _playerWin.asStateFlow()
        private val _resultImageRes = MutableStateFlow(R.drawable.iv_defeated)
        val resultImageRes = _resultImageRes.asStateFlow()
        private val winnerImageRes = R.drawable.iv_gold_trophy
        private val loserImageRes = R.drawable.iv_defeated

        init {
            viewModelScope.launch {
                _result.value = getResultDataScreen()
                _playerWin.value = checkIfPlayerWin(getResultDataScreen())
                Log.i("result2", _playerWin.value.toString())
                _resultImageRes.value =
                    if (_playerWin.value) {
                        R.drawable.im_ganador
                    } else {
                        R.drawable.iv_defeated
                    }

                _isLoading.value = false
            }
        }

        private fun checkIfPlayerWin(resultDataScreen: ResultDataScreen): Boolean {
            val playerLife = resultDataScreen.resultDataScreen!!.superHeroPlayer.life
            val comLife = resultDataScreen.resultDataScreen!!.superHeroCom.life
            return playerLife > comLife
        }

        fun getPlayerResultImageRes(): Int =
            if (playerWin.value) {
                winnerImageRes
            } else {
                loserImageRes
            }

        fun getComResultImageRes(): Int =
            if (playerWin.value) {
                loserImageRes
            } else {
                winnerImageRes
            }

        fun resetLife() {
            val resultData = _result.value ?: return
            resultData.resultDataScreen!!.superHeroPlayer.life = resultData.resultDataScreen!!.lifePlay
            resultData.resultDataScreen!!.superHeroCom.life = resultData.resultDataScreen!!.lifeCom
        }
    }
