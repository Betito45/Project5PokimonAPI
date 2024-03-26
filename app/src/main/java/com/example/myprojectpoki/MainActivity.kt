package com.example.myprojectpoki

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private var petImageURL: String = "" // Declaring petImageURL variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button: Button = findViewById(R.id.petButton)
        val imageView: ImageView = findViewById(R.id.petImage)

        getNextImage(button, imageView)
    }

    private fun getPokemonImageURL() {
        val client = AsyncHttpClient()
        client.get("https://pokeapi.co/api/v2/pokemon?limit=1&offset=${(0..1050).random()}", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, json: JSONObject) {
                val results = json.getJSONArray("results")
                val randomPokemon = results.getJSONObject(0)
                val pokemonURL = randomPokemon.getString("url")
                getPokemonImage(pokemonURL)
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseString: String?, throwable: Throwable?) {
                Log.e("Pokemon Error", "Failed to fetch Pokemon data")
            }
        })
    }

    private fun getPokemonImage(url: String) {
        val client = AsyncHttpClient()
        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, json: JSONObject) {
                val sprites = json.getJSONObject("sprites")
                petImageURL = sprites.getString("front_default") // Assigning value to petImageURL
                Log.d("Pokemon", "Image URL: $petImageURL")
                // Load the image using Glide or any other image loading library
                loadImage(petImageURL)
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseString: String?, throwable: Throwable?) {
                Log.e("Pokemon Error", "Failed to fetch Pokemon image")
            }
        })
    }
    private fun getNextImage(button: Button, imageView: ImageView) {
        button.setOnClickListener {
            getPokemonImageURL()
        }
    }

    private fun loadImage(url: String) {
        Glide.with(this)
            .load(url)
            .fitCenter()
            .into(findViewById(R.id.petImage))
    }
}
