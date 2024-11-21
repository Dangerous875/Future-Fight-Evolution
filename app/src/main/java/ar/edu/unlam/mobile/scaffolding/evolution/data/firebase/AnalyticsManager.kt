package ar.edu.unlam.mobile.scaffolding.evolution.data.firebase

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import javax.inject.Inject

class AnalyticsManager
    @Inject
    constructor(
        private val analytics: FirebaseAnalytics,
    ) {
        fun loginUser() {
            analytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
                param(FirebaseAnalytics.Param.SUCCESS, 1)
            }
        }

        fun createAccountUser() {
            analytics.logEvent(FirebaseAnalytics.Event.SIGN_UP) {
                param(FirebaseAnalytics.Param.SCORE, "Creación de cuenta exitosa")
            }
        }
    }
