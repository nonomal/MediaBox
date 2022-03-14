package com.su.mediabox.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.su.mediabox.bean.SearchHistoryBean
import com.su.mediabox.config.Const.Database.AppDataBase.SEARCH_HISTORY_TABLE_NAME

@Dao
interface SearchHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSearchHistory(searchHistoryBean: SearchHistoryBean)

    //按照时间戳顺序，从大到小。最后搜索的元组在最上方（下标0）显示
    @Query(value = "SELECT * FROM $SEARCH_HISTORY_TABLE_NAME ORDER BY timeStamp DESC")
    fun getSearchHistoryList(): List<SearchHistoryBean>

    //按照时间戳顺序，从大到小。最后搜索的元组在最上方（下标0）显示
    @Query(value = "SELECT * FROM $SEARCH_HISTORY_TABLE_NAME ORDER BY timeStamp DESC")
    fun getSearchHistoryListLiveData(): LiveData<List<SearchHistoryBean>>

    @Update
    fun updateSearchHistory(searchHistoryBean: SearchHistoryBean)

    @Query(value = "DELETE FROM $SEARCH_HISTORY_TABLE_NAME WHERE title = :title")
    fun deleteSearchHistory(title: String)

    @Query(value = "DELETE FROM $SEARCH_HISTORY_TABLE_NAME")
    fun deleteAllSearchHistory()

    // 获取记录条数
    @Query(value = "SELECT COUNT(1) FROM $SEARCH_HISTORY_TABLE_NAME")
    fun getSearchHistoryCount(): Long
}