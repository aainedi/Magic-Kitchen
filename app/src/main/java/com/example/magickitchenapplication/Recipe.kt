package com.example.magickitchenapplication

data class Recipe(var ImageUrl: String ?= null, var Title: String ?= null, var Ingredients: List<String>? = null, var Steps: List<String>? = null, var Category: String ?= null)

