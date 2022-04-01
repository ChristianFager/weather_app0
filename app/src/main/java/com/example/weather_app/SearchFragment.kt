package com.example.weather_app
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.weather_app.databinding.ActivityMainBinding
import com.example.weather_app.databinding.SearchFragmentBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SearchFragment: Fragment()
{
    @Inject
    lateinit var viewModel: SearchViewModel
    private lateinit var binding: SearchFragmentBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        binding = SearchFragmentBinding.bind(view)

        (requireActivity() as MainActivity).supportActionBar?.title = "Search"


        viewModel.showErrorDialog.observe(viewLifecycleOwner) { showError ->
            if (showError)
            {
                //ErrorDialogFragment().show(childFragmentManager, "")
            }
        }

        viewModel.enableButton.observe(viewLifecycleOwner) { enable ->
            binding.btnSearch.isEnabled = enable
        }


        viewModel.showErrorDialog.observe(viewLifecycleOwner) { showError ->
            if (showError)
            {
            }
        }

        binding.zipCode.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)
            {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)
            {
                p0?.toString()?.let { viewModel.updateZipCode(it) }
            }

            override fun afterTextChanged(p0: Editable?)
            {
            }
        })

        binding.btnSearch.setOnClickListener {
            viewModel.submitButtonClicked()

            val tzip = viewModel.getZip()
            val action = SearchFragmentDirections.actionSearchFragmentToCurrendConditionsFragment(tzip)
            findNavController().navigate(action)
        }


        binding.btnLocation.setOnClickListener {
            //(activity as MainActivity).showAlertLocation()
            (activity as MainActivity).getLastKnownLocation()
            //showAlertLocation()
            //getLastKnownLocation()

            //val lat = 31.9686 //viewModel.getLat()
            //val lon = 99.9018 //viewModel.getLon()
            val action = SearchFragmentDirections.actionSearchFragmentToCurrendConditionsFragment("99999")
            findNavController().navigate(action)
        }
    }


    override fun onResume()
    {
        super.onResume()
    }
}