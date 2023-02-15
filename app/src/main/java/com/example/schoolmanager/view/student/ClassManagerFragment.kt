package com.example.schoolmanager.view.student

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.schoolmanager.R
import com.example.schoolmanager.databinding.FragmentClassManagerBinding
import com.example.schoolmanager.model.network.SchoolClass
import com.example.schoolmanager.view.adapter.ClassOuterRvAdapter
import com.example.schoolmanager.view.main.MainActivity
import com.example.schoolmanager.view.main.MainViewModel

class ClassManagerFragment : Fragment() {
    //뷰모델
    val mainViewModel: MainViewModel by activityViewModels()
    val viewModel : StudentViewModel by activityViewModels()

    //context
    private var mBinding: FragmentClassManagerBinding? = null
    val binding get() = mBinding!!
    lateinit var mainActivity: MainActivity

    //어댑터 전역변수
    private lateinit var adapter : ClassOuterRvAdapter
    private var deleteMode: Boolean = false
        set(value) {
            adapter.deleteMode = value
            adapter.notifyDataSetChanged()
            field = value
        }

    //---------------------------------------------------------------------------------------생명주기
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentClassManagerBinding.inflate(inflater, container, false)
        bindViews()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.fetchClassesList().observe(viewLifecycleOwner) {
            setClassAdapter(it)
        }
        mainViewModel.classCreateMode.observe(viewLifecycleOwner){ //반 생성 모드 on/off 구독
            binding.coverLayout.isVisible = it
            binding.dialogContainer.isVisible = it
        }
    }

    private fun bindViews() {
        binding.openClassBtn.setOnClickListener {
            mainViewModel.classCreateMode.value = true
        }
        binding.deleteBtn.setOnClickListener {
            deleteModeOnOff()
        }
        binding.deleteClassBtn.setOnClickListener {
            if(adapter.getNumberOfSelectedClasses() == 0){
                Toast.makeText(context, "선택된 반이 없습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            showDeletePopUp()
        }
    }

    //삭제 모드 onOff 전환
    private fun deleteModeOnOff() {
        deleteMode = !deleteMode
        binding.deleteClassBtn.isVisible = deleteMode
    }

    //반 어뎁터 세팅
    private fun setClassAdapter(
        classList: MutableList<SchoolClass>,
    ) {
        adapter = ClassOuterRvAdapter(classList)
        binding.classOuterRecyclerview.layoutManager = GridLayoutManager(context, 3)
        binding.classOuterRecyclerview.adapter = adapter
        adapter.setOnItemClickListener {
            viewModel.selectClass(it)
            parentFragmentManager.beginTransaction() //반이동
                .replace(R.id.main_fragment_container_view, StudentManagerFragment())
                .commit()
        }
    }

    //삭제 확인 팝업
    private fun showDeletePopUp() {
        AlertDialog.Builder(requireContext())
            .setTitle("삭제 확인")
            .setMessage("이 반을 정말로 삭제하시겠습니까? 이 반의 모든 학생 정보도 삭제되며 이는 복구할 수 없습니다.")
            .setPositiveButton("삭제") { _, _ ->
                val selectedList = adapter.getSelectedClasses()
                mainViewModel.postDeleteClass(selectedList)
                deleteModeOnOff()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.cancel()
            }
            .create()
            .show()
    }
}