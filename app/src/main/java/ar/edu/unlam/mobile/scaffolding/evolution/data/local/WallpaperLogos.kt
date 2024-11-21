package ar.edu.unlam.mobile.scaffolding.evolution.data.local

import ar.edu.unlam.mobile.scaffolding.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WallpaperLogos
    @Inject
    constructor() {
        val original =
            listOf(
                Logo(R.drawable.iv_marvelone),
                Logo(R.drawable.iv_marveltwo),
                Logo(R.drawable.iv_marvelthree),
                Logo(R.drawable.iv_marvelfour),
                Logo(R.drawable.iv_marvelfive),
                Logo(R.drawable.iv_marvelsix),
            )
        val christmas =
            listOf(
                Logo(R.drawable.im_avengers_navidad0),
                Logo(R.drawable.im_avengers_navidad1),
                Logo(R.drawable.im_avengers_navidad2),
                Logo(R.drawable.im_avengers_navidad3),
                Logo(R.drawable.im_avengers_navidad4),
            )

        val halloween =
            listOf(
                Logo(R.drawable.im_avengers_halloween0),
                Logo(R.drawable.im_avengers_halloween1),
                Logo(R.drawable.im_avengers_halloween2),
                Logo(R.drawable.im_avengers_halloween3),
                Logo(R.drawable.im_avengers_halloween4),
            )

        val saintPatrick =
            listOf(
                Logo(R.drawable.im_avengers_sanpatricio1),
                Logo(R.drawable.im_avengers_sanpatricio2),
                Logo(R.drawable.im_avengers_sanpatricio3),
                Logo(R.drawable.im_avengers_sanpatricio4),
                Logo(R.drawable.im_avengers_sanpatricio5),
            )
    }
