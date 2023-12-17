package pl.marianjureczko.poszukiwacz.activity.facebook

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import pl.marianjureczko.poszukiwacz.R

class ElementsAdapter(
    private val activity: FragmentActivity,
    private val model: FacebookViewModel
) : RecyclerView.Adapter<ElementHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElementHolder {
        val view = activity.layoutInflater.inflate(R.layout.element_item, parent, false)
        model.getMap()?.isSelected ?: false
        return ElementHolder(activity, view, model, this)
    }

    override fun getItemCount(): Int = model.getElementsCount()

    override fun onBindViewHolder(holder: ElementHolder, position: Int) {
        val element = model.getElement(position)
        holder.setup(element)
    }
}