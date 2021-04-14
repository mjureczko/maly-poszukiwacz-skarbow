package pl.marianjureczko.poszukiwacz.activity.searching

import android.widget.TextView
import pl.marianjureczko.poszukiwacz.App
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureBag
import pl.marianjureczko.poszukiwacz.model.TreasureParser
import java.io.Serializable
import java.util.*

class TreasureBagPresenter(treasuresAmount: ArrayList<Int>?, collectedTreasures: ArrayList<String>?) : Serializable {

    private val treasureBag = TreasureBag(treasuresAmount, collectedTreasures)
    private val treasureParser = TreasureParser()

    private lateinit var goldView: TextView
    private lateinit var rubyView: TextView
    private lateinit var diamondView: TextView

    fun init(goldView: TextView, rubyView: TextView, diamondView: TextView) {
        this.goldView = goldView
        this.rubyView = rubyView
        this.diamondView = diamondView
    }

    fun processSearchingResult(result: String): DialogData {
        try {
            val treasure = treasureParser.parse(result)
            return if (treasureBag.contains(treasure)) {
                DialogData(App.getResources().getString(R.string.treasure_already_taken_msg), null)
            } else {
                add(treasure)
                DialogData(treasure.quantity.toString(), treasure.type.image())
            }
        } catch (ex: IllegalArgumentException) {
            return DialogData(App.getResources().getString(R.string.not_a_treasure_msg), null)
        }
    }

    fun bagContent() = treasureBag.bagContent()

    fun collectedInBag() = ArrayList<String>(treasureBag.collected)

    private fun add(treasure: Treasure) {
        treasureBag.collect(treasure)
        showCollectedTreasures()
    }

    fun showCollectedTreasures() {
        goldView.text = treasureBag.golds.toString()
        rubyView.text = treasureBag.rubies.toString()
        diamondView.text = treasureBag.diamonds.toString()
    }
}