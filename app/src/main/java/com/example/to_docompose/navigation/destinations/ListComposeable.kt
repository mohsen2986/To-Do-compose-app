package com.example.to_docompose.navigation.destinations

import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.to_docompose.ui.screens.list.ListScreen
import com.example.to_docompose.ui.viewModel.SharedViewModel
import com.example.to_docompose.utils.Constants
import com.example.to_docompose.utils.Constants.LIST_ARGUMENT_KEY

@ExperimentalMaterialApi
fun NavGraphBuilder.listComposable(
    navigateToTaskScreen: (Int) -> Unit ,
    sharedViewModel: SharedViewModel ,
){
    composable(
        route = Constants.LIST_SCREEN ,
        arguments = listOf(navArgument(LIST_ARGUMENT_KEY){
            type = NavType.StringType
        })
    ){
//        ListScreen(
//            navigateToTaskScreen = navigateToTaskScreen ,
//            sharedViewModel = sharedViewModel
//        )
    }
}