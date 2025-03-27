package ca.myscc.w0847446.expensetrackerapp.fragments

import android.icu.util.Currency
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import ca.myscc.w0847446.expensetrackerapp.data.ExpenseItem
import ca.myscc.w0847446.expensetrackerapp.R


/**
 * A simple [Fragment] subclass.
 * Use the [DetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailFragment : Fragment() {

    private lateinit var name: TextView
    private lateinit var expenseAmount: TextView
    private lateinit var expenseDate: TextView
    private lateinit var backHome: Button
    private lateinit var item: ExpenseItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        // Get the counter value from the intent
//        val item = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            intent.getSerializableExtra("DETAIL", ExpenseItem::class.java)
//        } else {
//            @Suppress("DEPRECATION")
//            intent.getSerializableExtra("DETAIL") as? ExpenseItem
//        }

        // Retrieve List data in arguments from bundle
        arguments?.let {
            val name = it.getString("name", "")
            val expenseAmount = it.getString("expenseAmount", "")
            val expenseDate = it.getString("expenseDate", "")
            val currency = it.getString("currency", "CAD") ?: "CAD"
            val convertedCost = it.getString("convertedCost", "0")
            if (name != null) {
               item = ExpenseItem(name,expenseAmount.toDouble(), expenseDate,
                   Currency.getInstance(currency), convertedCost.toDouble() )
            }
        }
        //get text view and button
        name = view.findViewById(R.id.detailName)
        expenseAmount = view.findViewById(R.id.detailAmount)
        expenseDate = view.findViewById(R.id.detailDate)
        backHome = view.findViewById(R.id.backHome)

        //show the detail from item passed by main activity
        name.setText("Expense Name: ${item?.name}")
        expenseAmount.setText("Expense Amount: ${item?.amount.toString()}")
        expenseDate.setText("Expense Date: ${item?.date.toString()}")

        //go back to home activity
        backHome.setOnClickListener {

            val navController = findNavController()
            // Pass the bundle to the previous fragment to add to the list
            //navController.previousBackStackEntry?.savedStateHandle?.set("newTask", bundle) // Pass the bundle instead of Task
            // Navigate back to the previous fragment
            navController.popBackStack()
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment DetailFragment.
         */

        @JvmStatic
        fun newInstance(): Fragment{
            return DetailFragment()
        }

    }
}