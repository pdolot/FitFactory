package com.example.fitfactory.presentation.customViews.tabLayout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.fitfactory.R
import com.example.fitfactory.presentation.fragments.buyPassFragment.PassAdapter
import com.example.fitfactory.utils.Bound
import com.example.fitfactory.utils.scaleValue
import kotlinx.android.synthetic.main.tab_layout.view.*

class MyTabLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var viewPager: ViewPager? = null
    private var recyclerView: RecyclerView? = null
    private var snapHelper: PagerSnapHelper? = null
    private var bounds = Bound(0, 0, 0, 0)
    private var page = 0
        set(value) {
            if (field != value){
                field = value
                val onPage = tab_indicator.itemCount - (tab_indicator.maxItemCountInRow * field)
                tab_indicator.itemInRow = if ( onPage >= tab_indicator.maxItemCountInRow) tab_indicator.maxItemCountInRow else onPage
            }
        }

    init {
        View.inflate(context, R.layout.tab_layout, this)
        getAttrs(attrs)
        tab_indicator.setTabIndicatorListener(object : TabIndicator.TabIndicatorListener {
            override fun onItemSelected(position: Int) {
                viewPager?.setCurrentItem(position, false)
                recyclerView?.smoothScrollToPosition(page * tab_indicator.maxItemCountInRow + position)
            }
        })
    }

    fun setupWithViewPager(viewPager: ViewPager) {
        this.viewPager = viewPager
        tab_indicator.itemCount = viewPager.adapter?.count ?: 1
        setIndicator()
        setListeners()
    }

    fun setupWithRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        snapHelper = PagerSnapHelper()
        snapHelper?.attachToRecyclerView(this.recyclerView)
        tab_indicator.itemCount = recyclerView.adapter?.itemCount ?: 1
        setRecyclerListener()
        setIndicator()
    }

    private fun setRecyclerListener() {
        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var position = 0

            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                val lm = rv.layoutManager
                val snapView = snapHelper?.findSnapView(lm)
                snapView?.let {
                    position = lm?.getPosition(it) ?: -1
                    page = position / tab_indicator.maxItemCountInRow
                    setIndicatorBounds(position % tab_indicator.maxItemCountInRow)
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    (recyclerView.adapter as PassAdapter).apply {
                        tab_title.text = getTitle(position)
                        setCurrentItem(position)
                    }
                }
            }
        })
    }

    private fun setIndicator() {
        viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                setIndicatorBounds(0)
                tab_title.text = viewPager?.adapter?.getPageTitle(0)
                tab_title.text = recyclerView?.adapter?.let {
                    (it as PassAdapter).getTitle(0)
                }
            }
        })
    }

    private fun setIndicatorBounds(position: Int) {
        bounds = tab_indicator.getPositionAt(position)
        indicator.setBounds(
            bounds.left.toFloat(),
            bounds.top.toFloat(),
            bounds.right.toFloat(),
            bounds.bottom.toFloat()
        )
    }

    private fun setListeners() {
        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            var previousPosition = 0f
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                if (positionOffset in 0.05f..0.95f) {
                    if (previousPosition < positionOffset) {
                        tab_title.alpha = positionOffset.scaleValue(0.05f, 0.95f, 1f, 0f)
                    } else if (previousPosition > positionOffset) {
                        tab_title.alpha = positionOffset.scaleValue(0.05f, 0.95f, 0f, 1f)
                    }
                } else {
                    tab_title.alpha = 1f
                }
                previousPosition = positionOffset
            }

            override fun onPageSelected(position: Int) {
                setIndicatorBounds(position)
                tab_title.alpha = 1f
                tab_title.text = viewPager?.adapter?.getPageTitle(position)
            }
        })
    }

    private fun getAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.style
        )
        val count = typedArray.indexCount

        try {
            for (i in 0 until count) {
                when (val attr = typedArray.getIndex(i)) {
                    R.styleable.style_color -> {
                        tab_indicator.bgColor = typedArray.getColor(
                            attr,
                            ContextCompat.getColor(context, R.color.colorPrimaryDark)
                        )
                    }
                    R.styleable.style_indicatorRadius -> {
                        tab_indicator.indicatorRadius = typedArray.getDimensionPixelSize(attr, 18)
                        indicator.indicatorRadius = typedArray.getDimensionPixelSize(attr, 18)
                    }
                    R.styleable.style_indicatorColor -> {
                        tab_indicator.indicatorColor = typedArray.getColor(
                            attr,
                            ContextCompat.getColor(context, R.color.colorPrimary)
                        )
                    }
                    R.styleable.style_indicatorActiveColor -> {
                        indicator.indicatorColor = typedArray.getColor(
                            attr,
                            ContextCompat.getColor(context, R.color.colorAccent)
                        )
                    }
                    R.styleable.style_itemCount -> {
                        tab_indicator.itemCount = typedArray.getInteger(attr, 3)
                    }
                    R.styleable.style_text -> {
                        tab_title.text = typedArray.getString(attr)
                    }
                }
            }
        } finally {
            typedArray.recycle()
        }

        tab_indicator.bgColor?.let { tab_titleBackground.setBackgroundColor(it) }
        tab_title.setPadding(
            (indicator.indicatorRadius * 2.5).toInt(),
            (indicator.indicatorRadius * 2.5).toInt(),
            (indicator.indicatorRadius * 2.5).toInt(),
            indicator.indicatorRadius * 2
        )
    }
}