package pl.marianjureczko.poszukiwacz.activity.facebook

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress

class FacebookViewModel(private val state: SavedStateHandle) : ViewModel() {
    private val TAG = javaClass.simpleName
    lateinit var progress: TreasuresProgress
        private set
    lateinit var elements: List<ElementDescription>
        private set

    fun initialize(progress: TreasuresProgress) {
        this.progress = progress
        val elements = mutableListOf<ElementDescription>()
        //TODO use string.xml
        elements.add(ElementDescription(Type.TREASURES_SUMMARY, true, "Zebrane skarby"))
        progress.commemorativePhotosByTreasuresDescriptionIds.forEach { (id, photo) ->
            elements.add(ElementDescription(Type.COMMEMORATIVE_PHOTO, true, "Skarb $id", orderNumber = id, photo = photo))
        }
        this.elements = elements
    }

    fun getElement(position: Int): ElementDescription = elements[position]

    fun getElementsCount(): Int = elements.size

    fun getSummaryElement(): ElementDescription = elements[0]

    fun getCommemorativePhotoElements(): List<ElementDescription> = elements.filter { it.type == Type.COMMEMORATIVE_PHOTO }
}