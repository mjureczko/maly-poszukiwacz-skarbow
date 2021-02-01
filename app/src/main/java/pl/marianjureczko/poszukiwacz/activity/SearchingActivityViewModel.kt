package pl.marianjureczko.poszukiwacz.activity

import androidx.lifecycle.ViewModel
import pl.marianjureczko.poszukiwacz.TreasuresList

class SearchingActivityViewModel() : ViewModel() {
    private val TAG = "SearchingActivityViewModel"
    lateinit var treasures: TreasuresList
}