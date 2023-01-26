package com.example.to_docompose.ui.viewModel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.to_docompose.data.models.Priority
import com.example.to_docompose.data.models.TodoTask
import com.example.to_docompose.data.repositories.DataStoreRepository
import com.example.to_docompose.data.repositories.TodoRepository
import com.example.to_docompose.navigation.Actions
import com.example.to_docompose.utils.Constants.MAX_TITLE_LENGTH
import com.example.to_docompose.utils.RequestState
import com.example.to_docompose.utils.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: TodoRepository ,
    private val dataStoreRepository: DataStoreRepository
): ViewModel(){
    init {
        Log.d("SharedViewModel" ,"create SharedViewModel")
    }
    val id: MutableState<Int> = mutableStateOf(0)
    val title: MutableState<String> = mutableStateOf("")
    val description: MutableState<String> = mutableStateOf("")
    val priority: MutableState<Priority> = mutableStateOf(Priority.NONE)

    val searchAppBarState: MutableState<SearchAppBarState> = mutableStateOf(SearchAppBarState.CLOSED)
    val searchTextState: MutableState<String> = mutableStateOf("")
    private val _search_Tasks =
        MutableStateFlow<RequestState<List<TodoTask>>>(RequestState.Idle)

    val searchTasks: StateFlow<RequestState<List<TodoTask>>> = _search_Tasks

    private val _allTask =
        MutableStateFlow<RequestState<List<TodoTask>>>(RequestState.Idle)

    val allTask: StateFlow<RequestState<List<TodoTask>>> = _allTask

    fun getAllTasks(){
        _allTask.update { RequestState.Loading }
        try {
            viewModelScope.launch {
                repository.tasks.collect { tasks ->
                    _allTask.getAndUpdate { RequestState.Success(tasks) }
                }
            }
        }catch (e: Exception){
            _allTask.update { RequestState.Error(e) }
        }
    }

    fun searchDatabase(searchQuery: String){
        _search_Tasks.update { RequestState.Loading }
        try {
            viewModelScope.launch {
                repository.searchDatabase("%$searchQuery%").collect{ tasks ->
                    _search_Tasks.update { RequestState.Success(tasks) }
                }
            }
        }catch (e: Exception){
            _search_Tasks.update { RequestState.Error(e) }
        }
        searchAppBarState.value = SearchAppBarState.TRIGGERED
    }

    private val _selectedTask : MutableStateFlow<TodoTask?> = MutableStateFlow(null)
    val selectedTask: StateFlow<TodoTask?> = _selectedTask

    fun getSelectedTask(taskId: Int){
        viewModelScope.launch {
            repository.getSelectedTask(taskId).collect{ task ->
                _selectedTask.update { task }
            }
        }
    }

    fun updateTaskFields(selectedTask: TodoTask?){
        (selectedTask ?: TodoTask()).also {
            id.value = it.id
            title.value = it.title
            description.value = it.description
            priority.value = it.priority
        }
    }

    fun updateTitle(newTitle: String){
        if(newTitle.length < MAX_TITLE_LENGTH){
            title.value = newTitle
        }
    }

    fun validateField(): Boolean =
        title.value.isNotEmpty() && description.value.isNotEmpty()

    private fun addTask(){
        viewModelScope.launch(IO) {
            val task = TodoTask(
                title = title.value,
                description = description.value,
                priority = priority.value
            )

            repository.insertData(task)
            searchAppBarState.value = SearchAppBarState.CLOSED
        }
    }

    private fun updateTask(){
        viewModelScope.launch(IO) {
            val task = TodoTask(
                id = id.value ,
                title = title.value,
                description = description.value,
                priority = priority.value
            )

            repository.updateData(task)
        }
    }

    private fun deleteAll(){
        viewModelScope.launch(IO) {
            repository.deleteAll()
        }
    }

    private fun deleteTask(){
        viewModelScope.launch(IO) {
            val task = TodoTask(
                id = id.value ,
                title = title.value,
                description = description.value,
                priority = priority.value
            )

            repository.deleteItem(task)
        }
    }

    fun handleDatabaseActions(action: Actions){
        when(action){
            Actions.ADD ->
                addTask()
            Actions.UPDATE ->
                updateTask()
            Actions.DELETE ->
                deleteTask()
            Actions.DELETE_ALL -> TODO()
            Actions.UNDO ->
                addTask()
            Actions.NO_ACTION -> {}
        }
    }

    private val _sortState  = MutableStateFlow<RequestState<Priority>>(RequestState.Idle)
    val sortState: StateFlow<RequestState<Priority>> = _sortState

    fun persistSortState(priority: Priority){
        viewModelScope.launch(IO) {
            dataStoreRepository.persistStoreData(priority)
        }
    }

    fun readSortState(){
        _sortState.update { RequestState.Loading }
        try {
            viewModelScope.launch {
              dataStoreRepository.readSortData
                  .map { Priority.valueOf(it) }
                  .collect{ priority ->
                      _sortState.update { RequestState.Success(priority ) }
                  }
            }
        }catch (e: Exception){
            _sortState.update { RequestState.Error(e) }
        }
    }

    val lowPriorityTasks: StateFlow<List<TodoTask>> =
        repository.sortByLowPriority.stateIn(
            scope = viewModelScope ,
            started = SharingStarted.WhileSubscribed() ,
            emptyList()
        )

    val highPriorityTasks: StateFlow<List<TodoTask>> =
        repository.sortByHighPriority.stateIn(
            scope = viewModelScope ,
            started = SharingStarted.WhileSubscribed() ,
            emptyList()
        )
}