package com.a_lab.draganddrop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.a_lab.draganddrop.databinding.FragmentIntersectionObjectsBinding

class IntersectionObjectsFragment : Fragment(), DragAndDrop.OnDragAndDropListener {

    private val binding: FragmentIntersectionObjectsBinding by lazy {
        DataBindingUtil.inflate(layoutInflater, R.layout.fragment_intersection_objects, null, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dragAndDrop = DragAndDrop(
            binding.intersectionObjectsLayout,
            RectMethod.HIT,
            ViewCaptureMethod.VIEW_CAPTURE_METHOD_CENTER
        )
        dragAndDrop.setOnDragListener(this)

        dragAndDrop.addDragView(binding.root.findViewById(R.id.redImage))
        dragAndDrop.addDragView(binding.root.findViewById(R.id.greenImage))
        dragAndDrop.addDragView(binding.root.findViewById(R.id.blueImage))
        dragAndDrop.addDragView(binding.blackImage, false)
    }

    override fun onObjectTouch(rootView: View, draggingView: View, x: Float, y: Float) {
        if (draggingView.id == R.id.redImage) Toast.makeText(
            activity,
            "Start drag red",
            Toast.LENGTH_SHORT
        ).show()
        if (draggingView.id == R.id.greenImage) Toast.makeText(
            activity,
            "Start drag green",
            Toast.LENGTH_SHORT
        ).show()
        if (draggingView.id == R.id.blueImage) Toast.makeText(
            activity,
            "Start drag blue",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onObjectDrag(
        rootView: View,
        overlapViews: List<View>,
        overlapWithTouchPointViews: List<View>,
        draggingView: View,
        x: Float,
        y: Float
    ) {
        if (draggingView.id == R.id.redImage) binding.redCoordinates.text =
            "Red x: " + x.toInt() + ", y: " + y.toInt()
        if (draggingView.id == R.id.greenImage) binding.greenCoordinates.text =
            "Green x: " + x.toInt() + ", y: " + y.toInt()
        if (draggingView.id == R.id.blueImage) binding.blueCoordinates.text =
            "Blue x: " + x.toInt() + ", y: " + y.toInt()
    }

    override fun onObjectDrop(
        rootView: View,
        overlapViews: List<View>,
        overlapWithTouchPointViews: List<View>,
        draggingView: View,
        x: Float,
        y: Float
    ) {
        Toast.makeText(
            requireContext(), "View drag intersects with "
                    + overlapViews.joinToString { it.tag.toString() }, Toast.LENGTH_LONG
        ).show()
    }
}