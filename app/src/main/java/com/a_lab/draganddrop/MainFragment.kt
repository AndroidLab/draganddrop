package com.a_lab.draganddrop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.a_lab.draganddrop.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val binding: FragmentMainBinding by lazy {
        DataBindingUtil.inflate(layoutInflater, R.layout.fragment_main, null, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController(requireView())

        binding.easyDragBtn.setOnClickListener {
            navController.navigate(
                R.id.toEasyDragFragment
            )
        }
        binding.returnBackBtn.setOnClickListener {
            navController.navigate(
                R.id.toReturnBackAndFrameHardFragment
            )
        }
        binding.touchConflictBtn.setOnClickListener {
            navController.navigate(
                R.id.toTouchConflictFragment
            )
        }
        binding.clickListenerBtn.setOnClickListener {
            navController.navigate(
                R.id.toClickListenerFragment
            )
        }
        binding.intersectionObjectsBtn.setOnClickListener {
            navController.navigate(
                R.id.toIntersectionObjectsFragment
            )
        }
    }

}