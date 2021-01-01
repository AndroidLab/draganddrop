package com.a_lab.draganddrop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TouchConflictFragment extends Fragment {

    private FrameLayout flTouchConflict;
    private ImageView ivTouchConflict;
    private ImageView ivRed, ivGreen, ivBlue;
    private DragAndDrop dragAndDrop;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_touch_conflict, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        flTouchConflict = view.findViewById(R.id.flTouchConflict);
        ivTouchConflict = view.findViewById(R.id.ivTouchConflict);
        ivRed = view.findViewById(R.id.ivRed);
        ivGreen = view.findViewById(R.id.ivGreen);
        ivBlue = view.findViewById(R.id.ivBlue);

        ivTouchConflict.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Area click", Toast.LENGTH_SHORT).show();
        });

        ((CheckBox) view.findViewById(R.id.cbTouchConflict)).setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                dragAndDrop.addViewForResolvingConflictTouch(ivTouchConflict);
            else
                dragAndDrop.removeViewForResolvingConflictTouch(ivTouchConflict);
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dragAndDrop = new DragAndDrop(getActivity(), flTouchConflict);

        dragAndDrop.addViewDrag(ivRed);
        dragAndDrop.addViewDrag(ivGreen);
        dragAndDrop.addViewDrag(ivBlue);
    }

}
