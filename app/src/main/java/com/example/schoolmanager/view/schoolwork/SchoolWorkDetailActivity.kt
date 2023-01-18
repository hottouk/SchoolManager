package com.example.schoolmanager.view.schoolwork

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.schoolmanager.databinding.ActivitySchoolWorkDetailBinding
import com.example.schoolmanager.model.network.SchoolWork
import com.example.schoolmanager.util.KeyValue

class SchoolWorkDetailActivity : AppCompatActivity() {

    private val viewModel: SchoolWorkViewModel by viewModels()

    private var mBinding: ActivitySchoolWorkDetailBinding? = null
    private val binding get() = mBinding!!
    private var mSchoolWork : SchoolWork? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySchoolWorkDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mSchoolWork = intent.getParcelableExtra(KeyValue.INTENT_EXTRA_SCHOOL_WORK)
        mSchoolWork?.let { bindViews(it) }
    }

    private fun bindViews(schoolWork: SchoolWork) {
        bindNumberPickers()
        //정보
        binding.schoolWorkTitleTextview.text = schoolWork.schoolWorkTitle
        binding.schoolWorkSimpleInfoEdittext.setText(schoolWork.schoolWorkSimpleInfo)
        binding.schoolWorkDetailInfoEdittext.setText(schoolWork.schoolWorkDetailInfo)
        //점수
        binding.careerNumberpicker.value = schoolWork.career
        binding.cooperationNumberpicker.value = schoolWork.cooperation
        binding.academicNumberpicker.value = schoolWork.academicAbility
        binding.leadershipNumberpicker.value = schoolWork.leadership
        binding.sincerityNumberpicker.value = schoolWork.sincerity
        binding.scoreSumTextview.text = "총점: ${schoolWork.getTotalScore()}점"
        binding.editSchoolWorkInfoBtn.setOnClickListener { //버튼
            showEditPopUp()
        }
        binding.deleteBtn.setOnClickListener {
            showDeletePopUp()
        }
    }

    fun bindNumberPickers() = with(binding) {
        val numberPickerList: List<NumberPicker> = mutableListOf(
            leadershipNumberpicker,
            academicNumberpicker,
            cooperationNumberpicker,
            sincerityNumberpicker,
            careerNumberpicker
        )
        numberPickerList.forEach {
            with(it) {
                maxValue = 5
                minValue = 0
            }
        }
    }

    //저장 확인 팝업
    private fun showEditPopUp() {
        AlertDialog.Builder(this)
            .setTitle("수정 확인")
            .setMessage("위 정보로 활동을 수정하시겠습니까?")
            .setPositiveButton("수정") { _, _ ->
                viewModel.editSchoolWorkData(binding)
                finish()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.cancel()
            }
            .create()
            .show()
    }

    //삭제 확인 팝업
    private fun showDeletePopUp() {
        AlertDialog.Builder(this)
            .setTitle("삭제 확인")
            .setMessage("이 활동을 삭제하시겠습니까? 삭제된 활동은 복구할 수 없습니다.")
            .setPositiveButton("삭제") { _, _ ->
                mSchoolWork?.let { viewModel.deleteSchoolWorkData(it) }
                finish()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.cancel()
            }
            .create()
            .show()
    }
}