package com.example.magickitchenapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MyAdapterSearch(private var recipeList: List<Recipe>, private val onClick: (Recipe) -> Unit) : RecyclerView.Adapter<MyAdapterSearch.MyViewHolder>() {

    private var filteredRecipeList: List<Recipe> = recipeList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val recipeView = LayoutInflater.from(parent.context).inflate(R.layout.list_title, parent, false)
        return MyViewHolder(recipeView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val recipe = filteredRecipeList[position]

        Glide.with(holder.itemView.context)
            .load(recipe.ImageUrl)
            .into(holder.ImageUrl)

        holder.titleTextView.text = recipe.Title
        holder.itemView.setOnClickListener {
            onClick(recipe)
        }
    }

    override fun getItemCount(): Int {
        return filteredRecipeList.size
    }

    fun updateRecipes(newRecipeList: List<Recipe>) {
        recipeList = newRecipeList
        filteredRecipeList = newRecipeList
        notifyDataSetChanged()
    }

    fun filterRecipe(text: String) {
        filteredRecipeList = if (text.isEmpty()) {
            recipeList
        } else {
            recipeList.filter {
                it.Title?.contains(text, ignoreCase = true) == true
            }
        }
        notifyDataSetChanged()
    }



    public class MyViewHolder(recipeView: View) : RecyclerView.ViewHolder(recipeView) {
        val ImageUrl: ImageView = recipeView.findViewById(R.id.imageView)
        val titleTextView: TextView = recipeView.findViewById(R.id.tvTitle)
    }
}
