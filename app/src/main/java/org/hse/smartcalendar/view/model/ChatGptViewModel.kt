package org.hse.smartcalendar.view.model

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.SuggestedTask
import org.hse.smartcalendar.network.ApiClient
import org.hse.smartcalendar.network.NetworkResponse
import org.hse.smartcalendar.repository.ChatGptRepository
import java.io.File

class ChatGptViewModel() : ViewModel() {
    private val repository: ChatGptRepository = ChatGptRepository(ApiClient.chatGptApiService)

    private val _chatHistory = mutableStateListOf<Pair<String, List<SuggestedTask>>>()
    val chatHistory: List<Pair<String, List<SuggestedTask>>> get() = _chatHistory
    private val _suggestions = MutableLiveData<NetworkResponse<List<DailyTask>>>()
    val chatResult: LiveData<NetworkResponse<List<DailyTask>>> = _suggestions

    fun fetchSuggestions(query: String) {
        viewModelScope.launch {
            _suggestions.value = NetworkResponse.Loading
            val tasks = repository.generateSuggestions(query)
            _suggestions.value = tasks
            if (tasks is NetworkResponse.Success) {
                val suggested = tasks.data.map { SuggestedTask(it) }
                _chatHistory.add(query to suggested)
            }
        }
    }
    fun fetchSuggestionsAudio(audioFile: File) {
        viewModelScope.launch {
            val result = repository.generateSuggestionsAudio(audioFile)
            if (result is NetworkResponse.Success) {
                val suggested = result.data.map { SuggestedTask(it) }
                _chatHistory.add("Audio query" to suggested)
            }
        }
    }

    fun acceptTask(task: SuggestedTask) : Boolean {
            val success = repository.tryAdd(task.task)
            if (success) {
                task.status = SuggestedTask.TaskStatus.ACCEPTED
                return true
            }
        return false
    }

    fun declineTask(task: SuggestedTask) {
        task.status = SuggestedTask.TaskStatus.DECLINED
//        if (queryIndex in _chatHistory.indices) {
//            val (q, tasks) = _chatHistory[queryIndex]
//            val task = tasks[taskIndex]
//            if (repository.declineTask(task)) {
//                val newTasks = tasks.toMutableList().apply { removeAt(taskIndex) }
//                _chatHistory[queryIndex] = q to newTasks
//            }
//        }
    }
    fun clearHistory() {
        _chatHistory.clear()
    }
}