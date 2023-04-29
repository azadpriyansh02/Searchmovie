package com.example.searchmovie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {
    lateinit var requestQueue: RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val appnetwork = BasicNetwork(HurlStack())
        val appcache = DiskBasedCache(cacheDir, 1024 * 1024) // 1MB cap
        requestQueue = RequestQueue(appcache, appnetwork).apply {
            start()
        }

        val search=findViewById<Button>(R.id.search)
        search.setOnClickListener {
            val userinput=findViewById<EditText>(R.id.userinput)
            var input = userinput.text.toString()
            fetchData(input)
        }


    }

    fun fetchData( input: String){
        val userinput=findViewById<EditText>(R.id.userinput)
        val search=findViewById<Button>(R.id.search)
        val url = "http://www.omdbapi.com/?t=${input}&apikey=5dbb298b"
        Log.d("link:",url)
        val name=findViewById<TextView>(R.id.name)
        val plot=findViewById<TextView>(R.id.plot)
        val image=findViewById<ImageView>(R.id.image)
        val error=findViewById<TextView>(R.id.error)
        val rating=findViewById<TextView>(R.id.rating)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                if(response.get("Response")=="False"){
                    userinput.visibility=View.INVISIBLE
                    search.visibility=View.INVISIBLE
                    error.text = "NO MOVIE FOUND"
                    error.visibility=View.VISIBLE
                }else {
                    userinput.visibility=View.INVISIBLE
                    search.visibility=View.INVISIBLE
                    Glide.with(this).load(response.getString("Poster")).into(image)
                    plot.text = "Plot:"+"\n\n"+response.getString("Plot")+"\n\n"
                    rating.text= "Rating:"+response.getString("imdbRating")
                    name.text = response.getString("Title")+"\n\n"+"Writer: "+response.getString("Writer")+"\n\n"
                }
            },
            { error ->
                Log.d("vol",error.toString())
            }
        )

        requestQueue.add(jsonObjectRequest)
    }
}