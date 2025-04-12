package ca.myscc.w0847446.expensetrackerapp.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ca.myscc.w0847446.expensetrackerapp.R
import ca.myscc.w0847446.expensetrackerapp.viewModel.BackgroundColor
import ca.myscc.w0847446.expensetrackerapp.viewModel.ExpenseListViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [FooterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FooterFragment : Fragment() {
    private val backgroundColor: BackgroundColor by activityViewModels()
    private val expenseListViewModel: ExpenseListViewModel by activityViewModels()
    private lateinit var textView:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_footer, container, false)
        textView = view.findViewById(R.id.footerTextView)
        // Observe the shared data
        backgroundColor.backgroundColor.observe(viewLifecycleOwner) { value ->
            textView.setBackgroundColor(value)
        }
        /*expenseListViewModel.expenseList.observe(viewLifecycleOwner){list->
            textView.text = "Total Expenses: $%.2f".format(list.sumOf { it.amount })
        }*/
        return view
    }
    companion object {
        fun newInstance(): Fragment {
            return FooterFragment()
        }
    }
    fun updateTotalExpensesDisplay(totalExpenses: Double) {
        textView.text = "Total Expenses: $%.2f".format(totalExpenses)
    }
}