package android.com.xlab

import android.com.xlab.databinding.ActivityMainBinding
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders


class MainActivity : AppCompatActivity() {
    lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        viewModel.getBoxCountLiveData().observe(this, Observer { autoAdjustItem ->
            activityMainBinding.autoAdjust.removeAllViews()
            var boxWidth = autoAdjustItem.width
            for (i in 1..autoAdjustItem.noOfBox) {
                val tv = TextView(this@MainActivity)
                tv.text = i.toString()
                tv.gravity = Gravity.CENTER
                tv.setBackgroundDrawable(resources.getDrawable(R.drawable.child_item_bg))
                tv.layoutParams = ViewGroup.LayoutParams(boxWidth, autoAdjustItem.height)
                activityMainBinding.autoAdjust.addView(tv)

                boxWidth += autoAdjustItem.widthIncrement
            }
        })

        activityMainBinding.apply {
            bt1.setOnClickListener {
                viewModel.onButton1Click()
                hideKeyboard()
            }
            bt2.setOnClickListener {
                viewModel.onButton2Click()
                hideKeyboard()
            }
            inputBox.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    p0?.let {
                        var inputStr = it.toString()
                        if (inputStr.isBlank()) inputStr = "0"
                        viewModel.onAmountEntered(inputStr)
                    }
                }
            })

        }
    }

    private fun hideKeyboard() {
        try {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: Exception) {

        }
    }
}
