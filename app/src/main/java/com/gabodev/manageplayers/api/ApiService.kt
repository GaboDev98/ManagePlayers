package com.gabodev.manageplayers.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import io.reactivex.Observable
import com.gabodev.manageplayers.model.Result
import com.gabodev.manageplayers.model.Player

interface ApiService {

    @GET("players")
    fun getAllPlayers(): Observable<Result>

    @GET("players/{id}")
    fun getPlayerById(@Path("id") id: Int): Call<Player>
}