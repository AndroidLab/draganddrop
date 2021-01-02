package com.a_lab.draganddrop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class MainFragment extends Fragment {

    private NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btnEasyDrag).setOnClickListener(v -> navController.navigate(R.id.actionMainFragmentToEasyDragFragment));
        view.findViewById(R.id.btnReturnBack).setOnClickListener(v -> navController.navigate(R.id.actionMainFragmentToReturnBackAndFrameHardFragment));
        view.findViewById(R.id.btnTouchConflict).setOnClickListener(v -> navController.navigate(R.id.actionMainFragmentToTouchConflictFragment));
        view.findViewById(R.id.btnClickListener).setOnClickListener(v -> navController.navigate(R.id.actionMainFragmentToClickListenerFragment));
        view.findViewById(R.id.btnIntersectionObjects).setOnClickListener(v -> navController.navigate(R.id.actionMainFragmentToIintersectionObjectsFragment));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        navController = Navigation.findNavController(requireView());
    }
}
