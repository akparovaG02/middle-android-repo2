package ru.yandex.praktikumchatapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.yandex.praktikumchatapp.data.ChatRepository

class ChatViewModel(
    val isWithReplies: Boolean = true
) : ViewModel() {

    private val repository = ChatRepository()
/*
    private val _messages =
        MutableStateFlow<List<Message>>(emptyList())  // TODO Задание 1: замените на Flow
    val messages: StateFlow<List<Message>> = _messages

    private val _shouldShowKeyboard = MutableStateFlow(false) // TODO Задание 3: добавьте состояние shouldShowKeyboard
    val shouldShowKeyboard: StateFlow<Boolean> = _shouldShowKeyboard
*/
    private val _chatState = MutableStateFlow(ChatState()) // TODO Задание 4: замените messages и shouldShowKeyboard на state
    val chatState: StateFlow<ChatState> = _chatState

    init {
        viewModelScope.launch {
            while (isWithReplies) {
                repository.getReplyMessage().collect { response ->

                    val currentState = _chatState.value
                    val newMessages =
                        currentState.messages + Message.OtherMessage(response)

                    _chatState.update {
                        it.copy(
                            messages = newMessages,
                            shouldShowKeyboard = it.messages.isEmpty()
                        )
                    }
                }
            }
        }
    }

    fun sendMyMessage(messageText: String) {
        _chatState.update {
            it.copy(
                messages = it.messages + Message.MyMessage(messageText)
            )
        }

    }
    /* 1,2, 3  задачи
    init {
        viewModelScope.launch {
            while (isWithReplies) {
                repository.getReplyMessage().collect { response ->

                    val currentMessages = _messages.value
                    val updateMasseges = currentMessages + Message.OtherMessage(response)

                    _messages.value = updateMasseges

                    if(currentMessages.isEmpty()){
                        _shouldShowKeyboard.value = true
                    }
                }
            }
        }
    }

    fun sendMyMessage(messageText: String) {
        _messages.value = _messages.value + Message.MyMessage(messageText)
    }
    */
}