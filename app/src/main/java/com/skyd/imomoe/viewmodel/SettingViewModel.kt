package com.skyd.imomoe.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.util.CoilUtils
import com.skyd.imomoe.App
import com.skyd.imomoe.R
import com.skyd.imomoe.database.getAppDataBase
import com.skyd.imomoe.database.getOfflineDatabase
import com.skyd.imomoe.util.showToast
import com.skyd.imomoe.util.coil.CoilUtil
import com.skyd.imomoe.util.formatSize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SettingViewModel : ViewModel() {
    var mldAllHistoryCount: MutableLiveData<Long> = MutableLiveData()
    var mldDeleteAllHistory: MutableLiveData<Boolean> = MutableLiveData()
    var mldClearAllCache: MutableLiveData<Boolean> = MutableLiveData()
    var mldCacheSize: MutableLiveData<String> = MutableLiveData()

    fun deleteAllHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getAppDataBase().historyDao().deleteAllHistory()
                getAppDataBase().searchHistoryDao().deleteAllSearchHistory()
                getOfflineDatabase().playRecordDao().deleteAll()
                mldDeleteAllHistory.postValue(true)
                getAllHistoryCount()
            } catch (e: Exception) {
                mldDeleteAllHistory.postValue(false)
                e.printStackTrace()
                (App.context.getString(R.string.delete_failed) + "\n" + e.message).showToast()
            }
        }
    }

    // 获取Glide磁盘缓存大小
    fun getCacheSize() {
        viewModelScope.launch(Dispatchers.IO) {
            mldCacheSize.postValue(
                try {
                    CoilUtils.createDefaultCache(App.context).directory.formatSize()
                } catch (e: Exception) {
                    e.printStackTrace()
                    "获取缓存大小失败"
                }
            )
        }
    }


    fun clearAllCache() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Glide
                CoilUtil.clearMemoryDiskCache()
                mldClearAllCache.postValue(true)
            } catch (e: Exception) {
                mldClearAllCache.postValue(false)
                e.printStackTrace()
                (App.context.getString(R.string.delete_failed) + "\n" + e.message).showToast()
            }
        }
    }

    fun getAllHistoryCount() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val count = getAppDataBase().historyDao().getHistoryCount() +
                        getAppDataBase().searchHistoryDao().getSearchHistoryCount() +
                        getOfflineDatabase().playRecordDao().getPlayRecordCount()
                mldAllHistoryCount.postValue(count)
            } catch (e: Exception) {
                mldAllHistoryCount.postValue(-1)
                e.printStackTrace()
            }
        }
    }

    companion object {
        const val TAG = "SettingViewModel"
    }
}