package com.enfotrix.luckydoller.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.enfotrix.luckydoller.Fragments.FragmentActiveBids
import com.enfotrix.luckydoller.Fragments.FragmentClosedBids
import com.enfotrix.luckydoller.Fragments.FragmentPendingBids

class BidViewPagerAdapter (fragmentActivity: FragmentActivity, private var totalCount: Int) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return totalCount
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentPendingBids()
            1 -> FragmentActiveBids()
            2 -> FragmentClosedBids()


            else -> FragmentActiveBids()
        }
    }
}