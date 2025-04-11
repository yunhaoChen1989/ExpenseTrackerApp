package ca.myscc.w0847446.expensetrackerapp.viewModel

import android.graphics.Color

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BackgroundColor: ViewModel() {
    var backgroundColor = MutableLiveData<Int>()

    fun changeBackground(){
        if(backgroundColor.value == Color.GREEN){
            backgroundColor.value = Color.BLUE
        }else{
            backgroundColor.value = Color.GREEN
        }
    }
}