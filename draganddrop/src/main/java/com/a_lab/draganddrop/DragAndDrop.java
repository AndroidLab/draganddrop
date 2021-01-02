package com.a_lab.draganddrop;

import android.app.Activity;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DragAndDrop {

    private static final int CLICK_TIME = 1000;

    private final Activity activity;
    private final List<View> viewList = new ArrayList<>();
    private final List<View> viewDisableClickList = new ArrayList<>();
    private final HashMap<View, OnClickListener> clickListenerMap = new HashMap<>();
    private OnDragAndDropListener onDragAndDropListener;
    private View viewDrag;
    private boolean isFrameHard = false;   //Если жесткие рамки экрана
    private boolean isReturnBack = false;   //Всегда возвращать на исходную позицию
    private long timerClick;

    public DragAndDrop(Activity activity) {
        this(activity, null);
    }

    public DragAndDrop(Activity activity, View rootView) {
        this.activity = activity;

        //Устанавливаем слушатель touch на root
        if (rootView == null)
            activity.findViewById(android.R.id.content).getRootView().setOnTouchListener(touchListenerRoot);
        else
            rootView.setOnTouchListener(touchListenerRoot);
    }

    //Добавляем объекты target
    public void addViewDrag(View view) {
        addViewDrag(view, true);
    }

    //Добавляем объекты target
    public void addViewDrag(View view, boolean canMove) {
        addViewDrag(view, canMove, null);
    }

    //Добавляем объекты target
    public void addViewDrag(View view, boolean canMove, OnClickListener l) {
        //Если в массиве еще нет такой вью
        if (!viewList.contains(view)) {
            viewList.add(view);   //Добавляем в список объектов, которые могут пересекаться

            //Если объект может быть подвижным
            if(canMove) {
                view.setOnTouchListener(touchListenerItem);   //Устанавливаем слушатель прикосновения
            }

            //Если на объекте может быть клик
            if(l != null) {
                clickListenerMap.put(view, l);
            }
        }
    }

    //Добавить view для которых нужно отключать clickable listener
    public void addViewForResolvingConflictTouch(View view) {
        //Если в массиве еще нет такой вью
        if (!viewDisableClickList.contains(view)) {
            viewDisableClickList.add(view);   //Добавляем в список объектов, у котрых нужно отключать clickable
        }
    }

    //Удалить view для которых нужно отключать clickable listener
    public void removeViewForResolvingConflictTouch(View view) {
        viewDisableClickList.remove(view);   //Удаляем из списка объектов, у котрых нужно отключать clickable
    }

    private final View.OnTouchListener touchListenerRoot = new View.OnTouchListener() {
        private int screenWidth, screenHeight;
        private float startViewDragX, startViewDragY;
        private final List<View> targetViewList = new ArrayList<>();
        private final List<Rect> targetRectList = new ArrayList<>();
        private final List<View> overlapViewList = new ArrayList<>();

        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            //Если мы прикоснулись к объекту сдвига
            if (viewDrag != null)
            {
                switch (event.getActionMasked())
                {
                    case MotionEvent.ACTION_DOWN:
                        screenWidth = v.getWidth();
                        screenHeight = v.getHeight();

                        startViewDragX = viewDrag.getX();   //Начальная координата итема
                        startViewDragY = viewDrag.getY();   //Начальная координата итема

                        targetViewList.clear();
                        targetRectList.clear();

                        //Перебираем все вьюшки для определения их позиций на экране
                        for(View targetView : viewList) {
                            //Если view таргета не равна viewDrag
                            if (targetView != viewDrag) {
                                Rect targetRect = new Rect();
                                targetView.getGlobalVisibleRect(targetRect);
                                targetViewList.add(targetView);   //Записываем view таргета
                                targetRectList.add(targetRect);   //Записываем расположение таргета
                            }
                        }

                        break;

                    case MotionEvent.ACTION_MOVE:
                        //Если время клика равно 0 или текущее время больше чем время клика + 1 сек
                        if (timerClick == 0 || System.currentTimeMillis() > timerClick + CLICK_TIME) {

                            overlapViewList.clear();   //Должно очищаться здесь

                            float touchX = event.getX();   //Точка касания X
                            float touchY = event.getY();   //Точка касания Y

                            //Получаем ректангл перетаскиваемого view
                            Rect dragRect = new Rect();
                            viewDrag.getGlobalVisibleRect(dragRect);

                            //Перебираем все ректанглы таргета
                            for(int i = 0; i < targetRectList.size(); i++)
                            {
                                View targetView = targetViewList.get(i);   //View таргета
                                Rect targetRect = targetRectList.get(i);   //Позиция таргета

                                //Если ректанглы объектов НЕ пересекаются по оси X и Y
                                if ( !(targetRect.left > dragRect.right || targetRect.right < dragRect.left)
                                        && !(targetRect.bottom < dragRect.top || targetRect.top > dragRect.bottom) ) {
                                    overlapViewList.add(targetView);
                                }
                            }

                            if (onDragAndDropListener != null)  {
                                onDragAndDropListener.onObjectDrag(overlapViewList, viewDrag, viewDrag.getX(), viewDrag.getY());
                            }

                            //Если жесткие рамки экрана
                            if (isFrameHard) {
                                //Если не упираемся в рамки экрана по X
                                if ( (touchX - viewDrag.getWidth() / 2f) > 0 && (touchX + viewDrag.getWidth() / 2f) < screenWidth) {
                                    viewDrag.setX(touchX - viewDrag.getWidth() / 2f);
                                }
                                //Если не упираемся в рамки экрана по Y
                                if((touchY - viewDrag.getHeight() / 2f) > 0 && (touchY + viewDrag.getHeight() / 2f) < screenHeight) {
                                    viewDrag.setY(touchY - viewDrag.getHeight() / 2f);
                                }
                            }
                            else {
                                viewDrag.setX(touchX - viewDrag.getWidth() / 2f);
                                viewDrag.setY(touchY - viewDrag.getHeight() / 2f);
                            }
                        }

                        break;

                    case MotionEvent.ACTION_UP:
                        //Если время клика больше 0 и и время клика + 1 сек меньше текущего времени
                        if (timerClick > 0 && timerClick + CLICK_TIME > System.currentTimeMillis()) {
                            clickListenerMap.get(viewDrag).onClick(viewDrag);
                        }
                        else {
                            //Если не было пересечения ни с одним объектом
                            if (isReturnBack) {
                                //Возвращаем объект на стартовое место
                                viewDrag.setX(startViewDragX);
                                viewDrag.setY(startViewDragY);
                            }
                        }

                        if (onDragAndDropListener != null)  {
                            onDragAndDropListener.onObjectDrop(overlapViewList, viewDrag, viewDrag.getX(), viewDrag.getY());
                        }

                        viewDrag = null;
                        timerClick = 0;
                        setClickable(true);

                        break;
                }
            }
            return true;
        }
    };

    //Слушатель прикосновения к итему
    private final View.OnTouchListener touchListenerItem = new View.OnTouchListener()
    {
        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            switch (event.getActionMasked())
            {
                case MotionEvent.ACTION_DOWN:
                    //Если вью есть в списке слушателей на клик
                    if (clickListenerMap.containsKey(v)) {
                        timerClick = System.currentTimeMillis();   //Запоминаем текущее время прикосновения
                    }
                    setClickable(false);
                    viewDrag = v;

                    if (onDragAndDropListener != null)  {
                        onDragAndDropListener.onObjectTouch(viewDrag, v.getX(), v.getY());
                    }

                    break;

                case MotionEvent.ACTION_UP:
                    //Здесь не выполняется (Перехватывается root)
                    break;
            }
            return false;
        }
    };

    //Устанавливаем свойства clickable
    private void setClickable(boolean isClickable) {
        //Перебираем все view
        for(View view : viewDisableClickList) {
            view.setClickable(isClickable);
        }
    }

    //Установить жесткость рамок
    public void setFrameHard(boolean isFrameHard) {
        this.isFrameHard = isFrameHard;
    }

    //Возвращать объект на место
    public void setReturnBack(boolean isReturnBack) {
        this.isReturnBack = isReturnBack;
    }

    //Вызываем из класса в котором устанавливаем слушатель
    public void setDragListener(OnDragAndDropListener l) {
        this.onDragAndDropListener = l;
    }

    //функция возврата реализуемая в главном классе
    public interface OnDragAndDropListener {
        void onObjectTouch(View viewDrag, float x, float y);
        void onObjectDrag(List<View> overlapViewList, View viewDrag, float x, float y);
        void onObjectDrop(List<View> overlapViewList, View viewDrag, float x, float y);
    }

    //
    public interface OnClickListener {
        void onClick(View v);
    }

}
