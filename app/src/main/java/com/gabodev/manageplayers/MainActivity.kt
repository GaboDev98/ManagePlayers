package com.gabodev.manageplayers

import retrofit2.Call
import android.os.Bundle
import android.util.Log
import android.view.View
import retrofit2.Retrofit
import retrofit2.Response
import retrofit2.Callback
import com.google.gson.Gson
import android.widget.Toast
import android.view.ViewGroup
import android.content.Context
import android.widget.TextView
import android.widget.BaseAdapter
import android.view.LayoutInflater
import com.gabodev.manageplayers.model.Result
import androidx.appcompat.app.AppCompatActivity
import com.gabodev.manageplayers.api.ApiService
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val TAG_LOGS = "API_REST"

    lateinit var service: ApiService

    // Init db
    // val dbHandler = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://www.balldontlie.io/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create<ApiService>(ApiService::class.java)

        getAllPlayers()

        lv_players.adapter = ListMyAdapter(this)

        lv_players.setOnItemClickListener { parent, view, position, id ->

            Toast.makeText(this, "Clicked item : $position", Toast.LENGTH_SHORT).show()
            // Intent intent = new Intent(MainActivity.this, NextActivity.class);
            // intent.putExtra("position", position);
            // this.startActivity(intent);
        }
    }

    fun getAllPlayers() {
        // Sevicio que retorna el listado de jugadores
        service.getAllPlayers().enqueue(object: Callback<Result> {
            override fun onResponse(call: Call<Result>?, response: Response<Result>?) {
                val result = response?.body()
                Log.i(TAG_LOGS, Gson().toJson(result))
            }
            override fun onFailure(call: Call<Result>?, t: Throwable?) {
                t?.printStackTrace()
            }
        })
    }

    private class ListMyAdapter(context: Context) : BaseAdapter() {

        internal var listPlayers = arrayOf("DeVaughn", "Ike", "Carmelo", "Ron")

        private val mInflator: LayoutInflater = LayoutInflater.from(context)

        override fun getCount(): Int {
            return listPlayers.size
        }

        override fun getItem(position: Int): Any {
            return listPlayers[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
            val view: View?
            val vh: ListRowHolder
            if (convertView == null) {
                view = this.mInflator.inflate(R.layout.player_list_item, parent, false)
                vh = ListRowHolder(view)
                view.tag = vh
            } else {
                view = convertView
                vh = view.tag as ListRowHolder
            }

            vh.label.text = listPlayers[position]
            return view
        }
    }

    private class ListRowHolder(row: View?) {
        val label: TextView = row?.findViewById(R.id.player_label) as TextView
    }
}
