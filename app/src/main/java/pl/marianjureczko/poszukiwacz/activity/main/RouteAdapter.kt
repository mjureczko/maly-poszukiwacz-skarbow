package pl.marianjureczko.poszukiwacz.activity.main

import android.app.Activity
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.Route
import pl.marianjureczko.poszukiwacz.StorageHelper

interface RoutesRemover {
    fun remove(routeToRemove: Route)
}

class RouteAdapter(
    private val activity: Activity,
    private var routes: MutableList<Route>,
    private val storageHelper: StorageHelper
) : RecyclerView.Adapter<RouteHolder>(), RoutesRemover {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteHolder {
        val view = activity.layoutInflater.inflate(R.layout.routes_item, parent, false)
        return RouteHolder(activity, view, this)
    }

    override fun onBindViewHolder(holder: RouteHolder, position: Int) {
        holder.setRoute(routes[position])
    }

    override fun getItemCount(): Int {
        return routes.size
    }

    override fun remove(routeToRemove: Route) {
        routes.remove(routeToRemove)
        this.notifyDataSetChanged()
        storageHelper.remove(routeToRemove)
    }
}