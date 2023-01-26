package com.example.to_docompose.navigation

import androidx.navigation.NavController
import com.example.to_docompose.utils.Constants.LIST_SCREEN

class Screens(
    navController: NavController
) {
    val list : (Actions) -> Unit ={ actions ->
        navController.navigate("list/${actions.name}"){
            popUpTo(LIST_SCREEN){
                inclusive = true
            }
        }
    }

    val task: (Int) -> Unit = { taskId ->
        navController.navigate("task/$taskId")
    }
}