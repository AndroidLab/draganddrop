package com.a_lab.draganddrop;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class IntersectionObjectsFragment extends Fragment implements DragAndDrop.OnDragAndDropListener {

    private FrameLayout flIntersectionObjects;
    private ImageView ivRed, ivGreen, ivBlue, ivBlack;
    private TextView tvRedCoordinates, tvGreenCoordinates, tvBlueCoordinates;
    private DragAndDrop dragAndDrop;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_intersection_objects, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        flIntersectionObjects = view.findViewById(R.id.flIntersectionObjects);
        ivRed = view.findViewById(R.id.ivRed);
        ivGreen = view.findViewById(R.id.ivGreen);
        ivBlue = view.findViewById(R.id.ivBlue);
        ivBlack = view.findViewById(R.id.ivBlack);
        tvRedCoordinates = view.findViewById(R.id.tvRedCoordinates);
        tvGreenCoordinates = view.findViewById(R.id.tvGreenCoordinates);
        tvBlueCoordinates = view.findViewById(R.id.tvBlueCoordinates);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dragAndDrop = new DragAndDrop(getActivity(), flIntersectionObjects);
        dragAndDrop.setDragListener(this);

        dragAndDrop.addViewDrag(ivRed);
        dragAndDrop.addViewDrag(ivGreen);
        dragAndDrop.addViewDrag(ivBlue);
        dragAndDrop.addViewDrag(ivBlack, false);
    }

    @Override
    public void onObjectTouch(View viewDrag, float x, float y) {
        if (viewDrag.getId() == R.id.ivRed)
            Toast.makeText(getActivity(), "Start drag red", Toast.LENGTH_SHORT).show();
        if (viewDrag.getId() == R.id.ivGreen)
            Toast.makeText(getActivity(), "Start drag green", Toast.LENGTH_SHORT).show();
        if (viewDrag.getId() == R.id.ivBlue)
            Toast.makeText(getActivity(), "Start drag blue", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onObjectDrag(List<View> overlapViewList, View viewDrag, float x, float y) {
        if (viewDrag.getId() == R.id.ivRed)
            tvRedCoordinates.setText("Red x: " + (int) x + ", y: " + (int) y);
        if (viewDrag.getId() == R.id.ivGreen)
            tvGreenCoordinates.setText("Green x: " + (int) x + ", y: " + (int) y);
        if (viewDrag.getId() == R.id.ivBlue)
            tvBlueCoordinates.setText("Blue x: " + (int) x + ", y: " + (int) y);
    }

    @Override
    public void onObjectDrop(List<View> overlapViewList, View viewDrag, float x, float y) {
        List<String> intersections = new ArrayList<>();

        if (!overlapViewList.isEmpty())
            for (View overlapView : overlapViewList)
                intersections.add(overlapView.getTag().toString());

        Toast.makeText(getActivity(), "View drag intersects with "
                + TextUtils.join(", ", intersections), Toast.LENGTH_LONG).show();
    }
}
