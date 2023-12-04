package com.example.aifordrive.models

data class ChatMessage(val message: String, val senderType: SenderType)

enum class SenderType {
    USER, BOT
}

