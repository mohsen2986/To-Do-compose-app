package com.example.to_docompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.to_docompose.ui.screens.NavGraphs
import com.example.to_docompose.ui.screens.startAppDestination
import com.example.to_docompose.ui.screens.startDestination
import com.example.to_docompose.ui.theme.TODOComposeTheme
import com.example.to_docompose.ui.viewModel.SharedViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.NestedNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.spec.NavHostEngine
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    private val sharedViewModel: SharedViewModel by viewModels()

    @OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TODOComposeTheme {
                val navHostEngine: NavHostEngine = rememberAnimatedNavHostEngine(
                    rootDefaultAnimations = RootNavGraphDefaultAnimations(
//                        enterTransition = {
//                            slideInHorizontally(
//                                initialOffsetX = { 100 },
//                                animationSpec = tween(
//                                    durationMillis = 700,
//                                    easing = FastOutSlowInEasing
//                                )
//                            )
//                        },
//                        exitTransition = {
//                            slideOutHorizontally(
//                                targetOffsetX = { -100 },
//                                animationSpec = tween(
//                                    durationMillis = 700,
//                                    easing = FastOutSlowInEasing
//                                )
//                            )
//                        },
                    )

                )
                navController = rememberAnimatedNavController()
//                navController = navHostEngine.rememberNavController()


                DestinationsNavHost(
                    navController= navController ,
                    engine = navHostEngine,
                    navGraph = NavGraphs.root ,
                    dependenciesContainerBuilder = {
                        dependency(hiltViewModel<SharedViewModel>(this@MainActivity))
                    }
                )

            }
        }
    }
}


