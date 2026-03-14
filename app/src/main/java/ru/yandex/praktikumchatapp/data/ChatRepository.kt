package ru.yandex.praktikumchatapp.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.retryWhen

class ChatRepository(
    private val api: ChatApi = ChatApi()
) {

    fun getReplyMessage(): Flow<String> {
        return api.getReply()
            .retryWhen { cause, attempt ->
                if (attempt < 3) {
                    delay(1000L * (1 shl attempt.toInt()))
                    true
                } else {
                    false
                }
            }
            .catch {
                emit("Ошибка сети")
            }
    // TODO Задание 2: добавьте обработку ошибок
    }
}