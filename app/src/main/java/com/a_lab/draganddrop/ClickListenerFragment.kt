package com.a_lab.draganddrop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.a_lab.draganddrop.databinding.FragmentClickListenerBinding

class ClickListenerFragment : Fragment() {

    private val binding: FragmentClickListenerBinding by lazy {
        DataBindingUtil.inflate(layoutInflater, R.layout.fragment_click_listener, null, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dragAndDrop = DragAndDrop(
            binding.clickListenerLayout,
            RectMethod.HIT,
            ViewCaptureMethod.VIEW_CAPTURE_METHOD_CENTER
        )

        dragAndDrop.addDragView(
            binding.root.findViewById(R.id.redImage),
            true,
            true
        ) {
            Toast.makeText(requireContext(), "Click red", Toast.LENGTH_SHORT).show()
        }

        dragAndDrop.addDragView(
            binding.root.findViewById(R.id.greenImage),
            true,
            true
        ) {
            Toast.makeText(requireContext(), "Click green", Toast.LENGTH_SHORT).show()
        }

        dragAndDrop.addDragView(
            binding.root.findViewById(R.id.blueImage),
            true,
            true
        ) {
            Toast.makeText(requireContext(), "Click blue", Toast.LENGTH_SHORT).show()
        }
    }

}