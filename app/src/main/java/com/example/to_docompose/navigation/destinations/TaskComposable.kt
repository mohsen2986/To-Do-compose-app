package com.example.to_docompose.navigation.destinations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.to_docompose.navigation.Actions
import com.example.to_docompose.utils.Constants
import com.example.to_docompose.utils.Constants.LIST_ARGUMENT_KEY
import com.example.to_docompose.utils.Constants.TASK_ARGUMENT_KEY

fun NavGraphBuilder.taskComposable(
    navigateToListScreen: (Actions) -> Unit
){
    composable(
        route = Constants.TASK_SCREEN ,
        arguments = listOf(navArgument(TASK_ARGUMENT_KEY){
            type = NavType.IntType
        })
    ){

    }
}