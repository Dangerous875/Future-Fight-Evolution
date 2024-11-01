package ar.edu.unlam.mobile.scaffolding.evolution.data.local

import ar.edu.unlam.mobile.scaffolding.R
import javax.inject.Inject

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
            )
        val christmas =
            listOf(
                Logo(R.drawable.im_avengers_navidad1),
                Logo(R.drawable.im_avengers_navidad2),
                Logo(R.drawable.im_avengers_navidad3),
                Logo(R.drawable.im_avengers_navidad4),
            )

        val halloween =
            listOf(
                Logo(R.drawable.im_avengers_halloweenb),
                Logo(R.drawable.im_avengers_halloween),
                Logo(R.drawable.im_avengers_halloweenb),
                Logo(R.drawable.im_avengers_halloween),
            )

        val saintPatrick =
            listOf(
                Logo(R.drawable.im_avengers_sanpatricio),
                Logo(R.drawable.iv_marvelone),
                Logo(R.drawable.iv_marvelone),
                Logo(R.drawable.im_avengers_sanpatricio),
            )
    }
