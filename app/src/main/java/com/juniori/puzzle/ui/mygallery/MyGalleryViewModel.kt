package com.juniori.puzzle.ui.mygallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juniori.puzzle.data.Resource
import com.juniori.puzzle.domain.entity.VideoInfoEntity
import com.juniori.puzzle.domain.usecase.GetMyVideoListUseCase
import com.juniori.puzzle.domain.usecase.GetSearchedMyVideoUseCase
import com.juniori.puzzle.domain.usecase.GetUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyGalleryViewModel @Inject constructor(
    val getMyVideoListUseCase: GetMyVideoListUseCase,
    val getUserInfoUseCase: GetUserInfoUseCase,
    val getSearchedMyVideoUseCase: GetSearchedMyVideoUseCase
) : ViewModel() {
    private val _list = MutableLiveData<List<VideoInfoEntity>>()
    val list: LiveData<List<VideoInfoEntity>>
        get() = _list

    private val _refresh = MutableLiveData(false)
    val refresh: LiveData<Boolean>
        get() = _refresh

    private var query = ""

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
                val data = getSearchedMyVideoUseCase(uid, 0, query)
                if (data is Resource.Success) {
                    val result = data.result
                    if(result==null||result.isEmpty()){
                        _list.postValue(emptyList())
                    }else {
                        _list.postValue(result)
                    }
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
                val data = if (query.isBlank()) {
                    getMyVideoListUseCase(uid, start)
                } else {
                    getSearchedMyVideoUseCase(uid, start, query)
                }

                if (data is Resource.Success) {
                    val result = data.result
                    addItems(result)//empty list paging
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
                val data = getMyVideoListUseCase(uid, 0)
                if (data is Resource.Success) {
                    val result = data.result
                    if(result==null||result.isEmpty()){
                        _list.postValue(emptyList())
                    }else {
                        _list.postValue(result)
                    }
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
}
