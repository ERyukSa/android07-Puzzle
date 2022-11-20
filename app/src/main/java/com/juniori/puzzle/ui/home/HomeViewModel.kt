package com.juniori.puzzle.ui.home

import android.location.Address
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juniori.puzzle.data.auth.AuthRepository
import com.juniori.puzzle.data.weather.WeatherItem
import com.juniori.puzzle.data.weather.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _welcomeText = MutableLiveData("")
    val welcomeText: LiveData<String> = _welcomeText

    private val _weatherInfoText = MutableLiveData("날씨 보기")
    val weatherInfoText: LiveData<String> = _weatherInfoText

    private val _displayName = MutableLiveData("")
    val displayName: LiveData<String> = _displayName

    private val _currentAddress = MutableLiveData("")
    val currentAddress: LiveData<String> = _currentAddress

    private val _weatherList = MutableLiveData<List<WeatherItem>>(emptyList())
    val weatherList: LiveData<List<WeatherItem>> = _weatherList

    private val _weatherMainList = MutableLiveData<WeatherItem>().apply {
        WeatherItem("", "", 0, 0, 0, 0, "", "")
    }
    val weatherMainList: LiveData<WeatherItem> = _weatherMainList

    fun setDisplayName() {
        _displayName.value = "${authRepository.currentUser?.displayName}님"
    }

    fun setWelcomeText(text: String) {
        _welcomeText.value = text
    }

    fun setWeatherInfoText(text: String) {
        _weatherInfoText.value = text
    }

    fun setCurrentAddress(address: List<Address>) {
        _currentAddress.value = if (address[0].locality == null) {
            "${address[0].adminArea} ${address[0].thoroughfare}"
        } else {
            "${address[0].adminArea} ${address[0].locality} ${address[0].thoroughfare}"
        }
    }

    fun getWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val result = repository.getWeather(latitude, longitude)
            if (result.isSuccess) {
                val list = result.getOrDefault(emptyList())
                _weatherMainList.value = list[0]
                _weatherList.value = list.subList(1, list.size)
                _weatherInfoText.value = ""
            } else {
                _weatherInfoText.value = result.exceptionOrNull()?.message
            }
        }
    }

}
