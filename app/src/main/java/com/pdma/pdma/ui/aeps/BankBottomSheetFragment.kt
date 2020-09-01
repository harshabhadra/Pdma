package com.pdma.pdma.ui.aeps

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.pdma.pdma.R
import com.pdma.pdma.network.BankDetail
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class BankBottomSheetFragment(onBankItemClickListener: OnBankItemClickListener) :
    BottomSheetDialogFragment() {

    private lateinit var viewModel: BankBottomSheetViewModel
    private lateinit var bankListAdapter: BankListAdapter
    private var bankNameList: MutableList<BankDetail> = mutableListOf()
    private var searchNameList: MutableList<BankDetail> = mutableListOf()
    private var onBankItemClickListener: OnBankItemClickListener = onBankItemClickListener

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    interface OnBankItemClickListener {
        fun onBankItemClick(bankDetail: BankDetail)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bank_bottom_sheet_fragment, container, false)

        //Initializing ViewModel class
        viewModel = ViewModelProvider(this).get(BankBottomSheetViewModel::class.java)

        //Setting up bankListRecyclerView
        val bankListRecyclerView: RecyclerView = view.findViewById(R.id.bankListRecyclerView)
        bankListAdapter = BankListAdapter(BankListItemClickListener {
            onBankItemClickListener.onBankItemClick(it)
            dismiss()
        })
        bankListRecyclerView.adapter = bankListAdapter

        //Network call to get bank list
        viewModel.getBankList()

        //Observing bank list from ViewModel class
        viewModel.bankListLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                for(items in it){
                    if (items.aepsEnabled == "1"){
                        bankNameList.add(items)
                        searchNameList.add(items)
                    }
                }
                bankListAdapter.submitList(bankNameList)
            }
        })


        //Add TextWatcher to the searchNameEditText
        val searchEditText: TextInputEditText = view.findViewById(R.id.bank_search_editText)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    if (s.isNotEmpty()) {
                        search(it.toString().toLowerCase(Locale.ROOT))
                    }
                }
            }
        })
        //Set onClickListener to close imageView
        val imageView: ImageView = view.findViewById(R.id.bank_sheet_close_iv)
        imageView.setOnClickListener {
            dismiss()
        }

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        val view = View.inflate(context, R.layout.bank_bottom_sheet_fragment, null)
        val rootLayout: LinearLayout = view.findViewById(R.id.root_layout)
        val params: LinearLayout.LayoutParams =
            rootLayout.layoutParams as (LinearLayout.LayoutParams)
        params.height = getScreenHeight()
        rootLayout.layoutParams = params

        bottomSheetDialog.setContentView(view)
        bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
        return bottomSheetDialog
    }

    override fun onStart() {
        super.onStart()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    //Get Screen Height
    private fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    fun search(name: String) {
        bankNameList.clear()
        for (item in searchNameList) {
            if (item.bankName.toLowerCase(Locale.ROOT).contains(name)) {
                bankNameList.add(item)
            }
            bankListAdapter.submitList(bankNameList)
            bankListAdapter.notifyDataSetChanged()
        }
    }
}