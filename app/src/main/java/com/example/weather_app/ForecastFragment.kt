package com.example.weather_app
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather_app.databinding.ForecastFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException
import javax.inject.Inject


@AndroidEntryPoint
class ForecastFragment: Fragment()
{
    @Inject
    lateinit var viewModel: ForecastViewModel
    private lateinit var binding: ForecastFragmentBinding
    private lateinit var tzip: String
    private val args: ForecastFragmentArgs by navArgs()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.forecast_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ForecastFragmentBinding.bind(view)

        (requireActivity() as MainActivity).supportActionBar?.title = "Forecast"
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        tzip = args.zip.toString()
    }


    override fun onResume()
    {
        super.onResume()
        viewModel.forecastConditions.observe(this)
        {
            binding.recyclerView.adapter = ForecastAdapter(it.list)
        }

        if (tzip != "0")
        {
            try
            {
                viewModel.loadData(tzip)
            }
            catch (e: HttpException)
            {
                ErrorDialogFragment().show(childFragmentManager, "")
            }
        }
        else
        {
            try
            {
                (activity as MainActivity).ultimate_request_location()
                val lat = (activity as MainActivity).lat
                val lon = (activity as MainActivity).lon

                viewModel.loadLatLong(lat, lon)
            }
            catch (e: HttpException)
            {
                ErrorDialogFragment().show(childFragmentManager, "")
            }
        }
    }
}