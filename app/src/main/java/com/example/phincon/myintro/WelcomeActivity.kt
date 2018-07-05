package com.example.phincon.myintro

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var myViewPagerAdapter: MyViewPagerAdapter
    private lateinit var dotsLayout: LinearLayout
    private lateinit var dots: Array<TextView>
    private lateinit var layouts: IntArray
    private lateinit var btnSkip: Button
    private lateinit var btnNext: Button

    val viewPagerPageChangeListener = object : ViewPager.OnPageChangeListener{

        override fun onPageSelected(position: Int) {
            addBottomDots(position)

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.size - 1) {
                // last page. make button text to GOT IT
                btnNext.text = "GOT IT"
                btnSkip.visibility = View.GONE
            } else {
                // still pages are left
                btnNext.text = "NEXT"
                btnSkip.visibility = View.VISIBLE
            }
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        }

        override fun onPageScrollStateChanged(state: Int) {

        }
    }
   // private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Checking for first time launch - before calling setContentView()
       // prefManager = PrefManager(this)
       // if(!prefManager.isFirstTimeLaunch()){
        //    launchHomeScreen()
       //     finish()
       // }

        if(Build.VERSION.SDK_INT >= 21){
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        setContentView(R.layout.activity_welcome)

        //Kotlin developer generally doesn't use findViewById, we use kotlin import
        //I'll leave this to your discretion
//        viewPager = findViewById(R.id.view_pager) as ViewPager
//        dotsLayout = findViewById(R.id.layoutDots) as LinearLayout
//        btnSkip = findViewById(R.id.btn_skip) as Button
//        btnNext = findViewById(R.id.btn_next) as Button


        viewPager = view_pager
        dotsLayout = layoutDots
        btnSkip = btn_skip
        btnNext = btn_next

        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = intArrayOf(
                R.layout.introslide1,
                R.layout.introslide2,
                R.layout.introslide3
        )

        // adding bottom dots
        addBottomDots(0)

        // making notification bar transparent
        changeStatusBarColor()

        myViewPagerAdapter = MyViewPagerAdapter()
        viewPager.adapter = myViewPagerAdapter
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener)

        btnSkip.setOnClickListener {launchHomeScreen()}

        btnNext.setOnClickListener {
            // checking for last page
            // if last page home screen will be launched

            val current: Int = getItem(+1)
            if (current < layouts.size) {
                // move to next screen
                viewPager.currentItem = current
            } else {
                launchHomeScreen()
            }
        }
    }

    private fun addBottomDots(currentPage: Int){

        dots = Array(layouts.size, {TextView(this)})

        val colorsActive = resources.getIntArray(R.array.array_dot_active)
        val colorsInactive = resources.getIntArray(R.array.array_dot_inactive)

        dotsLayout.removeAllViews()
        for(i in 0 until dots.size)
        {
            dots[i] = TextView(this)
            dots[i].setText(Html.fromHtml("&#8226;")) // this function (Html.fromHtml) are deprecated
            dots[i].textSize = 35.toFloat()
            dots[i].setTextColor(colorsInactive[currentPage])
            dotsLayout.addView(dots[i])
        }

        if(dots.size > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage])

    }

    private fun getItem(i: Int): Int{
        return viewPager.currentItem + i
    }

    private fun launchHomeScreen(){
       // prefManager.setFirstTimeLaunch(false)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    /**
     * Making notification bar transparent
     */
    private fun changeStatusBarColor(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            this.window.statusBarColor = Color.TRANSPARENT
        }
    }

    /**
     * View pager adapter
     */
    inner class MyViewPagerAdapter: PagerAdapter() {

        private lateinit var layoutInflater: LayoutInflater

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = LayoutInflater.from(container.context)

            val view: View = layoutInflater.inflate(layouts[position], container, false)
            container.addView(view)

            return view
        }

        override fun getCount(): Int{
            return layouts.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view == obj
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view: View = `object` as View
            container.removeView(view)
        }
    }
}
