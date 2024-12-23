package ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.combatResult.superHeroCombatScreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffolding.R
import ar.edu.unlam.mobile.scaffolding.evolution.data.local.Background
import ar.edu.unlam.mobile.scaffolding.evolution.domain.model.SuperHeroCombat
import ar.edu.unlam.mobile.scaffolding.evolution.domain.usecases.GetCombatDataScreenUseCase
import ar.edu.unlam.mobile.scaffolding.evolution.domain.usecases.GetCurrentUserUseCase
import ar.edu.unlam.mobile.scaffolding.evolution.domain.usecases.SetResultDataScreenUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.min
import kotlin.math.roundToInt

lateinit var superHero1: SuperHeroCombat
lateinit var superHero2: SuperHeroCombat
lateinit var backgroundData: Background

@HiltViewModel
class CombatViewModel
    @Inject
    constructor(
        getCombatDataScreenUseCase: GetCombatDataScreenUseCase,
        private val setResultDataScreen: SetResultDataScreenUseCase,
        private val getCurrentUserUseCase: GetCurrentUserUseCase,
        private val firebaseAuth: FirebaseAuth,
    ) : ViewModel() {
        private val _nickName = MutableStateFlow("")
        val nickName = _nickName.asStateFlow()
        private var _superHeroPlayer = MutableStateFlow<SuperHeroCombat?>(null)
        val superHeroPlayer = _superHeroPlayer.asStateFlow()
        private var _superHeroCom = MutableStateFlow<SuperHeroCombat?>(null)
        val superHeroCom = _superHeroCom.asStateFlow()
        private var _background = MutableStateFlow<Background?>(null)
        val background = _background.asStateFlow()
        private var _buttonEnable = MutableStateFlow(true)
        val buttonEnable = _buttonEnable.asStateFlow()
        private var _attackEffect = MutableStateFlow(false)
        val attackEffect = _attackEffect.asStateFlow()
        private var _audioAttack = MutableStateFlow(R.raw.raw_attack1)
        val audioAttack = _audioAttack.asStateFlow()
        private var _isLoading = MutableStateFlow(true)
        val isLoading = _isLoading.asStateFlow()
        private var _roundCount = MutableStateFlow(1)
        val roundCount = _roundCount.asStateFlow()
        var lifePlayer = ""
        var lifeCom = ""
        private val _navigationDone = MutableStateFlow(false)
        val navigationDone = _navigationDone.asStateFlow()
        private var _attackPlayer = MutableStateFlow(true)
        val attackPlayer = _attackPlayer.asStateFlow()
        private var _vibratorActivated = MutableStateFlow(false)
        val vibratorActivated = _vibratorActivated.asStateFlow()

        private var _iconButtonPotion = MutableStateFlow(true)
        val iconButtonPotion = _iconButtonPotion.asStateFlow()
        private var _iconButtonPowerUp = MutableStateFlow(true)
        val iconButtonPowerUp = _iconButtonPowerUp.asStateFlow()
        private var _iconButtonDefensive = MutableStateFlow(true)
        val iconButtonDefensive = _iconButtonDefensive.asStateFlow()

        private var _iconButtonPotionCom = MutableStateFlow(true)
        val iconButtonPotionCom = _iconButtonPotionCom.asStateFlow()
        private var _iconButtonPowerUpCom = MutableStateFlow(true)
        val iconButtonPowerUpCom = _iconButtonPowerUpCom.asStateFlow()
        private var _iconButtonDefensiveCom = MutableStateFlow(true)
        val iconButtonDefensiveCom = _iconButtonDefensiveCom.asStateFlow()
        private var _playerDefenseActivated = MutableStateFlow(false)
        val playerDefenseActivated = _playerDefenseActivated.asStateFlow()
        private var _comDefenseActivated = MutableStateFlow(false)
        val comDefenseActivated = _comDefenseActivated.asStateFlow()
        private var _playerAttackActivated = MutableStateFlow(false)
        val playerAttackActivated = _playerAttackActivated.asStateFlow()
        private var _comAttackActivated = MutableStateFlow(false)
        val comAttackActivated = _comAttackActivated.asStateFlow()
        private var _playerHealingActivated = MutableStateFlow(false)
        val playerHealingActivated = _playerHealingActivated.asStateFlow()
        private var _comHealingActivated = MutableStateFlow(false)
        val comHealingActivated = _comHealingActivated.asStateFlow()

        init {
            val combatDataScreen = getCombatDataScreenUseCase()
            _isLoading.value = true
            viewModelScope.launch {
                _nickName.value =
                    if (firebaseAuth.currentUser != null) {
                        getCurrentUserUseCase().nickname!!
                    } else {
                        "Player"
                    }
                delay(8000)
                superHero1 = combatDataScreen.playerCharacter!!
                superHero2 = combatDataScreen.comCharacter!!
                backgroundData = combatDataScreen.background!!
                _superHeroPlayer.value = superHero1
                _superHeroCom.value = superHero2
                _background.value = backgroundData
                lifePlayer = superHero1.life.toString()
                lifeCom = superHero2.life.toString()
                _isLoading.value = false
            }
        }

        fun markNavigationDone() {
            _navigationDone.value = true
        }

        fun initAttack() {
            _attackEffect.value = true
            _buttonEnable.value = false
            viewModelScope.launch {
                attackPlayer()
                delay(2500)
                _attackPlayer.value = false
                getRandomAudioAttack()
                attackCom()
                delay(2500)
                _buttonEnable.value = true
                _attackEffect.value = false
                _attackPlayer.value = true
                _roundCount.value += 1
            }
        }

        private fun attackCom() {
            var lifePlayer = superHero1.life
            val damageAbsPlayer = superHero1.damageAbs
            val attackCom = superHero2.attack.minus(superHero2.damagePenance)
            lifePlayer = lifePlayer.minus(attackCom.minus(damageAbsPlayer))
            superHero1.life = lifePlayer
            _superHeroPlayer.value = superHero1
            initSpecialCom()
        }

        private fun initSpecialCom() {
            val iaAttack = (1..5).random()
            if (iaAttack == 3) {
                specialAttackCom()
            }
            val iaDefense = (1..5).random()
            if (iaDefense == 3) {
                specialDefenseCom()
            }
            if (superHero2.life <= (lifeCom.toInt().div(2))) {
                healingPotionCom(lifeCom.toInt())
            }
            if (superHero2.life < 80) {
                specialAttackCom()
                specialDefenseCom()
            }
        }

        private fun attackPlayer() {
            var lifeCom = superHero2.life
            val damageAbsCom = superHero2.damageAbs
            val attackPlayer = superHero1.attack.minus(superHero1.damagePenance)
            lifeCom = lifeCom.minus(attackPlayer.minus(damageAbsCom))
            superHero2.life = lifeCom
            _superHeroCom.value = superHero2
        }

        fun healingPotion(lifePlayerTotal: Int) {
            _iconButtonPotion.value = false
            _playerHealingActivated.value = true
            viewModelScope.launch {
                delay(200)
                var lifePlayer = superHero1.life
                val healingPoint = superHero1.healingPotion
                lifePlayer = min((lifePlayer.plus(healingPoint)), lifePlayerTotal)
                superHero1.life = lifePlayer
                _superHeroPlayer.value = superHero1
                _playerHealingActivated.value = false
            }
        }

        fun specialAttack() {
            _iconButtonPowerUp.value = false
            _playerAttackActivated.value = true
            viewModelScope.launch {
                val attackAttribute = superHero1.attack
                val attackEnhanced = attackAttribute.times(2.6)
                _superHeroPlayer.value!!.attack = attackEnhanced.roundToInt()
                delay(13000)
                superHero1.attack = attackAttribute
                _superHeroPlayer.value = superHero1
                _playerAttackActivated.value = false
            }
        }

        fun specialDefense() {
            _iconButtonDefensive.value = false
            _playerDefenseActivated.value = true
            viewModelScope.launch {
                val attackComAttribute = superHero2.attack
                val damageAbsAttribute = superHero1.damageAbs
                _superHeroPlayer.value!!.damageAbs = attackComAttribute
                _superHeroPlayer.value!!.defense += 100
                delay(13000)
                _superHeroPlayer.value!!.defense -= 100
                _superHeroPlayer.value!!.damageAbs = damageAbsAttribute
                _playerDefenseActivated.value = false
            }
        }

        private fun healingPotionCom(lifeComTotal: Int) {
            viewModelScope.launch {
                if (iconButtonPotionCom.value) {
                    _comHealingActivated.value = true
                    delay(1000)
                    var lifePlayer = superHero2.life
                    val healingPoint = superHero2.healingPotion
                    lifePlayer = min((lifePlayer.plus(healingPoint)), lifeComTotal)
                    superHero2.life = lifePlayer
                    _superHeroCom.value = superHero2
                }
                _comHealingActivated.value = false
            }
            _iconButtonPotionCom.value = false
        }

        private fun specialAttackCom() {
            viewModelScope.launch {
                if (iconButtonPowerUpCom.value) {
                    _vibratorActivated.value = true
                    _comAttackActivated.value = true
                    val attackAttribute = superHero2.attack
                    val attackEnhanced = attackAttribute.times(2.75)
                    _superHeroCom.value!!.attack = attackEnhanced.roundToInt()
                    delay(11000)
                    _vibratorActivated.value = false
                    superHero2.attack = attackAttribute
                    _superHeroCom.value = superHero2
                }
                _comAttackActivated.value = false
            }
            _iconButtonPowerUpCom.value = false
        }

        private fun specialDefenseCom() {
            viewModelScope.launch {
                if (iconButtonDefensiveCom.value) {
                    _comDefenseActivated.value = true
                    val attackPlayerAttribute = superHero1.attack
                    val damageAbsAttribute = superHero2.damageAbs
                    _superHeroCom.value!!.damageAbs = attackPlayerAttribute
                    _superHeroCom.value!!.defense += 100
                    delay(14000)
                    _superHeroCom.value!!.defense -= 100
                    _superHeroCom.value!!.damageAbs = damageAbsAttribute
                }
                _comDefenseActivated.value = false
            }
            _iconButtonDefensiveCom.value = false
        }

        fun getRandomAudioAttack() {
            val audio =
                listOf(
                    R.raw.raw_attack1,
                    R.raw.raw_attack2,
                    R.raw.raw_attack3,
                    R.raw.raw_attack4,
                    R.raw.raw_attack5,
                )
            var randomAudio: Int
            do {
                randomAudio = audio.random()
            } while (randomAudio == _audioAttack.value)
            _audioAttack.value = randomAudio
        }

        fun setDataScreenResult(
            superHeroPlayer: SuperHeroCombat,
            superHeroCombat: SuperHeroCombat,
        ) {
            setResultDataScreen(superHeroPlayer, superHeroCombat, lifePlayer.toInt(), lifeCom.toInt())
        }
    }
