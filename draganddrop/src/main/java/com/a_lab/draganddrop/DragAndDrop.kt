package com.a_lab.draganddrop

import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener

enum class RectMethod {
    GLOBAL,
    HIT
}

enum class ViewCaptureMethod {
    VIEW_CAPTURE_METHOD_CENTER,
    VIEW_CAPTURE_METHOD_LEFT,
    VIEW_CAPTURE_METHOD_TOP,
    VIEW_CAPTURE_METHOD_RIGHT,
    VIEW_CAPTURE_METHOD_BOTTOM
}

class DragAndDrop(
    private val rootView: View,
    private val detectRectMethod: RectMethod = RectMethod.HIT,
    private val viewCaptureMethod: ViewCaptureMethod = ViewCaptureMethod.VIEW_CAPTURE_METHOD_CENTER
) {

    companion object {
        private const val CLICK_TIME = 250

    }

    private val dragViews = mutableListOf<View>()
    private val clickListenerMap = mutableMapOf<View, OnClickListener>()
    private val viewsForDisableClick = mutableListOf<View>()

    private var onDragAndDropListener: OnDragAndDropListener? = null
    private var draggingView: View? = null
    private var isFrameHard = false
    private var isReturnBack = false
    private var touchTime = 0L

    /**
     * Adds a drag-and-drop view.
     */
    fun addDragView(
        dragView: View,
        canMove: Boolean = true,
        canTouch: Boolean = true,
        l: OnClickListener? = null
    ): Boolean {
        return if (!dragViews.contains(dragView)) {
            dragViews.add(dragView)
            dragView.setTag(R.id.canMove, canMove)

            if (canTouch) {
                dragView.setOnTouchListener(touchListenerItem)
            }

            if(l != null) {
                clickListenerMap[dragView] = l
            }
            true
        } else {
            false
        }
    }

    /**
     * Remove a drag-and-drop view.
     */
    fun removeViewDrag(dragView: View) {
        dragView.setOnTouchListener(null)
        dragViews.remove(dragView)
    }

    fun addViewForResolvingConflictTouch(view: View) {
        if (!viewsForDisableClick.contains(view)) {
            viewsForDisableClick.add(view)
        }
    }

    fun removeViewForResolvingConflictTouch(view: View) {
        viewsForDisableClick.remove(view)
    }

    fun setFrameHard(isFrameHard: Boolean) {
        this.isFrameHard = isFrameHard
    }

    fun setReturnBack(isReturnBack: Boolean) {
        this.isReturnBack = isReturnBack
    }

    fun setOnDragListener(l: OnDragAndDropListener?) {
        onDragAndDropListener = l
    }

    private val rootTouchListener = object : View.OnTouchListener {
        private var rootWidth = 0
        private var rootHeight = 0
        private var startTouchX = 0f
        private var startTouchY = 0f
        private var startDragViewX = 0f
        private var startDragViewY = 0f
        private val views = mutableListOf<View>()
        private val rects = mutableListOf<Rect>()
        private val overlapViews = mutableListOf<View>()
        private val overlapWithTouchPointViews = mutableListOf<View>()


        override fun onTouch(view: View?, event: MotionEvent?): Boolean {

            if (draggingView != null)
            {
                when (event?.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        rootWidth = view?.width ?: 0
                        rootHeight = view?.height ?: 0

                        startDragViewX = draggingView!!.x
                        startDragViewY = draggingView!!.y

                        views.clear()
                        rects.clear()

                        dragViews.forEach { dragView ->
                            if (dragView != draggingView) {
                                val dragRect = Rect()
                                if (detectRectMethod == RectMethod.HIT)
                                    dragView.getHitRect(dragRect)
                                else
                                    dragView.getGlobalVisibleRect(dragRect)

                                views.add(dragView)
                                rects.add(dragRect)
                            }
                        }
                        views.reverse()
                        rects.reverse()
                    }

                    MotionEvent.ACTION_MOVE -> {
                        overlapViews.clear()
                        overlapWithTouchPointViews.clear()

                        if (draggingView?.getTag(R.id.canMove) as Boolean && (touchTime == 0L || System.currentTimeMillis() > touchTime + CLICK_TIME)) {
                            val touchX = event.x
                            val touchY = event.y

                            //Получаем ректангл перетаскиваемого view
                            val draggingRect = Rect()
                            if (detectRectMethod == RectMethod.HIT)
                                draggingView!!.getHitRect(draggingRect)
                            else
                                draggingView!!.getGlobalVisibleRect(draggingRect)

                            rects.forEachIndexed { index, rect ->
                                if ( !(rect.left > draggingRect.right || rect.right < draggingRect.left)
                                    && !(rect.bottom < draggingRect.top || rect.top > draggingRect.bottom) ) {
                                    overlapViews.add(views[index])
                                }

                                if (rect.left < draggingRect.centerX() && rect.right > draggingRect.centerX()
                                    && rect.top < draggingRect.centerY() + draggingRect.height() && rect.bottom > draggingRect.centerY() ) {
                                    overlapWithTouchPointViews.add(views[index])
                                }
                            }

                            if (onDragAndDropListener != null)  {
                                onDragAndDropListener!!.onObjectDrag(rootView, overlapViews, overlapWithTouchPointViews, draggingView!!, draggingView!!.x, draggingView!!.y)
                            }

                            if (isFrameHard) {
                                if ( (touchX - draggingView!!.width / 2f) > 0 && (touchX + draggingView!!.width / 2f) < rootWidth) {
                                    setViewPosition(touchX, -1f)
                                }
                                if((touchY - draggingView!!.getHeight() / 2f) > 0 && (touchY + draggingView!!.getHeight() / 2f) < rootHeight) {
                                    setViewPosition(-1f, touchY)
                                }
                            }
                            else {
                                setViewPosition(touchX, touchY)
                            }
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        //Если время клика больше 0 и и время клика + 1 сек меньше текущего времени
                        if (touchTime > 0 && touchTime + CLICK_TIME > System.currentTimeMillis()) {
                            clickListenerMap[draggingView]?.onClick(draggingView)
                        }
                        else {
                            if (isReturnBack) {
                                //Возвращаем объект на стартовое место
                                draggingView!!.x = startDragViewX
                                draggingView!!.y = startDragViewY
                            }
                        }

                        if (onDragAndDropListener != null)  {
                            onDragAndDropListener!!.onObjectDrop(rootView, overlapViews, overlapWithTouchPointViews, draggingView!!, draggingView!!.x, draggingView!!.y)
                        }
                        startDragViewX = 0f
                        startDragViewY = 0f
                        draggingView = null
                        touchTime = 0
                        setClickable(true)
                    }
                }
            } else {
                when (event!!.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        startTouchX = event.x
                        startTouchY = event.y
                    }

                    MotionEvent.ACTION_UP -> {
                        startTouchX = 0f
                        startTouchY = 0f
                    }
                }
            }

            return true
        }
    }

    private fun setViewPosition(touchX: Float, touchY: Float) {
        var offsetX = draggingView!!.width / 2f
        var offsetY = draggingView!!.width / 2f

        when (viewCaptureMethod) {
            ViewCaptureMethod.VIEW_CAPTURE_METHOD_CENTER -> {
                offsetX = draggingView!!.width / 2f
                offsetY = draggingView!!.height / 2f
            }

            ViewCaptureMethod.VIEW_CAPTURE_METHOD_LEFT -> {
                offsetX = 0f
                offsetY = draggingView!!.height / 2f
            }

            ViewCaptureMethod.VIEW_CAPTURE_METHOD_TOP -> {
                offsetX = draggingView!!.width / 2f
                offsetY = 0f
            }

            ViewCaptureMethod.VIEW_CAPTURE_METHOD_RIGHT -> {
                offsetX = draggingView!!.width.toFloat()
                offsetY = draggingView!!.height / 2f
            }

            ViewCaptureMethod.VIEW_CAPTURE_METHOD_BOTTOM -> {
                offsetX = draggingView!!.width / 2f
                offsetY = draggingView!!.height.toFloat()
            }
        }

        if (touchX != -1f) {
            draggingView!!.x = touchX - offsetX
        }

        if (touchY != -1f) {
            draggingView!!.y = touchY - offsetY
        }
    }

    private val touchListenerItem = object : View.OnTouchListener {

        override fun onTouch(view: View?, event: MotionEvent?): Boolean {
            when (event!!.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    if (clickListenerMap.containsKey(view)) {
                        touchTime = System.currentTimeMillis()
                    }
                    setClickable(false)
                    draggingView = view
                    if (onDragAndDropListener != null)  {
                        onDragAndDropListener!!.onObjectTouch(rootView, draggingView!!, view!!.x, view.y)
                    }
                }

                MotionEvent.ACTION_UP -> {
                    //Not executed (root is intercepted)
                }
            }
            return false
        }
    }

    private fun setClickable(isClickable: Boolean) {
        viewsForDisableClick.forEach { view ->
            view.isClickable = isClickable
        }
    }

    init {
        rootView.setOnTouchListener(rootTouchListener)
    }

    interface OnDragAndDropListener {
        fun onObjectTouch(rootView: View, draggingView: View, x: Float, y: Float)
        fun onObjectDrag(rootView: View, overlapViews: List<View>, overlapWithTouchPointViews: List<View>, draggingView: View, x: Float, y: Float)
        fun onObjectDrop(rootView: View, overlapViews: List<View>, overlapWithTouchPointViews: List<View>, draggingView: View, x: Float, y: Float)
    }

}