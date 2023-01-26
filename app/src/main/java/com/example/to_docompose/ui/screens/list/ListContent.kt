package com.example.to_docompose.ui.screens.list

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.to_docompose.data.models.Priority
import com.example.to_docompose.data.models.TodoTask
import com.example.to_docompose.navigation.Actions
import com.example.to_docompose.ui.theme.*
import com.example.to_docompose.utils.RequestState
import com.example.to_docompose.utils.SearchAppBarState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun ListContent(
    toDoTasks: RequestState<List<TodoTask>> ,
    searchTasks: RequestState<List<TodoTask>> ,
    lowPriority: List<TodoTask> ,
    highPriority: List<TodoTask> ,
    sortState: RequestState<Priority> ,
    searchAppBarState: SearchAppBarState,
    onSwipeToDelete: (Actions , TodoTask) -> Unit,
    navigationToTaskScreen: (task: TodoTask) -> Unit
){
    val listTaskContent =
        if(sortState is RequestState.Success){
            if(searchAppBarState == SearchAppBarState.TRIGGERED)
                searchTasks
            else
                when(sortState.data){
                    Priority.HIGH ->
                        RequestState.Success(highPriority)
                    Priority.MEDIUM ->
                        RequestState.Success(lowPriority)
                    Priority.LOW ->
                        RequestState.Success(lowPriority)
                    Priority.NONE ->
                        toDoTasks
                }
    }else{
            RequestState.Idle
    }

    when(listTaskContent){
        is RequestState.Success -> {
           HandleListContent(
               tasks = listTaskContent.data,
               onSwipeToDelete = onSwipeToDelete,
               navigationToTaskScreen = navigationToTaskScreen
           )
        }
        is RequestState.Error -> {}
        RequestState.Idle -> {}
        RequestState.Loading -> {}
    }
}

@Composable
fun HandleListContent(
    tasks: List<TodoTask> ,
    onSwipeToDelete: (Actions , TodoTask) -> Unit,
    navigationToTaskScreen: (task: TodoTask) -> Unit
){
    if(tasks.isEmpty()){
        EmptyContent()
    }else{
        DisplayTasks(
            toDoTasks = tasks,
            onSwipeToDelete = onSwipeToDelete,
            navigationToTaskScreen = navigationToTaskScreen
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DisplayTasks(
    toDoTasks: List<TodoTask>,
    onSwipeToDelete: (Actions , TodoTask) -> Unit,
    navigationToTaskScreen: (task: TodoTask) -> Unit
){
    LazyColumn{
        items(
            items = toDoTasks ,
            key = {task ->
                task.id
            }
        ){ task ->
            val dismissState = rememberDismissState()
            val dismissDirection = dismissState.dismissDirection
            val isDismissed = dismissState.isDismissed(DismissDirection.EndToStart)
            if(isDismissed && dismissDirection == DismissDirection.EndToStart){
                val scope = rememberCoroutineScope()
                scope.launch {
                    delay(700)
                    onSwipeToDelete(Actions.DELETE , task)
                }
            }

            val degree by animateFloatAsState(
                targetValue =
                if (dismissState.targetValue == DismissValue.Default)
                    0f
                else
                    -45f
            )
            var itemAppeared by remember {
                mutableStateOf(false)
            }
            LaunchedEffect(key1 = true){
                itemAppeared = true
            }

            AnimatedVisibility(
                visible = itemAppeared && !isDismissed ,
                enter = expandVertically(
                    animationSpec = tween(
                        durationMillis = 300
                    )
                ) ,
                exit = shrinkVertically(
                    animationSpec = tween(
                        durationMillis = 300
                    )
                )

            ) {
                SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(DismissDirection.EndToStart),
                    dismissThresholds = {FractionalThreshold(fraction = 0.3f)},
                    background = { RedBackground(degree = degree)},
                ) {
                    TaskItem(toDoTask = task, navigationToTaskScreen = navigationToTaskScreen)

                }
            }

        }
    }
}

@Composable
fun RedBackground(degree: Float){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(HighPriorityColor)
        .padding(horizontal = 24.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            modifier = Modifier.rotate(degree),
            imageVector = Icons.Filled.Delete,
            tint = Color.White,
            contentDescription = ""
        )
    }
}

@ExperimentalMaterialApi
@Composable
fun TaskItem(
    toDoTask: TodoTask ,
    navigationToTaskScreen: (task: TodoTask) -> Unit
){
    Surface(
        modifier = Modifier.fillMaxWidth() ,
        color = MaterialTheme.colors.taskItemBackgroundColor ,
        shape = RectangleShape  ,
        elevation = TASK_ITEM_ELEVATION ,
        onClick = {
            navigationToTaskScreen(toDoTask)
        }
    ) {
        Column(modifier = Modifier
            .padding(LARGE_PADDING)
            .fillMaxWidth()
        ) {
            Row{
               Text(
                   modifier = Modifier
                       .weight(8f) ,
                   text = toDoTask.title ,
                   color = MaterialTheme.colors.taskItemTextColor ,
                   style = MaterialTheme.typography.h5 ,
                   fontWeight = FontWeight.Bold ,
                   maxLines = 1
               )
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) ,
                    contentAlignment = Alignment.TopEnd
                ){
                    Canvas(
                        modifier = Modifier
                            .width(PRIORITY_INDICATOR_SIZE)
                            .height(PRIORITY_INDICATOR_SIZE),

                    ){
                        drawCircle(toDoTask.priority.color)
                    }
                }
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = toDoTask.description,
                color = MaterialTheme.colors.taskItemTextColor ,
                style = MaterialTheme.typography.subtitle1 ,
                maxLines = 2 ,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
@Preview
fun Preview(){
    TaskItem(toDoTask = TodoTask(0 , "this is title" , "this is description" ,Priority.HIGH), navigationToTaskScreen = {})
}