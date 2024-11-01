package ar.edu.unlam.mobile.scaffolding.evolution.domain.usecases

import android.util.Log
import androidx.compose.ui.graphics.Color
import ar.edu.unlam.mobile.scaffolding.evolution.data.local.WallpaperLogos
import ar.edu.unlam.mobile.scaffolding.evolution.data.repository.SuperHeroRepository
import ar.edu.unlam.mobile.scaffolding.evolution.ui.theme.ColorWay
import javax.inject.Inject

class GetWallpaperLogosUseCase
    @Inject
    constructor(
        private val repository: SuperHeroRepository,
        private val wallpaperLogos: WallpaperLogos,
    ) {
        suspend operator fun invoke(): List<Int> {
            val specialEvent = repository.getSpecialEvent()
            Log.i("LOGOSREADY", specialEvent)
            setColorWay(specialEvent)
            return getList(specialEvent)
        }

        private fun setColorWay(specialEvent: String) {
            ColorWay =
                when (specialEvent) {
                    "original" -> Color(0xFF009DDB)
                    "christmas" -> Color(0xFFCA1010)
                    "halloween" -> Color(0xFF652BCE)
                    "saintPatrick" -> Color(0xFF18C71F)
                    else -> {
                        Color(0xFF009DDB)
                    }
                }
        }

        private fun getList(specialEvent: String): List<Int> =
            when (specialEvent) {
                "original" -> wallpaperLogos.original.map { it.logo }
                "christmas" -> wallpaperLogos.christmas.map { it.logo }
                "halloween" -> wallpaperLogos.halloween.map { it.logo }
                "saintPatrick" -> wallpaperLogos.saintPatrick.map { it.logo }
                else -> {
                    Log.i("LOGOSREADY", "DEFAULT")
                    wallpaperLogos.original.map { it.logo }
                }
            }
    }
