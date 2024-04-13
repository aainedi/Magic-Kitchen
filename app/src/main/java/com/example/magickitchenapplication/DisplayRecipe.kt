package com.example.magickitchenapplication

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.BulletSpan
import android.text.style.LeadingMarginSpan
import com.example.magickitchenapplication.R

class DisplayRecipe : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var recipeTitle: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.list_recipe)

        // Retrieve the recipe title from the intent
        recipeTitle = intent.getStringExtra("recipeTitle") ?: ""

        // Load and display recipe details
        loadAndDisplayRecipeDetails(recipeTitle)
    }

    private fun loadAndDisplayRecipeDetails(recipeTitle: String) {
        Log.d("DisplayRecipe", "Searching for recipe with title: $recipeTitle")
        if (recipeTitle.isNotEmpty()) {
            val db = FirebaseFirestore.getInstance()
            db.collection("Recipes")
                .whereEqualTo("Title", recipeTitle)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        val document = querySnapshot.documents[0]
                        val recipe = document.toObject(Recipe::class.java)
                        if (recipe != null) {
                            // If recipe found, display its details
                            displayRecipeDetails(recipe)
                        } else {
                            Log.e("DisplayRecipe", "Recipe is null")
                        }
                    } else {
                        Log.e("DisplayRecipe", "No recipe found with title: $recipeTitle")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("DisplayRecipe", "Error getting recipe", exception)
                }
        } else {
            Log.e("DisplayRecipe", "Recipe title is empty")
        }
    }

    private fun displayRecipeDetails(recipe: Recipe) {
        val titleTextView = findViewById<TextView>(R.id.tvTitle)
        val ingredientsTextView = findViewById<TextView>(R.id.tvIngredients)
        val stepsTextView = findViewById<TextView>(R.id.tvSteps)
        val imageView = findViewById<ImageView>(R.id.imageView)

        titleTextView.text = recipe.Title
        ingredientsTextView.text = formatIngredients(recipe.Ingredients)
        stepsTextView.text = formatSteps(recipe.Steps)

        Glide.with(this)
            .load(recipe.ImageUrl)
            .into(imageView)
    }


    private fun formatIngredients(ingredients: List<String>?): SpannableString {
        val ingredientsText = ingredients?.joinToString("\n\u2022 ") { it.trim() } ?: ""
        val bulletSpans = BulletSpan(16, Color.BLACK) // Set the desired bullet size and color
        val leadingMarginSpans = LeadingMarginSpan.Standard(0, 40) // Use a negative first line margin to create a hanging indent
        val spannableString = SpannableString("\u2022 $ingredientsText")
        spannableString.setSpan(bulletSpans, 0, spannableString.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(leadingMarginSpans, 0, spannableString.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        return spannableString
    }

    private fun formatSteps(steps: List<String>?): SpannableString {
        val stepsText = steps?.mapIndexed { index, step -> "${index + 1}. $step" }?.joinToString("\n") ?: ""
        val leadingMarginSpansSteps = LeadingMarginSpan.Standard(0, 60) // Use a negative first line margin for steps
        val spannableSteps = SpannableString(stepsText)
        spannableSteps.setSpan(leadingMarginSpansSteps, 0, spannableSteps.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        return spannableSteps
    }
}