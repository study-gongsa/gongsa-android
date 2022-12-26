package com.app.gong4

import com.app.gong4.model.ResponseRefreshTokenBody

interface AutoLoginView {
    fun onValidateSuccess(response: ResponseRefreshTokenBody)
    fun onValidateFail(code:Int)
}