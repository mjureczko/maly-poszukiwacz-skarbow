package pl.marianjureczko.poszukiwacz.screen.main

import android.content.res.Resources
import pl.marianjureczko.poszukiwacz.R

data class MainState(
    private val resources: Resources,
    val messageIndex: Int = 0,
    val assetsCopied: Boolean = false,
    val messages : List<Message> = listOf(
        Message(resources.getString(R.string.custom_lead1), R.drawable.host),
        Message(resources.getString(R.string.custom_lead2), R.drawable.welcome_to_the_town),
        Message(resources.getString(R.string.custom_lead3), R.drawable.qr),
        Message(resources.getString(R.string.custom_lead4), R.drawable.chest),
        Message(resources.getString(R.string.custom_lead5), R.drawable.lead5),
        Message(resources.getString(R.string.custom_lead6), R.drawable.change_chest)
    )
) {
    fun isLastMessage(): Boolean {
        return messageIndex >= messages.size - 1
    }

    fun isFirstMessage(): Boolean {
        return messageIndex == 0
    }
}

data class Message(
    val text: String,
    val imageId: Int
)
