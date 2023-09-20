package it.step.moviecatalog.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import it.step.moviecatalog.R
import it.step.moviecatalog.model.Movie

class MovieAdapter(
    private val data: List<Movie>,
    private val onItemClick: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {


    inner class ViewHolder(
        itemView: View,
        onItemClicked: (Movie) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        var cardTitle: TextView = itemView.findViewById(R.id.card_title)
        var cardDescription: TextView = itemView.findViewById(R.id.card_description)
        var cardImage : ImageView = itemView.findViewById(R.id.card_image)


        fun bind(model: Movie) {
            //bind data with the component
            itemView.setOnClickListener {
                // this will be called only once.
                onItemClick.invoke(model)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ViewHolder {
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.card_movie, parent, false)
        return ViewHolder(viewHolder) {
            onItemClick(it)
        }
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.cardTitle.text = abbreviateString(data[position].title,20)
        viewHolder.cardDescription.text = data[position].plot?.let { abbreviateString(it,80) }
        viewHolder.cardImage.load(data[position].poster)
        viewHolder.bind(data[position])
    }

    private fun abbreviateString(string: String, dim : Int) : String{
        var newString = string
        if(string.length >= dim){
            newString = string.substring(0, dim - 3) + "..."
        }
        return newString
    }

}