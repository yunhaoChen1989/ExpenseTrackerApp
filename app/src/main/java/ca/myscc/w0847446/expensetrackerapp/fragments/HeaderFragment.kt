package ca.myscc.w0847446.expensetrackerapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.myscc.w0847446.expensetrackerapp.R
import ca.myscc.w0847446.expensetrackerapp.viewModel.BackgroundColor


/**
 * A simple [Fragment] subclass.
 * Use the [HeaderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HeaderFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_header, container, false)
        return view
    }
    companion object {
        fun newInstance(): Fragment {
            return HeaderFragment()
        }
    }
}