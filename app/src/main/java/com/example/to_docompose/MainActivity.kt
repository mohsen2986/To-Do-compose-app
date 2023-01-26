package com.example.to_docompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.to_docompose.ui.screens.NavGraphs
import com.example.to_docompose.ui.theme.TODOComposeTheme
import com.example.to_docompose.ui.viewModel.SharedViewModel
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TODOComposeTheme {

                DestinationsNavHost(
                    navGraph = NavGraphs.root ,
                    dependenciesContainerBuilder = {
                        dependency(hiltViewModel<SharedViewModel>(this@MainActivity))
                    }
                )
            }
        }
    }
}


