/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.marsrealestate.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.marsrealestate.network.MarsApi
import com.example.android.marsrealestate.network.MarsProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData String that stores the status of the most recent request
    private val _response = MutableLiveData<String>()

    // The external immutable LiveData for the request status String
    val response: LiveData<String>
        get() = _response

    // Coroutine Job
    private var viewmodelJob = Job()

    // Coroutine Scope
    private var coroutineScope = CoroutineScope(viewmodelJob + Dispatchers.Main)

    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        getMarsRealEstateProperties()
    }

    override fun onCleared() {
        super.onCleared()

        // Cancel any coroutines in progress
        viewmodelJob.cancel()
    }

    /**
     * Sets the value of the status LiveData to the Mars API status.
     */
    private fun getMarsRealEstateProperties() {
        coroutineScope.launch {

            // Request
            val request = MarsApi.retrofitService.getPropertiesAsync()

            try {

                // Result
                val result = request.await()

                // Act
                _response.value = "Success: ${result?.size} Mars Properties Received!"
            }
            catch (e : Exception)
            {
                _response.value = "Failure ${e.message}"
            }
        }
    }
}
