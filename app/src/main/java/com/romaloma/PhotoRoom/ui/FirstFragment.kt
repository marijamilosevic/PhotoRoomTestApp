package com.romaloma.PhotoRoom.ui

import RecyclerViewAdapter
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.romaloma.PhotoRoom.R
import com.romaloma.PhotoRoom.databinding.FragmentFirstBinding


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val sharedPhotoViewModel: PhotoViewModel by activityViewModels()

    private lateinit var adapter: RecyclerViewAdapter

    private val changeImage =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data
                val imgUri = data?.data
                Log.d("SELECTING IMAGE", "Uri: $imgUri")

                if (imgUri != null) {
                    sharedPhotoViewModel.addPhoto(imgUri, requireActivity().contentResolver)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RecyclerViewAdapter(requireContext()) { pos -> openImage(pos) }
        binding.imageRecyclerView.adapter = adapter

        val layoutManager = GridLayoutManager(requireContext(), 3)
        binding.imageRecyclerView.layoutManager = layoutManager

        binding.fab.setOnClickListener {
            pickImageFromGallery()
        }

        sharedPhotoViewModel.dataList.observe(viewLifecycleOwner) {
            adapter.updateItems(it)
        }
    }

    private fun openImage(pos: Int) {
        val bundle = Bundle()
        bundle.putInt("imagePosition", pos)
        findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)
    }

    private fun pickImageFromGallery() {
        val pickImg =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        changeImage.launch(pickImg)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}