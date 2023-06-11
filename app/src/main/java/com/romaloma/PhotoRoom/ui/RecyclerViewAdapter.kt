import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.romaloma.PhotoRoom.R

class RecyclerViewAdapter(
    private val context: Context,
    private val itemClick: (Int) -> Unit
) : RecyclerView.Adapter<RecyclerViewAdapter.ImageHolder>() {

    var bitmapList: List<Bitmap?> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_recycler, parent, false)
        return ImageHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.bindBitmap(bitmapList[position])
    }

    override fun getItemCount(): Int = bitmapList.size

    fun updateItems(items: MutableList<Bitmap?>) {
        bitmapList = items
        notifyDataSetChanged()
    }

    inner class ImageHolder(itemView: View?, val itemClick: (Int) -> Unit) :
        RecyclerView.ViewHolder(itemView!!) {
        private val image = itemView?.findViewById<AppCompatImageView>(R.id.image_recycler_item)

        fun bindBitmap(bitmap: Bitmap?) {

            if (bitmap != null) image?.setImageBitmap(bitmap)
            else image?.setImageResource(android.R.drawable.ic_delete)
            itemView.setOnClickListener { itemClick(adapterPosition) }
        }
    }
}