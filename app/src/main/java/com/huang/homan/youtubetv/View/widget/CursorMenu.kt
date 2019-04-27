package com.huang.homan.youtubetv.View.widget

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.view.ViewCompat
import com.huang.homan.youtubetv.R
import com.huang.homan.youtubetv.View.widget.CursorMenu.Companion.FocusState.*
import com.huang.homan.youtubetv.View.widget.CursorMenu.Companion.WidthMode.SAME
import com.huang.homan.youtubetv.View.widget.CursorMenu.Companion.WidthMode.SELF
import com.hxxdashai.android.navigation.util.SoundUtil
import java.util.*
import com.huang.homan.youtubetv.View.widget.CursorMenu.Companion.WidthMode as ComHuangHomanYoutubetvViewWidgetCursorMenuWidthMode

/**
 * Original code created by Mr.T on 2018/3/27.
 * Enhanced code created by Homan Huang on 2019/4/27.
 */
class CursorMenu
        @JvmOverloads
        constructor(context: Context,
                    attrs: AttributeSet? = null,
                    defStyleAttr: Int = 0)
        : LinearLayout(context, attrs, defStyleAttr) {

    companion object {
        enum class FocusState { NoSelect, SelectNoFocus, SelectHasFocus }
        enum class WidthMode {
            SAME, //"Same Width"
            SELF //"Self Adjust Width"
        }

        /* Log tag and shortcut */
        private val TAG = "MYLOG " + CursorMenu::class.java.simpleName
        fun ltag(message: String) { Log.i(TAG, message) }
    }

    private var fontSize: Float = 0.0F
    private var enlargeRate: Float = 0.0f
    private var normalFontColor: Int = 0
    private var selectedFontColor: Int = 0
    private var outlineFontColor: Int = 0

    var startPosition: Int = 0 //default position

    private var widthMode: String = "" //item arrange mode
    private var itemSpace: Int = 0 //"same":item width，"self":item space

    init {
        if (attrs != null) {
            val attributes = context.obtainStyledAttributes(
                    attrs,
                    R.styleable.CursorMenu)
            fontSize = attributes.getDimension(
                    R.styleable.CursorMenu_FontSize, 20f)
            enlargeRate = attributes.getFloat(
                    R.styleable.CursorMenu_EnlargeRate, 1.1f)
            normalFontColor = attributes.getColor(
                    R.styleable.CursorMenu_NormalFontColor, Color.WHITE)
            selectedFontColor = attributes.getColor(
                    R.styleable.CursorMenu_SelectedFontColor, Color.BLUE)
            outlineFontColor = attributes.getColor(
                    R.styleable.CursorMenu_OutlineFontColor, Color.RED)
            startPosition = attributes.getInteger(
                    R.styleable.CursorMenu_StartPosition, 0)
            widthMode = attributes.getString(
                    R.styleable.CursorMenu_WidthMode) ?: SELF.toString()
            itemSpace = attributes.getDimensionPixelSize(
                    R.styleable.CursorMenu_ItemSpace, 20)
            attributes.recycle()
        }
        isFocusable = true
    }

    /**
     * Setting
     */
    var mDataList: MutableList<String> = ArrayList()
        set(value) {
            field = value
            initView()
        }

    //distance between this to the left of item
    //                                key  value
    private var cursorMap: MutableMap<Int, Int> = HashMap()

    // Present position
    var thisPos: Int = -1                           
    /**
     * Listen to the command
     */
    var cursorListener: CursorListener? = null
        set(value) { // setter
            field = value
            field?.onCursorChange(thisPos, KeyEvent.KEYCODE_DPAD_LEFT)
        }
    /**
     * Guidance cursor top
     */
    var mCursorViewTop: CursorView? = null
        set(value) {
            field = value
            cursorMap[thisPos]?.let { field?.fsatJumpTo(it) }
        }
    /**
     * Guidance cursor base
     */
    var mCursorViewBase: CursorView? = null
        set(value) {
            field = value
            cursorMap[thisPos]?.let { field?.fsatJumpTo(it) }
        }

    private fun initView() {
        if (cursorMap.isNotEmpty()) cursorMap.clear()
        if (mDataList.size > childCount) {
            do {
                val mTV = getItemView()
                addView(mTV)

            } while (mDataList.size > childCount)
        } else if (mDataList.size < childCount) {
            do {
                removeViewAt(childCount - 1)
            } while (mDataList.size < childCount)
        }
        if (thisPos != -1 && thisPos < childCount)
            changeItemState(thisPos, NoSelect)//default

        for (i in 0..(childCount - 1)) {
            val child = getChildAt(i) as TextView
            child.text = mDataList[i]
            child.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    child.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    // get locations
                    cursorMap[i] = child.width / 2 + child.left + this@CursorMenu.left//mid to left of item
                    //ltag("cursorMap[ $i ]: ${cursorMap[i]} = ${child.width} + ${child.left} + ${this@CursorMenu.left}")

                    if (startPosition == i) {//TODO No reset pos，change logic
                        thisPos = startPosition
                        changeItemState(thisPos, if (this@CursorMenu.isFocused) SelectHasFocus else SelectNoFocus)//default pos

                        mCursorViewTop?.let {
                            ltag("mCursorViewTop is not NULL.")
                            cursorMap[thisPos]?.let { mCursorViewTop?.fsatJumpTo(it) }//moving cursor top
                        }
                        mCursorViewBase?.let {
                            ltag("mCursorViewBase is not NULL.")
                            cursorMap[thisPos]?.let { mCursorViewBase?.fsatJumpTo(it) }//moving cursor top
                        }


                        cursorListener?.onCursorChange(thisPos, KeyEvent.KEYCODE_DPAD_LEFT)//left or right
                    }
                }
            })
        }
    }

    private fun changeItemState(pos: Int, state: FocusState) {
        val child = getChildAt(pos)
        if (child != null)
            when (state) {
                NoSelect -> {
                    //if (child.scaleX != 1f) ViewCompat.animate(child).scaleX(1f).scaleY(1f).translationZ(0f).start()//TODO BUG
                    ViewCompat.animate(child).scaleX(1f).scaleY(1f).translationZ(0f).start()
                    (child as TextView).setShadowLayer(0f, 0f, 0f, outlineFontColor)
                    child.isSelected = false
                }
                SelectNoFocus -> {
                    if (child.scaleX != 1f) ViewCompat.animate(child).scaleX(1f).scaleY(1f).translationZ(0f).start()
                    if (!child.isSelected) {
                        (child as TextView).setShadowLayer(25f, 0f, 0f, outlineFontColor)
                        child.isSelected = true
                    }
                }
                SelectHasFocus -> {
                    ViewCompat.animate(child).scaleX(enlargeRate).scaleY(enlargeRate).translationZ(0f).start()
                    if (!child.isSelected) {
                        (child as TextView).setShadowLayer(25f, 0f, 0f, outlineFontColor)
                        child.isSelected = true
                    }
                }
            }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event?.action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_DPAD_LEFT -> {
                    if (thisPos > 0) {
                        changeItemState(thisPos, NoSelect)
                        changeItemState(--thisPos, SelectHasFocus)

                        ltag("move Left pos: "+thisPos)

                        cursorMap[thisPos]?.let {
                            mCursorViewTop?.jumpTo(it)
                            mCursorViewBase?.jumpTo(it)
                        }

                        cursorListener?.onCursorChange(thisPos, keyCode)
                    } else {
                        thisPos = mDataList.size-1
                        changeItemState(0, NoSelect)
                        changeItemState(thisPos, SelectHasFocus)

                        ltag("move Left pos: "+thisPos)

                        cursorMap[thisPos]?.let {
                            mCursorViewTop?.jumpTo(it)
                            mCursorViewBase?.jumpTo(it)
                        }
                        cursorListener?.onCursorChange(thisPos, keyCode)
                    }
                    SoundUtil.playClickSound(this@CursorMenu)
                    return true //TODO block sound process
                }
                KeyEvent.KEYCODE_DPAD_RIGHT -> {
                    if (thisPos < childCount - 1) {
                        changeItemState(thisPos, NoSelect)
                        changeItemState(++thisPos, SelectHasFocus)

                        ltag("move Right pos: "+thisPos)

                        cursorMap[thisPos]?.let {
                            mCursorViewTop?.jumpTo(it)
                            mCursorViewBase?.jumpTo(it)
                        }

                        cursorListener?.onCursorChange(thisPos, keyCode)
                    } else {
                        thisPos = 0
                        changeItemState(mDataList.size-1, NoSelect)
                        changeItemState(thisPos, SelectHasFocus)

                        ltag("move Right pos: "+thisPos)

                        cursorMap[thisPos]?.let {
                            mCursorViewTop?.jumpTo(it)
                            mCursorViewBase?.jumpTo(it)
                        }

                        cursorListener?.onCursorChange(thisPos, keyCode)
                    }
                    SoundUtil.playClickSound(this@CursorMenu)
                    return true
                }
                KeyEvent.KEYCODE_DPAD_UP, KeyEvent.KEYCODE_DPAD_DOWN -> {
                    //TODO define focus object by return true
                    cursorListener?.onCursorChange(thisPos, keyCode)
                }
                KeyEvent.KEYCODE_MENU -> { //TODO Non directional event
                    cursorListener?.onCursorChange(thisPos, keyCode)
                    return true //TODO bug
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onFocusChanged(gainFocus: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        changeItemState(thisPos, if (gainFocus) SelectHasFocus else SelectNoFocus)

        mCursorViewTop?.visibility = if (gainFocus) View.VISIBLE else View.INVISIBLE
        mCursorViewBase?.visibility = if (gainFocus) View.VISIBLE else View.INVISIBLE

        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect)
    }

    private fun getItemView(): TextView {
        val states = arrayOf(intArrayOf(android.R.attr.state_selected), intArrayOf())
        val colors = intArrayOf(selectedFontColor, normalFontColor)
        val colorStateList = ColorStateList(states, colors)
        val textView = TextView(context)
        textView.textSize = fontSize
        textView.setTextColor(colorStateList)
        textView.includeFontPadding = false

        when (widthMode) {
            SAME.toString() -> {
                val layoutParams = LayoutParams(itemSpace, LayoutParams.WRAP_CONTENT)
                textView.layoutParams = layoutParams
                textView.gravity = Gravity.CENTER
            }
            SELF.toString() -> {
                textView.setPadding(itemSpace, 0, itemSpace, 0)
            }
        }
        return textView
    }

    /**
     * move to item
     */
    fun jumpTo(keyCode: Int) {
        if ((thisPos > 0 && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) || (thisPos < childCount - 1 && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)) {
            changeItemState(thisPos, NoSelect)
            changeItemState(if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) --thisPos else ++thisPos, SelectNoFocus)

            cursorMap[thisPos]?.let {
                mCursorViewTop?.jumpTo(it)
                mCursorViewBase?.jumpTo(it)
            }

            cursorListener?.onCursorChange(thisPos, KeyEvent.KEYCODE_DPAD_LEFT)
        }
    }

    interface CursorListener {
        fun onCursorChange(position: Int, keyCode: Int)
    }
}