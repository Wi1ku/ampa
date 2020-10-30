package com.example.bmi

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bmi.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private var isImperial = false
    var history = ArrayList<Double>(10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("massET", binding.massET.text.toString())
        outState.putString("heightET", binding.heightET.text.toString())
        outState.putString("massTV", binding.massTV.text.toString())
        outState.putString("heightTV", binding.heightTV.text.toString())
        outState.putString("bmiTV", binding.bmiTV.text.toString())
        outState.putBoolean("IsImperial", isImperial)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.apply {
            massTV.text = savedInstanceState.getString("massTV")
            heightTV.text = savedInstanceState.getString("heightTV")
            if(savedInstanceState.getBoolean("IsImperial")){
                isImperial = true
            }
            massET.setText(savedInstanceState.getString("massET"), TextView.BufferType.EDITABLE)
            heightET.setText(savedInstanceState.getString("heightET"), TextView.BufferType.EDITABLE)
            val bmi = savedInstanceState.getString("bmiTV")
            if(bmi != null){
                setBmi(bmi.toDouble(), binding.root)
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.units_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.selectMetric -> {
                setUnits("Metric")
                isImperial = false
                true
            }
            R.id.selectImperial -> {
                setUnits("Imperial")
                isImperial = true
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showBmiDetails(view: View){
        val LAUNCH_SECOND_ACTIVITY = 1
        val i = BmiDetails.newIntent(this, binding.bmiTV.text.toString())
        startActivityForResult(i, LAUNCH_SECOND_ACTIVITY)
    }


    fun count(view: View) {
            binding.apply {
                //TODO oprogramowac liczenie bmi i sprawdzanie danych wejsciowych lepiej
                val bmiCounter = BMICounter()
                if (heightET.text.isBlank()) {
                    heightET.error = getString(R.string.height_is_empty)
                }
                if (massET.text.isBlank()) {
                    massET.error = getString(R.string.mass_is_empty)
                }
                if (bmiCounter.checkForCorrectValues(massET.text.toString().toDouble(), heightET.text.toString().toDouble(), isImperial)){
                    setBmi(bmiCounter.countBmi(massET.text.toString().toDouble(), heightET.text.toString().toDouble(), isImperial), view)

                    }
                else{
                    val toast = Toast.makeText(applicationContext, R.string.invalid_input_data, Toast.LENGTH_SHORT)
                    toast.show()
                    }
                }
            }

    private fun setBmi(bmi: Double, view: View){
        binding.apply {
            bmiTV.text = bmi.toString()
            colourBmi(bmi, bmiTV)
            }
        }
    private fun setUnits(name: String){
        when(name){
            "Imperial" -> {
                binding.apply {
                    massTV.text = getString(R.string.mass_lb)
                    heightTV.text = getString(R.string.height_in)
                }
            }
            "Metric" -> {
                binding.apply {
                    massTV.text = getString(R.string.mass_kg)
                    heightTV.text = getString(R.string.height_cm)

                }
            }
        }
    }

    fun colourBmi(bmi: Double, textView: TextView){
        when {
            bmi < 16 -> textView.setTextColor(getColor(R.color.colorVeryServerlyUnderweight))
            (bmi >= 16) and (bmi < 17) -> textView.setTextColor(getColor(R.color.colorServerlyUnderweight))
            (bmi >= 17) and (bmi < 18.5) -> textView.setTextColor(getColor(R.color.colorUnderweight))
            (bmi >= 18.5) and (bmi < 26) -> textView.setTextColor(getColor(R.color.colorNormal))
            (bmi >= 25) and (bmi < 30) -> textView.setTextColor(getColor(R.color.colorOverweight))
            (bmi >= 30) and (bmi < 35) -> textView.setTextColor(getColor(R.color.colorModeratlyObese))
            (bmi >= 35) and (bmi < 40) -> textView.setTextColor(getColor(R.color.colorServerlyObese))
            bmi >= 40 -> textView.setTextColor(getColor(R.color.colorVeryServerlyObese))
        }
    }

}