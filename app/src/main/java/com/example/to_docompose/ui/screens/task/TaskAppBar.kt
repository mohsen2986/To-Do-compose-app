package com.example.to_docompose.ui.screens.task

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.style.TextOverflow
import com.example.to_docompose.data.models.TodoTask
import com.example.to_docompose.navigation.Actions
import com.example.to_docompose.ui.theme.topAppBarBackgroundColor
import com.example.to_docompose.ui.theme.topAppBarContentColor


@Composable
fun TaskAppBar(
    selectedTask: TodoTask?,
    onActionPress: (Actions) -> Unit
){
    if(selectedTask == null){
        NewTaskAppBar{
            onActionPress(it)
        }
    }else{
        ExistingTaskAppBar(
            selectedTask = selectedTask,
            onActionPress = {
                onActionPress(it)
            }
        )
    }
}

@Composable
fun NewTaskAppBar(
    onActionPress: (Actions) -> Unit
){
    TopAppBar(
        navigationIcon = {
            BackAction{
                onActionPress(Actions.NO_ACTION)
            }
        },
        title = {
            Text(text = "Add Task",  color = MaterialTheme.colors.topAppBarContentColor)
        } ,
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor ,
        actions = {
            AddAction{
                onActionPress(Actions.ADD)
            }
        }
    )
}

@Composable
fun ExistingTaskAppBar(
    selectedTask: TodoTask ,
    onActionPress: (Actions) -> Unit
){


    TopAppBar(
        navigationIcon = {
          CloseAction(onCloseClicked = {onActionPress(Actions.NO_ACTION)})
        },
        title = {
            Text(
                text = selectedTask.title,
                color = MaterialTheme.colors.topAppBarContentColor ,
                maxLines = 1 ,
                overflow = TextOverflow.Ellipsis
            )
        } ,
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor ,
        actions = {
            DeleteAction(onDeleteClicked = {onActionPress(it)})
            UpdateAction(onUpdateClicked = {onActionPress(it)})
        }
    )
}


@Composable
fun BackAction(
    onBackPress: (Actions) -> Unit
){
    IconButton(
        onClick = { onBackPress(Actions.NO_ACTION) } ,
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "back",
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun CloseAction(
    onCloseClicked: (Actions) -> Unit
){
    IconButton(
        onClick = { onCloseClicked(Actions.NO_ACTION) } ,
    ) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = "close",
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun DeleteAction(
    onDeleteClicked: (Actions) -> Unit
){
    IconButton(
        onClick = { onDeleteClicked(Actions.DELETE) } ,
    ) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = "close",
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}


@Composable
fun UpdateAction(
    onUpdateClicked: (Actions) -> Unit
){
    IconButton(
        onClick = { onUpdateClicked(Actions.UPDATE) } ,
    ) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = "close",
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun AddAction(
    onAddPress: (Actions) -> Unit
){
    IconButton(
        onClick = { onAddPress(Actions.ADD) } ,
    ) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = "Add",
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

