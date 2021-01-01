package com.a_lab.draganddrop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ClickListenerFragment extends Fragment {

    private FrameLayout flClickListener;
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
        return inflater.inflate(R.layout.fragment_click_listener, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        flClickListener = view.findViewById(R.id.flClickListener);
        ivRed = view.findViewById(R.id.ivRed);
        ivGreen = view.findViewById(R.id.ivGreen);
        ivBlue = view.findViewById(R.id.ivBlue);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dragAndDrop = new DragAndDrop(getActivity(), flClickListener);

        dragAndDrop.addViewDrag(ivRed, true,
                v -> Toast.makeText(getActivity(), "Click red", Toast.LENGTH_SHORT).show());
        dragAndDrop.addViewDrag(ivGreen, true,
                v -> Toast.makeText(getActivity(), "Click green", Toast.LENGTH_SHORT).show());
        dragAndDrop.addViewDrag(ivBlue, true,
                v -> Toast.makeText(getActivity(), "Click blue", Toast.LENGTH_SHORT).show());
    }

}
