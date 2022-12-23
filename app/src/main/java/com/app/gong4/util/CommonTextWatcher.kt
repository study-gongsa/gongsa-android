package com.app.gong4.util

import android.text.Editable
import android.text.TextWatcher

class CommonTextWatcher(private val beforeChanged: ((CharSequence?, Int, Int, Int) -> Unit) = { _, _, _, _ -> },
                        private val onChanged: ((CharSequence?, Int, Int, Int) -> Unit) = { _, _, _, _ -> },
                        private val afterChanged: ((Editable?) -> Unit) = {}
) :TextWatcher{
    override fun beforeTextChanged(c: CharSequence?, start: Int, count: Int, after: Int) {
        beforeChanged(c,start,count,after)
    }

    override fun onTextChanged(c: CharSequence?, start: Int, count: Int, after: Int) {
        onChanged(c,start,count,after)
    }

    override fun afterTextChanged(e: Editable?) {
        afterChanged(e)
    }
}