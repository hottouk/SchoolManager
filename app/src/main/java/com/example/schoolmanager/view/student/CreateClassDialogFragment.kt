package com.example.schoolmanager.view.student

import android.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.schoolmanager.databinding.FragmentCreateClassDialogBinding
import com.example.schoolmanager.util.KeyValue
import com.example.schoolmanager.view.main.MainViewModel

class CreateClassDialogFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private var mBinding: FragmentCreateClassDialogBinding? = null
    val binding get() = mBinding!!

    private var classNameList = mutableListOf<String>()

    //스피너 관련
    val spinnerItems = KeyValue.spinnerItems
    private var mSelectedSubjectItem = ""
    private val mySpinnerAdapter: SpinnerAdapter by lazy {
        ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_dropdown_item,
            spinnerItems
        )
    }
    private val spinnerItemListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when (position) {
                0 -> {
                    mSelectedSubjectItem = spinnerItems[0]
                }
                1 -> {
                    mSelectedSubjectItem = spinnerItems[1]
                }
                2 -> {
                    mSelectedSubjectItem = spinnerItems[2]
                }
                3 -> {
                    mSelectedSubjectItem = spinnerItems[3]
                }
                4 -> {
                    mSelectedSubjectItem = spinnerItems[4]
                }
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }
    }

    //---------------------------------------------------------------------------------------생명주기
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentCreateClassDialogBinding.inflate(inflater, container, false)
        viewModel.fetchClassesList().observe(viewLifecycleOwner) { //반 목록을 가져와 동일한 이름의 반이 있는지 검사
            classNameList.clear()
            it.forEach { schoolClass ->
                classNameList.add(schoolClass.className)
            }
        }

        binding.openBtn.setOnClickListener { //생성 버튼
            val classTitle = binding.inputEdittext.text.toString()
            when {
                (classTitle.isEmpty()) -> {
                    Toast.makeText(context, "반 이름을 입력해주세요", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                (classNameList.contains(classTitle)) -> {
                    Toast.makeText(context, "동일한 반 이름이 존재합니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                else -> {
                    viewModel.postCreateClass(
                        classTitle,
                        binding.subjectSpinner.selectedItem.toString()
                    )
                    viewModel.classCreateMode.value = false
                    container?.visibility = View.GONE
                    Toast.makeText(context, "$classTitle 가(이) 생성되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.cancelBtn.setOnClickListener { //취소 버튼
            container?.visibility = View.GONE
            viewModel.classCreateMode.value = false
        }

        binding.subjectSpinner.adapter = mySpinnerAdapter
        binding.subjectSpinner.onItemSelectedListener = spinnerItemListener
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}