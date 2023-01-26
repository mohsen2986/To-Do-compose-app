package com.example.to_docompose.ui.screens.splash

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.NavBackStackEntry
import com.example.to_docompose.ui.screens.appDestination
import com.example.to_docompose.ui.screens.destinations.ListScreenDestination
import com.example.to_docompose.ui.screens.destinations.SplashScreenDestination
import com.example.to_docompose.ui.screens.destinations.TaskScreenDestination
import com.ramcosta.composedestinations.spec.DestinationStyle

@OptIn(ExperimentalAnimationApi::class)
object SplashTransitions : DestinationStyle.Animated {
    override fun AnimatedContentScope<NavBackStackEntry>.exitTransition(): ExitTransition? {
        return when (targetState.appDestination()) {
            SplashScreenDestination ->
                return slideOutVertically (
                    targetOffsetY = { -it / 2 },
                    animationSpec = tween(2000)
                )
            else -> null
        }
    }

}