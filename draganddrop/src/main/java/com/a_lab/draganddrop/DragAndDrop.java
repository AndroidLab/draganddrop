package com.a_lab.draganddrop;

import android.app.Activity;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DragAndDrop {

    private static final int CLICK_TIME = 1000;

    private final Activity activity;
    private final List<View> list_view = new ArrayList<>();
    private final List<View> list_viewDisableClick = new ArrayList<>();
    private final HashMap<View, OnClickListener> map_clickListener = new HashMap<>();
    private OnDragAndDropListener onDragAndDropListener;
    private View viewDrag;
    private boolean isFrameHard = false;   //Если жесткие рамки экрана
    private boolean isDragBack = true;   //Всегда возвращать на исходную позицию
    private long timerClick;

    public DragAndDrop(Activity activity) {
        this.activity = activity;
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
        if (!list_view.contains(view)) {
            list_view.add(view);   //Добавляем в список объектов, которые могут пересекаться

            //Если объект может быть подвижным
            if(canMove) {
                view.setOnTouchListener(touchListenerItem);   //Устанавливаем слушатель прикосновения
            }

            //Если на объекте может быть клик
            if(l != null) {
                map_clickListener.put(view, l);
            }
        }
    }

    //Добавить view для которых нужно отключать clickable listener
    public void addViewForResolvingConflictTouch(View view) {
        //Если в массиве еще нет такой вью
        if (!list_viewDisableClick.contains(view)) {
            list_viewDisableClick.add(view);   //Добавляем в список объектов, у котрых нужно отключать clickable
        }
    }

    private final View.OnTouchListener touchListenerRoot = new View.OnTouchListener() {
        private int screenWidth, screenHeight;
        private float startViewDragX, startViewDragY;
        private final List<View> list_targetView = new ArrayList<>();
        private final List<Rect> list_targetRect = new ArrayList<>();
        private final List<View> list_overlapView = new ArrayList<>();

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

                        list_targetView.clear();
                        list_targetRect.clear();

                        //Перебираем все вьюшки для определения их позиций на экране
                        for(View targetView : list_view) {
                            //Если view таргета не равна viewDrag
                            if (targetView != viewDrag) {
                                Rect targetRect = new Rect();
                                targetView.getGlobalVisibleRect(targetRect);
                                list_targetView.add(targetView);   //Записываем view таргета
                                list_targetRect.add(targetRect);   //Записываем расположение таргета
                            }
                        }

                        break;

                    case MotionEvent.ACTION_MOVE:
                        //Если время клика равно 0 или текущее время больше чем время клика + 1 сек
                        if (timerClick == 0 || System.currentTimeMillis() > timerClick + CLICK_TIME) {

                            list_overlapView.clear();   //Должно очищаться здесь

                            float touchX = event.getX();   //Точка касания X
                            float touchY = event.getY();   //Точка касания Y

                            //Получаем ректангл перетаскиваемого view
                            Rect dragRect = new Rect();
                            viewDrag.getGlobalVisibleRect(dragRect);

                            //Перебираем все ректанглы таргета
                            for(int i = 0; i < list_targetRect.size(); i++)
                            {
                                View targetView = list_targetView.get(i);   //View таргета
                                Rect targetRect = list_targetRect.get(i);   //Позиция таргета

                                //Если ректанглы объектов НЕ пересекаются по оси X
                                if (targetRect.left > dragRect.right || targetRect.right < dragRect.left)
                                    break;

                                //Если ректанглы объектов НЕ пересекаются по оси Y
                                if (targetRect.bottom < dragRect.top || targetRect.top > dragRect.bottom)
                                    break;

                                list_overlapView.add(targetView);
                            }

                            onDragAndDropListener.onObjectDrag(list_overlapView, viewDrag, viewDrag.getX(), viewDrag.getY());

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
                            map_clickListener.get(viewDrag).onClick(viewDrag);
                        }
                        else {
                            //Если не было пересечения ни с одним объектом
                            if (isDragBack) {
                                //Возвращаем объект на стартовое место
                                viewDrag.setX(startViewDragX);
                                viewDrag.setY(startViewDragY);
                            }
                        }

                        onDragAndDropListener.onObjectDrop(list_overlapView, viewDrag, viewDrag.getX(), viewDrag.getY());
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
                    if (map_clickListener.containsKey(v)) {
                        timerClick = System.currentTimeMillis();   //Запоминаем текущее время прикосновения
                    }
                    setClickable(false);
                    viewDrag = v;
                    onDragAndDropListener.onObjectTouch(viewDrag, v.getX(), v.getY());
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
        for(View view : list_viewDisableClick) {
            view.setClickable(isClickable);
        }
    }

    //Установить жесткость рамок
    public void setFrameHard(boolean isFrameHard) {
        this.isFrameHard = isFrameHard;
    }

    //Возвращать объект на место
    public void setDragBack(boolean isDragBack) {
        this.isDragBack = isDragBack;
    }

    //Вызываем из класса в котором устанавливаем слушатель
    public void startListener(DragAndDrop.OnDragAndDropListener l) {
        //Устанавливаем слушатель touch на root
        startListener(l , null);
    }

    //Вызываем из класса в котором устанавливаем слушатель
    public void startListener(OnDragAndDropListener l, View rootView) {
        //Устанавливаем слушатель touch на root
        if (rootView == null)
            activity.findViewById(android.R.id.content).getRootView().setOnTouchListener(touchListenerRoot);
        else
            rootView.setOnTouchListener(touchListenerRoot);

        this.onDragAndDropListener = l;
    }

    //функция возврата реализуемая в главном классе
    public interface OnDragAndDropListener {
        void onObjectTouch(View viewDrag, float x, float y);
        void onObjectDrag(List<View> list_overlapView, View viewDrag, float x, float y);
        void onObjectDrop(List<View> list_overlapView, View viewDrag, float x, float y);
    }

    //
    public interface OnClickListener {
        void onClick(View v);
    }

}
