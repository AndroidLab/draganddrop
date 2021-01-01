package com.a_lab.draganddrop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ReturnBackAndFrameHardFragment extends Fragment {

    private FrameLayout flReturnBack;
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
        return inflater.inflate(R.layout.fragment_return_back, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        flReturnBack = view.findViewById(R.id.flReturnBack);
        ivRed = view.findViewById(R.id.ivRed);
        ivGreen = view.findViewById(R.id.ivGreen);
        ivBlue = view.findViewById(R.id.ivBlue);

        ((CheckBox) view.findViewById(R.id.cbReturnBack)).setOnCheckedChangeListener((buttonView, isChecked) -> {
            dragAndDrop.setReturnBack(isChecked);
        });

        ((CheckBox) view.findViewById(R.id.cbFrameHard)).setOnCheckedChangeListener((buttonView, isChecked) -> {
            dragAndDrop.setFrameHard(isChecked);
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dragAndDrop = new DragAndDrop(getActivity(), flReturnBack);

        dragAndDrop.addViewDrag(ivRed);
        dragAndDrop.addViewDrag(ivGreen);
        dragAndDrop.addViewDrag(ivBlue);
    }
}
