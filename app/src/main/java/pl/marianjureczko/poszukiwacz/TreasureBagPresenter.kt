package pl.marianjureczko.poszukiwacz

import android.widget.TextView
import pl.marianjureczko.poszukiwacz.dialog.SearchResultDialog
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureBag
import pl.marianjureczko.poszukiwacz.model.TreasureParser
import java.util.*

class TreasureBagPresenter(
    treasuresAmount: ArrayList<Int>?,
    collectedTreasures: ArrayList<String>?
) {

    private val treasureBag = TreasureBag(
        treasuresAmount,
        collectedTreasures
    )
    private val treasureParser =
        TreasureParser()

    private lateinit var goldView: TextView
    private lateinit var rubyView: TextView
    private lateinit var diamondView: TextView

    fun init(goldView: TextView, rubyView: TextView, diamondView: TextView) {
        this.goldView = goldView
        this.rubyView = rubyView
        this.diamondView = diamondView
    }

    fun processSearchingResult(result: String, resultDialog: SearchResultDialog) : DialogData{
        try {
            val treasure = treasureParser.parse(result)
            if (treasureBag.contains(treasure)) {
                return DialogData("Ten skarb został już zabrany!", null)
            } else {
                add(treasure)
                return DialogData(treasure.quantity.toString(), treasure.type.image())
            }
        } catch (ex: IllegalArgumentException) {
            return DialogData("To nie jest skarb!", null)
        }
    }

    fun bagContent() = treasureBag.bagContent()

    fun collectedInBag() = ArrayList<String>(treasureBag.collected)

    private fun add(treasure: Treasure) {
        treasureBag.collect(treasure)
        showTreasure()
    }

    fun showTreasure() {
        goldView.text = treasureBag.golds.toString()
        rubyView.text = treasureBag.rubies.toString()
        diamondView.text = treasureBag.diamonds.toString()
    }
}