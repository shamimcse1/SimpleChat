package net.beingup.simplechat.ChatFragments



import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.ArrayList

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragments: ArrayList<Fragment> = ArrayList()
    private val titles: ArrayList<String> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    fun addFragment(f: Fragment, t: String) {
        fragments.add(f)
        titles.add(t)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }
}
