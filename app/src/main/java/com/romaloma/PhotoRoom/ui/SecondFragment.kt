package com.romaloma.PhotoRoom.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.romaloma.PhotoRoom.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val sharedPhotoViewModel: PhotoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pos = arguments?.getInt("imagePosition")
        Log.d("Photo Position", "Position = $pos")
        val bitmap = pos?.let { sharedPhotoViewModel.dataList.value?.get(it) }
        if (bitmap != null) binding.detailImageView.setImageBitmap(bitmap)
        else binding.detailImageView.setImageResource(android.R.drawable.ic_delete)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}