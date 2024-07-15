import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.coingeckotask.data.models.response.PriceEntry
import com.example.coingeckotask.databinding.ListItemBitCoinBinding
import com.example.coingeckotask.utils.toFormattedDateString

class CryptoPriceAdapter : ListAdapter<PriceEntry, CryptoPriceAdapter.MyViewHolder>(
    CryptoPriceDiffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding: ListItemBitCoinBinding = ListItemBitCoinBinding.inflate(
            layoutInflater, parent, false
        )
        return MyViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val cryptoPrice: PriceEntry = getItem(position)!!
        holder.bind(cryptoPrice)
    }

    inner class MyViewHolder(private val binding: ListItemBitCoinBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: PriceEntry) {
            binding.tvTime.text = "Time: ${item.timestamp.toFormattedDateString()}"
            binding.tvPrice.text = "Price: ${item.price}"
        }
    }

    companion object {
        private val CryptoPriceDiffCallback = object : DiffUtil.ItemCallback<PriceEntry>() {
            override fun areItemsTheSame(oldItem: PriceEntry, newItem: PriceEntry): Boolean {
                return oldItem.timestamp == newItem.timestamp
            }

            override fun areContentsTheSame(oldItem: PriceEntry, newItem: PriceEntry): Boolean {
                return oldItem == newItem
            }
        }
    }
}
