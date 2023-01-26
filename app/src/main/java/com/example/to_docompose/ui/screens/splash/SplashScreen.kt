package com.example.to_docompose.ui.screens.splash

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.to_docompose.R
import com.example.to_docompose.ui.screens.destinations.ListScreenDestination
import com.example.to_docompose.ui.screens.destinations.SplashScreenDestination
import com.example.to_docompose.ui.theme.splashScreen
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterialApi::class)
@RootNavGraph(start = true)
@Destination(
//    style = SplashTransitions::class,
)
@Composable
fun SplashScreen(
    navigator: DestinationsNavigator ,
){
    var startAnimation by remember {
        mutableStateOf(false)
    }
    val offsetState by animateDpAsState(
        targetValue = if(startAnimation) 0.dp else 100.dp,
        animationSpec = tween(durationMillis = 1_000)
    )
    val alphaState by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f ,
         animationSpec = tween(durationMillis = 1_000)
    )

    LaunchedEffect(key1 = true){
        startAnimation = true
        delay(3_000)
        navigator.navigate(
            ListScreenDestination
        ){
           popUpTo(SplashScreenDestination.route) {inclusive = true}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.splashScreen),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .size(100.dp)
                .alpha(alphaState)
                .offset(y = offsetState),
            painter = painterResource(id = R.drawable.ic_todo),
            contentDescription = ""
        )
    }
}