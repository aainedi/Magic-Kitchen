package com.example.magickitchenapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore


class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recipeArrayList: ArrayList<Recipe>
    private lateinit var myAdapter: MyAdapterSearch
    private lateinit var db: FirebaseFirestore
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchView = view.findViewById(R.id.searchView)
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(text: String): Boolean {
                filterRecipe(text)
                return true
            }
        })

        recyclerView = view.findViewById(R.id. recycleView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        recipeArrayList = arrayListOf()

        myAdapter = MyAdapterSearch(recipeArrayList) { clickedRecipe ->
            val intent = Intent(requireContext(), DisplayRecipe::class.java)
            intent.putExtra("recipeTitle", clickedRecipe.Title)
            startActivity(intent)
        }

        recyclerView.adapter = myAdapter


        view.findViewById<ImageView>(R.id.chickenicon).setOnClickListener {
            filterRecipesByCategory("Chicken")
        }
        view.findViewById<ImageView>(R.id.seafoodicon).setOnClickListener {
            filterRecipesByCategory("Seafood")
        }
        view.findViewById<ImageView>(R.id.eggicon).setOnClickListener {
            filterRecipesByCategory("Egg")
        }
        view.findViewById<ImageView>(R.id.riceicon).setOnClickListener {
            filterRecipesByCategory("Rice")
        }
        view.findViewById<ImageView>(R.id.pastaicon).setOnClickListener {
            filterRecipesByCategory("Pasta")
        }
        view.findViewById<ImageView>(R.id.beeficon).setOnClickListener {
            filterRecipesByCategory("Beef")
        }
        view.findViewById<ImageView>(R.id.vegetablesicon).setOnClickListener {
            filterRecipesByCategory("Vegetables")
        }
        view.findViewById<ImageView>(R.id.desserticon).setOnClickListener {
            filterRecipesByCategory("Dessert")
        }

        eventChangeListener()
    }

    private fun filterRecipe(text: String) {
        myAdapter.filterRecipe(text)
    }

    private fun filterRecipesByCategory(category: String) {
        val filteredRecipes = recipeArrayList.filter { it.Category == category }
        myAdapter.updateRecipes(filteredRecipes)
    }

    private fun eventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("Recipes").addSnapshotListener { value, error ->
            if (error != null) {
                Log.e("Firestore Error", "Error code: ${error.code}", error)
                return@addSnapshotListener
            }

            for (dc: DocumentChange in value?.documentChanges!!) {
                if (dc.type == DocumentChange.Type.ADDED) {
                    recipeArrayList.add(dc.document.toObject(Recipe::class.java))
                    myAdapter.notifyItemInserted(recipeArrayList.size - 1)
                }
            }
        }
    }
}
