package com.gabodev.manageplayers

import android.os.Bundle
import retrofit2.Retrofit
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gabodev.manageplayers.api.ApiService
import com.gabodev.manageplayers.db.DatabaseHandler
import com.gabodev.manageplayers.model.Player
import com.gabodev.manageplayers.model.PlayerItemAdapter
import com.google.gson.GsonBuilder
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.android.synthetic.main.activity_main.*
import io.reactivex.android.schedulers.AndroidSchedulers
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    val TAG_LOGS = "API_REST"

    lateinit var service: ApiService

    // Init db
    val dbHandler = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var listPlayers: List<Player>

        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("https://www.balldontlie.io/api/v1/")
            .build()

        service = retrofit.create<ApiService>(ApiService::class.java)

        rv_list_players.layoutManager = LinearLayoutManager(this)

        var response = service.getAllPlayers()

        response.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe ({ result ->
                listPlayers = result.data as List<Player>

                listPlayers.forEach {
                    val player = Player()
                    var success: Boolean
                    player.id_player = it.id
                    player.first_name = it.first_name
                    player.last_name = it.last_name
                    success = dbHandler!!.addPlayer(player)
                    if (success) {
                        Log.d(TAG_LOGS,  (it.first_name + "  " + it.last_name)+ " se registró correctamente.")
                    } else {
                        Log.d(TAG_LOGS,  (it.first_name + "  " + it.last_name)+ " no se logró registrar correctamente.")
                    }
                }

                Log.d(TAG_LOGS, "Se encontraron ${result.data!!.size} jugadores en el servicio.")

                var listPlayersBd = dbHandler.getAllPlayers()

                Log.d(TAG_LOGS, "Se encontraron ${listPlayersBd.size} jugadores en la bd local.")

                rv_list_players.adapter = PlayerItemAdapter(listPlayersBd, this)
            }, {
                    error -> error.printStackTrace()
            })

        searchBar.addTextChangeListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }
            override fun onTextChanged(charSequence: CharSequence, p1: Int, p2: Int, p3: Int) {
                Log.d(TAG_LOGS, "onTextChanged: $charSequence")
                // Results
                var results = dbHandler.getAllPlayersByName(charSequence.toString())
                // Count Results
                Log.d(TAG_LOGS, "CountResults: ${results.size}")
                // Search Filter
                rv_list_players.adapter = PlayerItemAdapter(results, this@MainActivity)
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
        })
    }
}
