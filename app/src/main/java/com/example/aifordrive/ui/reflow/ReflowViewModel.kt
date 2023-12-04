package com.example.aifordrive.ui.reflow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aifordrive.models.ChatMessage
import com.example.aifordrive.models.SenderType


class ReflowViewModel : ViewModel() {

    private val _messages = MutableLiveData<MutableList<ChatMessage>>()
    val messages: LiveData<MutableList<ChatMessage>> = _messages

    init {
        _messages.value = mutableListOf()
    }

    fun sendMessage(content: String) {
        val newMessage = ChatMessage(content, SenderType.USER) // true for user message
        _messages.value?.add(newMessage)
        _messages.value = _messages.value // Trigger LiveData update

        respondToMessage(content)
    }

    private fun respondToMessage(content: String) {
        val botResponse = "Bot: $content"
        val responseMessage = ChatMessage(botResponse, SenderType.BOT) // false for bot message
        _messages.value?.add(responseMessage)
        _messages.value = _messages.value // Trigger LiveData update
    }
}
