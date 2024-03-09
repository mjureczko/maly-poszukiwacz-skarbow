package pl.marianjureczko.poszukiwacz.activity.main

import android.content.res.Resources
import pl.marianjureczko.poszukiwacz.R

data class CustomMainState(
    private val resources: Resources,
    var messageIndex: Int = 0,
    val messages : List<Message> = listOf(
        Message(resources.getString(R.string.custom_lead1), R.drawable.lead1),
        Message(resources.getString(R.string.custom_lead2), R.drawable.kalinowice_wita),
        Message(resources.getString(R.string.custom_lead3), R.drawable.chest),
        Message(resources.getString(R.string.custom_lead4), R.drawable.lead4),
        Message(resources.getString(R.string.custom_lead5), R.drawable.change_chest)
    )
) {
    fun isLastMessage(): Boolean {
        return messageIndex >= messages.size - 1
    }
}

data class Message(
    val text: String,
    val imageId: Int
)
