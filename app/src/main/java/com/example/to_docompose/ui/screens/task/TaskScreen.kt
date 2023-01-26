package com.example.to_docompose.ui.screens.task

import android.annotation.SuppressLint
import android.app.Notification.Action
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.to_docompose.data.models.Priority
import com.example.to_docompose.data.models.TodoTask
import com.example.to_docompose.navigation.Actions
import com.example.to_docompose.ui.viewModel.SharedViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.example.to_docompose.R
import com.example.to_docompose.ui.screens.CustomAlertDialogActions
import com.example.to_docompose.ui.screens.destinations.CustomAlertDialogDestination
import com.example.to_docompose.ui.screens.splash.SplashTransitions
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import com.ramcosta.composedestinations.spec.Direction

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Destination(
//    style = SplashTransitions::class
)
@Composable
fun TaskScreen(
    sharedViewModel: SharedViewModel,
    navigator: DestinationsNavigator ,
    selectedTask: TodoTask?,
    actionNavigationResult: ResultBackNavigator<Actions>,
    dialogRecipient: ResultRecipient<CustomAlertDialogDestination, CustomAlertDialogActions>
){

    LaunchedEffect(key1 = selectedTask?.id ?: 0){
        sharedViewModel.updateTaskFields(selectedTask)
    }
    val title: String by sharedViewModel.title
    val description: String by sharedViewModel.description
    val priority: Priority by sharedViewModel.priority

    val context: Context = LocalContext.current
    var lastAction by remember(key1 = true) {
        mutableStateOf(Actions.NO_ACTION)
    }


    Scaffold(
        topBar = {
            TaskAppBar(selectedTask){ action ->
                lastAction = action
                when(action){
                    Actions.ADD -> {
                        if(sharedViewModel.validateField()){

                        navigator.navigate(
                            CustomAlertDialogDestination(
                            "Add Item" ,
                            "Are you shore add item: ${sharedViewModel.title.value}"
                        ) )

                        }else{
                            displayToast(context)
                        }
                    }
                    Actions.UPDATE -> {
                        if(sharedViewModel.validateField()){
                            actionNavigationResult.navigateBack(action)
                        }else{
                            displayToast(context)
                        }
                    }
                    Actions.DELETE -> {
                        if(sharedViewModel.validateField()){
                            actionNavigationResult.navigateBack(action)
                        }else{
                            displayToast(context)
                        }
                    }
                    Actions.DELETE_ALL -> TODO()
                    Actions.UNDO -> TODO()
                    Actions.NO_ACTION -> {
                        actionNavigationResult.navigateBack()
                    }
                }
            }
        }
    ) {
        TaskContent(
            title = title,
            onTitleChanged = {
                sharedViewModel.updateTitle(it)
            },
            description = description,
            onDescriptionChanged = {
                sharedViewModel.description.value = it
            },
            priority = priority,
            onPrioritySelected = {
                sharedViewModel.priority.value = it
            }
        )
    }

    dialogRecipient.onNavResult { result ->
        when(result){
            NavResult.Canceled -> {}
            is NavResult.Value -> {
                when(result.value){
                    CustomAlertDialogActions.YES -> {
                        actionNavigationResult.navigateBack(lastAction)
                    }
                    CustomAlertDialogActions.NO -> {
                    }
                }
            }
        }
    }
}



fun displayToast(context: Context){
    Toast.makeText(
        context ,
        "Fields empty" ,
        Toast.LENGTH_LONG
    ).show()
}