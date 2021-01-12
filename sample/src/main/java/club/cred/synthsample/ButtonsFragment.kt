package club.cred.synthsample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import club.cred.synthsample.databinding.FragmentButtonsBinding

class ButtonsFragment : Fragment() {

    private var _binding: FragmentButtonsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentButtonsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val l = { _: View -> } // empty listener so that touch effects are visible
        binding.type1Button.setOnClickListener(l)
        binding.type1IconButton.setOnClickListener(l)
        binding.type2Button.setOnClickListener(l)
        binding.type3Button.setOnClickListener(l)
        binding.type3IconButton.setOnClickListener(l)

        binding.closeButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
