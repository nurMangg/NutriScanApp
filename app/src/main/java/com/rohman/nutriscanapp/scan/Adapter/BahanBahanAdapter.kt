import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rohman.nutriscanapp.R

class BahanBahanAdapter(private val ingredients: List<String>) :
    RecyclerView.Adapter<BahanBahanAdapter.IngredientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bahan_bahan, parent, false)
        return IngredientViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.ingredientNameTextView.text = ingredients[position]
    }

    override fun getItemCount() = ingredients.size

    class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ingredientNameTextView: TextView = itemView.findViewById(R.id.ingredientNameTextView)
    }
}
