package com.example.examenandroidalbertobarcenas.fragments

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.examenandroidalbertobarcenas.databinding.GaleryFragmentBinding
import com.example.examenandroidalbertobarcenas.fragments.viewmodel.GaleryViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.example.examenandroidalbertobarcenas.adapters.ImageAdapter
import java.util.ArrayList

class GaleryFragment : Fragment() {

    private var _binding: GaleryFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: GaleryViewModel
    private val File = 1
    var myRef: DatabaseReference? = null

    companion object {
        fun newInstance() = GaleryFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = GaleryFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(GaleryViewModel::class.java)

        val database = FirebaseDatabase.getInstance()
        myRef = database.getReference("user1")

        binding.ibUploadImage.setOnClickListener {
            fileUpload()
        }

        setListImages()
    }

    private fun fileUpload() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("*/*")
        startActivityForResult(intent, File)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == File) {
            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data!!.data
                val folder: StorageReference =
                    FirebaseStorage.getInstance().getReference().child("User")
                val file_name: StorageReference = folder.child("file" + fileUri!!.lastPathSegment)
                file_name.putFile(fileUri).addOnSuccessListener { taskSnapshot ->
                    file_name.getDownloadUrl().addOnSuccessListener { uri ->
                        val hashMap = HashMap<String, String>()
                        hashMap["link"] = java.lang.String.valueOf(uri)
                        myRef?.setValue(hashMap)
                        setListImages()
                    }
                }
            }
        }
    }

    private fun setListImages(){
        var imagelist = ArrayList<Any>()
        var arrayAdapter = ImageAdapter(imagelist, this.requireContext())

        binding.pbLoad.visibility = View.VISIBLE
        val listRef = FirebaseStorage.getInstance().reference.child("User")
        listRef.listAll().addOnSuccessListener { listResult ->
            for (file in listResult.items) {
                file.downloadUrl.addOnSuccessListener { uri ->
                    imagelist.add(uri.toString())
                }.addOnSuccessListener {
                    binding.rvImage.setAdapter(arrayAdapter)
                    binding.pbLoad.visibility = View.GONE
                }
            }
        }
    }
}