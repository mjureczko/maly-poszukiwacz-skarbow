package pl.marianjureczko.poszukiwacz.activity.searching

import android.content.Context
import android.view.View
import pl.marianjureczko.poszukiwacz.activity.photo.PhotoActivity
import pl.marianjureczko.poszukiwacz.shared.errorTone

class PhotoButtonListener(
    private val context: Context,
    private val data: DataStorageWrapper
) : View.OnClickListener {
    override fun onClick(v: View?) {
        if (data.getTreasure() != null && data.getTreasure()!!.hasPhoto()) {
            context.startActivity(PhotoActivity.intent(context, data.getTreasure()!!.photoFileName!!))
        } else {
            errorTone()
        }
    }
}