package com.a_lab.draganddrop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.a_lab.draganddrop.databinding.FragmentReturnBackBinding

class ReturnBackAndFrameHardFragment : Fragment() {

    private val binding: FragmentReturnBackBinding by lazy {
        DataBindingUtil.inflate(layoutInflater, R.layout.fragment_return_back, null, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dragAndDrop = DragAndDrop(binding.returnBackLayout)

        dragAndDrop.addDragView(binding.root.findViewById(R.id.redImage))
        dragAndDrop.addDragView(binding.root.findViewById(R.id.greenImage))
        dragAndDrop.addDragView(binding.root.findViewById(R.id.blueImage))

        binding.returnBackCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            dragAndDrop.setReturnBack(isChecked)
        }

        binding.frameHardCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            dragAndDrop.setFrameHard(isChecked)
        }
    }

}