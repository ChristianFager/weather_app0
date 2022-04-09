package com.example.weather_app
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.weather_app.databinding.CurrentConditionsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import retrofit2.HttpException


@AndroidEntryPoint
class CurrentConditionsFragment: Fragment()
{
    @Inject
    lateinit var viewModel: CurrentConditionsViewModel
    private lateinit var binding: CurrentConditionsFragmentBinding
    private lateinit var tzip: String
    private val args: CurrentConditionsFragmentArgs by navArgs()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.current_conditions_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CurrentConditionsFragmentBinding.bind(view)

        (requireActivity() as MainActivity).supportActionBar?.title = "Current Conditions"

        tzip = args.zip.toString()

        binding.btnForecast.setOnClickListener {
            navigateToForecast()
        }
    }


    override fun onResume()
    {
        super.onResume()
        viewModel.currentConditions.observe(this)
        {
            CurrentConditions -> bindData(CurrentConditions)
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
                (activity as MainActivity).getLastKnownLocation()
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


    @SuppressLint("SetTextI18n")
    private fun bindData(currentConditions: CurrentConditions)
    {
        binding.cityName.text = currentConditions.name
        binding.temperature.text = getString(R.string.temperature, currentConditions.main.temp.toInt())
        binding.tempMin.text = "Low " + getString(R.string.tempMin, currentConditions.main.tempMin.toInt())
        binding.tempMax.text = "High " + getString(R.string.tempMax, currentConditions.main.tempMax.toInt())
        binding.humidity.text = "Humidity " + getString(R.string.humidity, currentConditions.main.humidity.toInt()) + "%"
        binding.pressure.text = "Pressure " + getString(R.string.pressure, currentConditions.main.pressure.toInt()) + " hPa"
        binding.feelsLike.text = "Feels like " + getString(R.string.feels_like, currentConditions.main.feelsLike.toInt())
        val iconName = currentConditions.weather.firstOrNull()?.icon
        val iconUrl = "https://openweathermap.org/img/wn/${iconName}@2x.png"
        Glide.with(this)
            .load(iconUrl)
            .into(binding.conditionIcon)
    }


    private fun navigateToForecast()
    {
        val action = CurrentConditionsFragmentDirections.actionCurrendConditionsFragmentToForecastFragment(tzip)
        findNavController().navigate(action)
    }
}