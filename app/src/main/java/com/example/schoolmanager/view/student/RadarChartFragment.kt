package com.example.schoolmanager.view.student

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import com.bumptech.glide.Glide
import com.example.schoolmanager.R
import com.example.schoolmanager.databinding.FragmentRadarChartBinding
import com.example.schoolmanager.model.network.Pet
import com.example.schoolmanager.util.KeyValue
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class RadarChartFragment : Fragment() {

    val viewModel: StudentViewModel by activityViewModels()

    private var mBinding: FragmentRadarChartBinding? = null
    val binding get() = mBinding!!

    private lateinit var chart: RadarChart
    private val chartData = ArrayList<RadarEntry>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentRadarChartBinding.inflate(inflater, container, false)
        chart = binding.radarChart

        binding.closeBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .remove(this)
                .commit()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.selectedStudentPet.observe(viewLifecycleOwner) {
            chartData.add(RadarEntry(it.academicAbility.toFloat()))
            chartData.add(RadarEntry(it.career.toFloat()))
            chartData.add(RadarEntry(it.leadership.toFloat()))
            chartData.add(RadarEntry(it.sincerity.toFloat()))
            chartData.add(RadarEntry(it.cooperation.toFloat()))
            makeChart()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        chartData.clear()
        mBinding = null
    }

    private fun makeChart() {
        val dataSet = RadarDataSet(chartData, "DATA")
        dataSet.color = R.color.teal_700
        val data = RadarData()
        data.addDataSet(dataSet)
        val labels = arrayOf("학업력", "진로", "리더십", "성실성", "협동성")
        val xAxis: XAxis = chart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        chart.data = data
    }
}