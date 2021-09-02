package com.genmare.ad340.details

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import coil.load
import com.genmare.ad340.*
import com.genmare.ad340.databinding.FragmentForecastDetailsBinding
import java.text.SimpleDateFormat
import java.util.*

class ForecastDetailsFragment : Fragment() {

    private val args: ForecastDetailsFragmentArgs by navArgs()

    private lateinit var viewModelFactory: ForecastDetailsViewModelFactory
    // by viewModels() permet de ne pas recharger le model lors d'un changement de config (ex: écran en mode paysage)
    // grâce à un "delegate" qui fera une copie du mofel
    private val viewModel: ForecastDetailsViewModel by viewModels(
        factoryProducer = { viewModelFactory }
    )

    private var _binding: FragmentForecastDetailsBinding? = null
    // This property only valid between onCreateView and onDestroyView (!! : assure que la valeur est non nulle)
    private val binding:FragmentForecastDetailsBinding get() = _binding!!

    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForecastDetailsBinding.inflate(inflater, container,false)
        viewModelFactory = ForecastDetailsViewModelFactory(args)
        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewStateObserver = Observer<ForecastDetailsViewState> { viewState ->
            // update the UI
            binding.tempText2.text = formatTempForDisplay(viewState.temp, tempDisplaySettingManager.getTempDisplaysetting())
            binding.descriptionText.text = viewState.description
            binding.dateText.text = viewState.date
            binding.forecastIcon.load(viewState.iconUrl)
        }
        viewModel.viewState.observe(viewLifecycleOwner, viewStateObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
