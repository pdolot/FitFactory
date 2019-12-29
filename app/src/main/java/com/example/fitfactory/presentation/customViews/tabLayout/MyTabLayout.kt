package com.example.fitfactory.presentation.customViews.tabLayout

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfactory.R
import com.example.fitfactory.presentation.base.BaseAdapter
import kotlinx.android.synthetic.main.tab_layout.view.*

class MyTabLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var title: String? = null
        set(value) {
            field = value
            tab_title.text = field
        }
    private var recyclerView: RecyclerView? = null
    private var leftIconDrawable: Drawable? = null
    private var rightIconDrawable: Drawable? = null
    private var iconPadding: Int = 0
    var iconColor: Int? = null
        set(value) {
            field = value
            value?.let {
                leftIcon.drawable?.setTint(it)
                rightIcon.drawable?.setTint(it)
            }

        }
    private var snapHelper: PagerSnapHelper? = null
    private var page = 0
        set(value) {
            if (field != value) {
                field = value
                val onPage = tab_indicator.itemCount - (tab_indicator.maxItemCountInRow * field)
                tab_indicator.itemInRow =
                    if (onPage >= tab_indicator.maxItemCountInRow) tab_indicator.maxItemCountInRow else onPage
            }
        }

    var rightIconClickListener: () -> Unit = {}
    var leftIconClickListener: () -> Unit = {}
    var addPositionChangeListener: (Int) -> Unit = {}

    private var scrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            var position = 0

            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                val lm = rv.layoutManager
                val snapView = snapHelper?.findSnapView(lm)
                snapView?.let {
                    position = lm?.getPosition(it) ?: 0
                    addPositionChangeListener(position)
                    page = position / tab_indicator.maxItemCountInRow
                    tab_indicator.activeItem = position % tab_indicator.maxItemCountInRow
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    (recyclerView.adapter as BaseAdapter).apply {
                        title = getTitle(position)
                        setCurrentItem(position)
                    }
                }
            }
        }

    init {
        View.inflate(context, R.layout.tab_layout, this)
        getAttrs(attrs, defStyleAttr)
        tab_indicator.setTabIndicatorListener(object : TabIndicator.TabIndicatorListener {
            override fun onItemSelected(position: Int) {
                recyclerView?.smoothScrollToPosition(page * tab_indicator.maxItemCountInRow + position)
            }
        })
        rightIcon.setOnClickListener { rightIconClickListener() }
        leftIcon.setOnClickListener { leftIconClickListener() }
    }

    fun setupWithRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        snapHelper = PagerSnapHelper()
        if (recyclerView.onFlingListener == null){
            snapHelper?.attachToRecyclerView(this.recyclerView)
        }
        (recyclerView.adapter as BaseAdapter).apply {
            onDataSetChanged = {
                recyclerView.removeOnScrollListener(scrollListener)
                val itemCount = recyclerView.adapter?.itemCount ?: 0
                tab_indicator.itemCount = itemCount
                if (itemCount == 0) {
                    tab_title.text = context.getString(R.string.data_not_given)
                } else {
                    recyclerView.addOnScrollListener(scrollListener)
                    recyclerView.scrollToPosition(0)
                    setCurrentItem(0)
                    title = getTitle(0)
                    addPositionChangeListener(0)
                    tab_indicator.activeItem = 0
                }
            }
        }

    }

    private fun getAttrs(attrs: AttributeSet?, defStyleAttr: Int) {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.MyTabLayout, defStyleAttr, 0)
        tab_indicator.bgColor = a.getColor(R.styleable.MyTabLayout_backgroundColor, ContextCompat.getColor(context, R.color.colorPrimaryDark))
        tab_indicator.indicatorRadius = a.getDimensionPixelSize(R.styleable.MyTabLayout_indicatorRadius, 18)
        tab_indicator.indicatorColor = a.getColor(R.styleable.MyTabLayout_indicatorColor, ContextCompat.getColor(context, R.color.colorPrimary))
        tab_indicator.activeIndicatorColor = a.getColor(R.styleable.MyTabLayout_activeIndicatorColor, ContextCompat.getColor(context, R.color.colorAccent))
        tab_indicator.itemCount = a.getInteger(R.styleable.MyTabLayout_itemCount, 3)
        tab_title.text = a.getString(R.styleable.MyTabLayout_tabTitle)
        tab_title.textSize = a.getDimensionPixelSize(R.styleable.MyTabLayout_textSize,12).toFloat()
        leftIconDrawable = a.getDrawable(R.styleable.MyTabLayout_leftIconDrawable)
        rightIconDrawable = a.getDrawable(R.styleable.MyTabLayout_rightIconDrawable)
        iconColor = a.getColor(R.styleable.MyTabLayout_iconColor, ContextCompat.getColor(context, R.color.colorAccent))
        iconPadding = a.getDimensionPixelSize(R.styleable.MyTabLayout_iconPadding, 0)
        a.recycle()

        iconColor?.let {
            leftIconDrawable?.setTint(it)
            rightIconDrawable?.setTint(it)
        }


        leftIconDrawable?.let { id ->
            leftIcon.also {
                it.setImageDrawable(id)
                it.visibility = View.VISIBLE
            }
        }
        rightIconDrawable?.let { id ->
            rightIcon.also {
                it.setImageDrawable(id)
                it.visibility = View.VISIBLE
            }
        }

        if (iconPadding != 0) {
            rightIcon.setPadding(iconPadding)
            leftIcon.setPadding(iconPadding)
        }

        tab_indicator.bgColor?.let { tab_titleBackground.setBackgroundColor(it) }
        tab_title.setPadding(
            0,
            (tab_indicator.indicatorRadius * 2.5).toInt(),
            0,
            tab_indicator.indicatorRadius * 2
        )
    }

}
