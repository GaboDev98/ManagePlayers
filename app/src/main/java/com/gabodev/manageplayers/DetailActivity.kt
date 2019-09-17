package com.gabodev.manageplayers

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gabodev.manageplayers.api.ApiService
import com.gabodev.manageplayers.db.DatabaseHandler
import com.gabodev.manageplayers.model.Player
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import kotlinx.android.synthetic.main.activity_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailActivity : AppCompatActivity() {

    lateinit var service: ApiService

    val dbHandler = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        var id = intent.getStringExtra("id")
        var playerId = intent.getStringExtra("id_player")

        Log.d("ACT_DETAIL", "id: " + id)
        Log.d("ACT_DETAIL", "playerId: " + playerId)

        /*val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("https://www.balldontlie.io/api/v1/")
            .build()

        service = retrofit.create<ApiService>(ApiService::class.java)

        var call = service.getPlayerById(playerId)

        call.enqueue(object : Callback<Player> {

            override fun onResponse(call: Call<Player>?, response: Response<Player>?) {
                // var player = response!!.body() as Player
                // editText_firstName.setText(player.first_name)
                // editText_lastName.setText(player.last_name)
                Toast.makeText(
                    this@DetailActivity,
                    "La información se consultó correctamente.",
                    Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<Player>, t: Throwable) {
                Toast.makeText(
                    this@DetailActivity,
                    "Ocurrió un error al consultar la información del jugador",
                    Toast.LENGTH_LONG).show()
            }
        })*/

        button_save.setOnClickListener {
            var player = Player()
            // player.id = id
            player.first_name = editText_firstName.text.toString()
            player.last_name = editText_lastName.text.toString()
            var response = dbHandler.updatePlayer(player)
            if (response) {
                Toast.makeText(
                    this@DetailActivity,
                    "La información se actualizó correctamente.",
                    Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(
                    this@DetailActivity,
                    "La información no se logró actualizar correctamente.",
                    Toast.LENGTH_LONG).show()
            }
        }
    }
}