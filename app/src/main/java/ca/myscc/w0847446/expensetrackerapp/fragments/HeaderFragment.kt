package ca.myscc.w0847446.expensetrackerapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ca.myscc.w0847446.expensetrackerapp.R
import ca.myscc.w0847446.expensetrackerapp.viewModel.BackgroundColor


/**
 * A simple [Fragment] subclass.
 * Use the [HeaderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HeaderFragment : Fragment() {

    private lateinit var headerText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_header, container, false)
        headerText = view.findViewById(R.id.headerText)
        return view
    }
    fun updateAirplaneText(t: Boolean){
        Log.d("UI-Update", "Fragment")
        if(t) {
            headerText.text = "Airplane Mode On"
        }else{
            headerText.text = "Expense Tracker"
        }


    }
    companion object {
        fun newInstance(): Fragment {
            return HeaderFragment()
        }
    }
}