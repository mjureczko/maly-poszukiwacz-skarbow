package pl.marianjureczko.poszukiwacz.activity.main

import pl.marianjureczko.poszukiwacz.App
import pl.marianjureczko.poszukiwacz.R

data class CustomMainState(
    var messageIndex: Int = 0,
    val messages : List<Message> = listOf(
        Message(App.getResources().getString(R.string.custom_lead1), R.drawable.lead1),
        Message(App.getResources().getString(R.string.custom_lead2), R.drawable.map),
        Message(App.getResources().getString(R.string.custom_lead3), R.drawable.chest),
        Message(App.getResources().getString(R.string.custom_lead4), R.drawable.lead4),
        Message(App.getResources().getString(R.string.custom_lead5), R.drawable.change_chest)
    )
)

data class Message(
    val text: String,
    val imageId: Int
)
