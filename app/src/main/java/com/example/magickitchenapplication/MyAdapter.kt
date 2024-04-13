package com.example.magickitchenapplication

import android.content.Intent
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.BulletSpan
import android.text.style.LeadingMarginSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.magickitchenapplication.Recipe
import com.bumptech.glide.Glide

class MyAdapter(private val recipeList: List<Recipe>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        val recipeView = LayoutInflater.from(parent.context).inflate(R.layout.list_recipe, parent, false)
        return MyViewHolder(recipeView)
    }

    override fun onBindViewHolder(holder: MyAdapter.MyViewHolder, position: Int) {
        val recipe: Recipe = recipeList[position]

        Glide.with(holder.itemView.context)
            .load(recipe.ImageUrl)
            .into(holder.ImageUrl)

        holder.Title.text = recipe.Title

        val ingredientsText = recipe.Ingredients?.joinToString("\n\u2022 ") { it.trim() } ?:""
        val bulletSpans = BulletSpan(16, Color.BLACK)
        val leadingMarginSpans = LeadingMarginSpan.Standard(0, 30)
        val spannableString = SpannableString("\u2022 $ingredientsText")
        spannableString.setSpan(bulletSpans, 0, spannableString.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(leadingMarginSpans, 0, spannableString.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

        holder.Ingredients.text = spannableString

        val stepsText = recipe.Steps?.mapIndexed { index, step -> "${index + 1}. $step" }?.joinToString("\n") ?: ""
        val leadingMarginSpansSteps = LeadingMarginSpan.Standard(0, 50)
        val spannableSteps = SpannableString(stepsText)
        spannableSteps.setSpan(leadingMarginSpansSteps, 0, spannableSteps.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

        holder.Steps.text = spannableSteps

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DisplayRecipe::class.java)
            intent.putExtra("recipeTitle", recipe.Title)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    public class MyViewHolder(recipeView: android.view.View) : RecyclerView.ViewHolder(recipeView) {
        val ImageUrl: ImageView = recipeView.findViewById(R.id.imageView)
        val Title: TextView = recipeView.findViewById(R.id.tvTitle)
        val Ingredients: TextView = recipeView.findViewById(R.id.tvIngredients)
        val Steps: TextView = recipeView.findViewById(R.id.tvSteps)
    }
}