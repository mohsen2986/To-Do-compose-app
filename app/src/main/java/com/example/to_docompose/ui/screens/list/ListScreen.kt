package com.example.to_docompose.ui.screens.list

import android.annotation.SuppressLint
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import com.example.to_docompose.R
import com.example.to_docompose.navigation.Actions
import com.example.to_docompose.ui.screens.CustomAlertDialogActions
import com.example.to_docompose.ui.screens.destinations.CustomAlertDialogDestination
import com.example.to_docompose.ui.screens.destinations.TaskScreenDestination
import com.example.to_docompose.ui.screens.splash.SplashTransitions
import com.example.to_docompose.ui.theme.fabBackgroundColor
import com.example.to_docompose.ui.viewModel.SharedViewModel
import com.example.to_docompose.utils.SearchAppBarState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalMaterialApi
@Destination(
//    style = SplashTransitions::class
)
@Composable
fun ListScreen(
    sharedViewModel: SharedViewModel ,
    navigator: DestinationsNavigator ,
    resultRecipient: ResultRecipient<TaskScreenDestination, Actions>,
    dialogRecipient: ResultRecipient<CustomAlertDialogDestination, CustomAlertDialogActions>
){
    LaunchedEffect(key1 = true, block = {
        sharedViewModel.getAllTasks()
        sharedViewModel.readSortState()
    })
    var action by remember(true) {
        mutableStateOf(Actions.NO_ACTION)
    }

    val allTasks by sharedViewModel.allTask.collectAsState()
    val searchTasks by sharedViewModel.searchTasks.collectAsState()

    val searchAppBarState: SearchAppBarState by sharedViewModel.searchAppBarState
    val searchTextState: String by sharedViewModel.searchTextState

    val sortState by sharedViewModel.sortState.collectAsState()
    val lowPriorityTasks by sharedViewModel.lowPriorityTasks.collectAsState()
    val highPriorityTasks by sharedViewModel.highPriorityTasks.collectAsState()

    val scaffoldState = rememberScaffoldState()
    DisplaySnakeBar(
        scaffoldState = scaffoldState,
        handleDatabaseActions = {
            sharedViewModel.handleDatabaseActions(action = action)
        },
        onUndoClicked = {
            sharedViewModel.handleDatabaseActions(action = Actions.UNDO)
        },
        title = sharedViewModel.title.value,
        action = action
    )

    Scaffold (
        scaffoldState = scaffoldState ,
        topBar = {
            ListAppBar(
                sharedViewModel = sharedViewModel ,
                searchAppBarState = searchAppBarState ,
                searchTextState = searchTextState,
                onActionClicked = {actions ->
                    action = actions
                }
            )
       },
        content = {
                  ListContent(
                      toDoTasks = allTasks,
                      searchTasks = searchTasks,
                      searchAppBarState = searchAppBarState ,
                      navigationToTaskScreen = {
                          navigator.navigate(TaskScreenDestination(it))
                      },
                      onSwipeToDelete = {actions, todoTask ->
                          action = actions
                          sharedViewModel.updateTaskFields(todoTask)
                      } ,
                      highPriority = highPriorityTasks,
                      lowPriority = lowPriorityTasks,
                      sortState = sortState
                  )
        } ,
        floatingActionButton = {
            ListFab(onFabClicked = {
                navigator.navigate(TaskScreenDestination(null ))
            })
        }
    )
    
    resultRecipient.onNavResult { result ->
        when(result){
            NavResult.Canceled -> {}
            is NavResult.Value -> {
                action = result.value
//                sharedViewModel.handleDatabaseActions(action = result.value)
            }
        }
    }

    dialogRecipient.onNavResult { result ->
        when(result){
            NavResult.Canceled -> {}
            is NavResult.Value -> {

            }
        }
    }
}

@Composable
fun ListFab(
    onFabClicked: (Int) -> Unit
){
    FloatingActionButton(
        onClick = {
            onFabClicked(-1)
        },
        backgroundColor = MaterialTheme.colors.fabBackgroundColor
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(R.string.add_button)
        )
    }
}

@Composable
fun DisplaySnakeBar(
    scaffoldState: ScaffoldState,
    handleDatabaseActions: ()-> Unit,
    onUndoClicked: (Actions) -> Unit,
    title: String,
    action: Actions
){
    handleDatabaseActions()
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = action){
        if(action != Actions.NO_ACTION){
            scope.launch {
                val snakeBarResult = scaffoldState.snackbarHostState.showSnackbar(
                    message = "${action.name}: $title" ,
                    actionLabel = setActionLabel(action)
                )
                undoDeletedTask(
                    action = action ,
                    snakeBarResult = snakeBarResult,
                    onUndoClicked = onUndoClicked
                )

            }
        }
    }
}

private fun setActionLabel(action: Actions): String{
    return if(action.name == "DELETE"){
        "Undo"
    }else{
        "Ok"
    }
}
private fun undoDeletedTask(
    action: Actions,
    snakeBarResult: SnackbarResult ,
    onUndoClicked: (Actions) -> Unit
){
    if(snakeBarResult == SnackbarResult.ActionPerformed
        && action == Actions.DELETE
    ){
        onUndoClicked(Actions.UNDO)
    }
}
