package ar.edu.unlam.mobile.scaffolding.evolution.data.local

import ar.edu.unlam.mobile.scaffolding.R
import javax.inject.Inject

data class Background(
    val name: String,
    val background: Int,
    val theme: Int,
)

class CombatBackgroundsData
    @Inject
    constructor() {
        val combatBackgroundsData =
            listOf(
                Background("Arena", R.drawable.iv_combatscreen1, R.raw.raw_arenafight),
                Background("Mortal Kombat", R.drawable.iv_mortalkombatfight, R.raw.raw_mortalkombat),
                Background("Ancient Lost Temple", R.drawable.iv_new_arena_ancient_lost_temple, R.raw.raw_arenafight),
                Background("Dragon Ball", R.drawable.iv_dragonballfight, R.raw.raw_dragonball),
                Background("New Arena", R.drawable.iv_new_arena_combatscreen2, R.raw.raw_mortalkombat),
                Background("Street Fighters", R.drawable.iv_streetfighterfight, R.raw.raw_streetfighters),
                Background("Ancient God Temple", R.drawable.iv_new_arena_balrog_temple, R.raw.raw_arenafight),
                Background("Star Wars", R.drawable.iv_starwarsfight, R.raw.raw_starwars),
                Background("Lost Dune", R.drawable.iv_new_arena_lost_dune, R.raw.raw_dragonball),
                Background("Titan Planet", R.drawable.iv_new_arena_titan_planet, R.raw.raw_arenafight),
                Background("Dune", R.drawable.iv_dune, R.raw.raw_arenafight),
                Background("Avengers Ship", R.drawable.iv_new_arena_avengers_ship, R.raw.raw_starwars),
                Background("Cyberpunk", R.drawable.iv_new_arena_cyberpunk, R.raw.raw_arenafight),
            )
    }
