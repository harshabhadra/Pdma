package com.pdma.pdma.ui.plan

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.pdma.pdma.R
import com.pdma.pdma.network.RechargePlans
import com.pdma.pdma.network.Roffer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout

class PlanBottomSheetFragment(
    operatorCode: Int,
    circleCode: Int,
    operatorName: String,
    mobile: String,
    onPlanItemClickListener: OnPlanItemClickListener
) : BottomSheetDialogFragment() {

    private lateinit var viewModel: PlanBottomSheetViewModel
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var progressBar: ProgressBar

    interface OnPlanItemClickListener {
        fun onPlanItemClick(amount: Int)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.plan_bottom_sheet_fragment, container, false)

        viewPager2 = view.findViewById(R.id.plan_viewpager_2)
        tabLayout = view.findViewById(R.id.plan_tab_layout)
        progressBar = view.findViewById(R.id.plan_progress_bar)

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val bottomSheetDialog =
            super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view =
            View.inflate(context, R.layout.plan_bottom_sheet_fragment, null)

        val rootLayout = view.findViewById<LinearLayout>(R.id.plan_root_layout)
        val params =
            rootLayout.layoutParams as LinearLayout.LayoutParams
        params.height = getScreenHeight()
        rootLayout.layoutParams = params

        bottomSheetDialog.setContentView(view)
        bottomSheetBehavior =
            BottomSheetBehavior.from(view.parent as View)
        return bottomSheetDialog
    }

    override fun onStart() {
        super.onStart()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(PlanBottomSheetViewModel::class.java)

    }

    //Get Screen Height
    private fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    private fun setFragments(rechargePlans: RechargePlans, roffer: Roffer?) {
        if (roffer == null) {
            viewPager2.currentItem = 1
        }
    }
}