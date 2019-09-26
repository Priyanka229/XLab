package android.com.xlab

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class MainViewModel(app: Application): AndroidViewModel(app) {
    private val boxCountLiveData: MutableLiveData<AutoAdjustItem> = MutableLiveData()
    private var enteredAmount: Int = 0
    private val SCREEN_WIDTH = app.resources.displayMetrics.widthPixels

    companion object {
        const val WIDTH_INCREMENT = 20
        const val HEIGHT = 250
    }

    fun getBoxCountLiveData(): MutableLiveData<AutoAdjustItem> { return boxCountLiveData; }

    fun onAmountEntered(amountStr: String?) {
        amountStr?.let {
            enteredAmount = it.toInt()
        }
    }

    fun onButton1Click() {
        boxCountLiveData.postValue(AutoAdjustItem(enteredAmount, WIDTH_INCREMENT, HEIGHT, WIDTH_INCREMENT))
    }

    fun onButton2Click() {
        boxCountLiveData.postValue(AutoAdjustItem(enteredAmount, SCREEN_WIDTH / 4, HEIGHT, 0))
    }
}