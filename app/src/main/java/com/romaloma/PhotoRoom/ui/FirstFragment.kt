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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.romaloma.PhotoRoom.BitmapList
import com.romaloma.PhotoRoom.R
import com.romaloma.PhotoRoom.api.PhotoRoomApi
import com.romaloma.PhotoRoom.databinding.FragmentFirstBinding


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var viewModel: PhotoViewModel
    private val photoRoomApi = PhotoRoomApi()
    var list = BitmapList()

    private val changeImage =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data
                val imgUri = data?.data
                Log.d("SELECTING IMAGE", "Uri: $imgUri")
                activity?.let { it1 ->
                    if (imgUri != null) {
                        list.addImageFromUri(imgUri, it1.contentResolver)
                        val inputStream = context?.contentResolver?.openInputStream(imgUri)!!
                        inputStream.let { inputStream -> viewModel.editPhoto(inputStream) }
                    }
                }
                adapter.updateItems(list.bitmapList)
            }

        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RecyclerViewAdapter(requireContext()) { openImage() }
        binding.imageRecyclerView.adapter = adapter

        val layoutManager = GridLayoutManager(requireContext(), 3)
        binding.imageRecyclerView.layoutManager = layoutManager

        binding.fab.setOnClickListener {
            pickImageFromGallery()
        }

        viewModel = PhotoViewModel(photoRoomApi)

    }

    private fun openImage() {
        findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
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