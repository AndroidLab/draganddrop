package com.a_lab.draganddrop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.a_lab.draganddrop.databinding.FragmentTouchConflictBinding

class TouchConflictFragment : Fragment() {

    private val binding: FragmentTouchConflictBinding by lazy {
        DataBindingUtil.inflate(layoutInflater, R.layout.fragment_touch_conflict, null, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dragAndDrop = DragAndDrop(binding.touchConflictLayout)

        dragAndDrop.addDragView(binding.root.findViewById(R.id.redImage))
        dragAndDrop.addDragView(binding.root.findViewById(R.id.greenImage))
        dragAndDrop.addDragView(binding.root.findViewById(R.id.blueImage))

        binding.touchConflictImage.setOnClickListener {
            Toast.makeText(
                activity,
                "Area click",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.touchConflictCheckBox.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            if (isChecked)
                dragAndDrop.addViewForResolvingConflictTouch(binding.touchConflictImage)
            else
                dragAndDrop.removeViewForResolvingConflictTouch(binding.touchConflictImage)
        }
    }
}