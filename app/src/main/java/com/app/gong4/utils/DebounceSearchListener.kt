package com.app.gong4.utils

import android.os.Handler
import android.os.Looper
import android.widget.SearchView

class DebounceSearchListener (private val delay: Long = 1000L,private val onSearch: (String) -> Unit) : SearchView.OnQueryTextListener {

    private var handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null

    override fun onQueryTextSubmit(query: String?): Boolean {
        // 검색 버튼을 누르면 바로 검색을 실행합니다.
        query?.let { onSearch(it) }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        runnable?.let { handler.removeCallbacks(it) }

        runnable = Runnable {
            newText?.let { onSearch(it) }
        }
        handler.postDelayed(runnable!!, delay)

        return true
    }
}