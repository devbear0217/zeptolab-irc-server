package me.yuri0217.zeptolab.irc.server.dto

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

data class IrcChannel(
    val name: String,
    private val messages: MutableList<Message> = mutableListOf(),
    private val connectedUsers: MutableList<User> = mutableListOf()
) {
    private val messagesMutex = Mutex()
    private val usersMutex = Mutex()

    fun getConnectedUsers() = connectedUsers

    fun getLastMessages(size: Int) = if (messages.size >= size) messages.sorted().takeLast(size) else messages.sorted()

    suspend fun send(message: Message) = messagesMutex.withLock { messages.add(message) }

    suspend fun connect(user: User) = usersMutex.withLock { connectedUsers.add(user) }

    suspend fun disconnect(user: User) = usersMutex.withLock { connectedUsers.remove(user) }
}
