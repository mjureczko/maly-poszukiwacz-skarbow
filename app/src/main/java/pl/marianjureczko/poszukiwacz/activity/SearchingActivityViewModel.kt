package pl.marianjureczko.poszukiwacz.activity

import androidx.lifecycle.ViewModel
import pl.marianjureczko.poszukiwacz.Route

class SearchingActivityViewModel() : ViewModel() {
    private val TAG = "SearchingActivityViewModel"
    lateinit var route: Route
}