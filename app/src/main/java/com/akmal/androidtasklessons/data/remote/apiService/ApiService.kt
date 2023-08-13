package com.akmal.androidtasklessons.data.remote.apiService

import com.akmal.androidtasklessons.data.remote.model.response.LessonsResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("test-api/lessons")
    suspend fun getOffers(): Response<LessonsResponse>

}