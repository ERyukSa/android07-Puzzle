package com.juniori.puzzle.ui.othersgallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juniori.puzzle.data.Resource
import com.juniori.puzzle.domain.entity.VideoInfoEntity
import com.juniori.puzzle.domain.usecase.*
import com.juniori.puzzle.util.SortType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OthersGalleryViewModel @Inject constructor(
    val getSocialVideoList: GetSocialVideoListUseCase,
    val getUserInfoUseCase: GetUserInfoUseCase,
    val getSearchedSocialVideoListUseCase: GetSearchedSocialVideoListUseCase
) : ViewModel() {
    private val _list = MutableLiveData<List<VideoInfoEntity>>()
    val list: LiveData<List<VideoInfoEntity>>
        get() = _list

    private val _refresh = MutableLiveData(false)
    val refresh: LiveData<Boolean>
        get() = _refresh

    var query = ""
    var sortType = SortType.NEW
    fun setQueryText(nowQuery: String?) {
        if (nowQuery.isNullOrBlank()) {
            query = ""
            getMyData()
            return
        }

        query = nowQuery
        val uid = getUid()

        if (uid == null) {
            //todo network err
        } else {
            viewModelScope.launch {
                val data = getSearchedSocialVideoListUseCase(0, query, sortType)
                if (data is Resource.Success) {
                    val result = data.result
                    _list.value = result//todo empty list
                } else {
                    //todo network err
                }
            }
        }
    }

    fun getPaging(start: Int) {
        if (refresh.value == true) {
            return
        }

        val uid = getUid()
        if (uid == null) {
            //todo network err
        } else {
            viewModelScope.launch {
                _refresh.value = true
                delay(500)
                val data = if (query.isBlank()) {
                    getSocialVideoList(start, sortType)
                } else {
                    getSearchedSocialVideoListUseCase(start, query, sortType)
                }

                if (data is Resource.Success) {
                    val result = data.result
                    addItems(result)//todo empty list
                } else {
                    //todo network err
                }

                _refresh.value = false
            }
        }
    }

    fun getMyData() {
        val uid = getUid()

        if (uid == null) {
            //todo network err
        } else {
            viewModelScope.launch {
                val data = getSocialVideoList(0, sortType)
                if (data is Resource.Success) {
                    val result = data.result
                    _list.postValue(result)//todo empty list
                } else {
                    //todo network err
                }
            }
        }
    }

    private fun getUid(): String? {
        val userInfo = getUserInfoUseCase()
        val uid: String? = if (userInfo is Resource.Success) {
            userInfo.result.uid
        } else {
            null
        }

        return uid
    }

    private fun addItems(items: List<VideoInfoEntity>) {
        val newList = mutableListOf<VideoInfoEntity>()
        _list.value?.forEach {
            newList.add(it)
        }

        items.forEach {
            newList.add(it)
        }

        _list.value = newList
    }

    fun setOrderType(type: SortType): Boolean{
        if(sortType!=type){
            sortType = type

            if(query.isBlank()){
                getMyData()
            }else{
                setQueryText(query)
            }

            return true
        }

        return false

    }
}
