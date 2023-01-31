package com.example.schoolmanager.view.schoolwork

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.schoolmanager.databinding.ActivitySchoolWorkDetailBinding
import com.example.schoolmanager.model.network.SchoolWork
import com.example.schoolmanager.model.network.Teacher
import com.example.schoolmanager.util.KeyValue

class SchoolWorkDetailActivity : AppCompatActivity() {

    //뷰모델
    private lateinit var viewModel: SchoolWorkViewModel
    private lateinit var viewModelFactory: SchoolWorkViewModelFactory

    private var mBinding: ActivitySchoolWorkDetailBinding? = null
    private val binding get() = mBinding!!
    private var mSchoolWork: SchoolWork? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = intent.getParcelableExtra<Teacher>(KeyValue.INTENT_EXTRA_USER_INFO)

        viewModelFactory = user?.let { SchoolWorkViewModelFactory(it) }!!
        viewModel = ViewModelProvider(this, viewModelFactory)[SchoolWorkViewModel::class.java]

        mBinding = ActivitySchoolWorkDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mSchoolWork = intent.getParcelableExtra(KeyValue.INTENT_EXTRA_SCHOOL_WORK)
        mSchoolWork?.let { bindViews(it) }
    }

    private fun bindViews(schoolWork: SchoolWork) {
        bindNumberPickers()
        //정보
        binding.subjectTextview.text = schoolWork.subject
        binding.schoolWorkTitleEditTextview.text = schoolWork.schoolWorkTitle
        binding.schoolWorkSimpleInfoEdittext.setText(schoolWork.schoolWorkSimpleInfo)
        binding.schoolWorkDetailEditEdittext.setText(schoolWork.schoolWorkDetailInfo)
        //점수
        binding.careerNumberpicker.value = schoolWork.career
        binding.cooperationNumberpicker.value = schoolWork.cooperation
        binding.academicNumberpicker.value = schoolWork.academicAbility
        binding.leadershipNumberpicker.value = schoolWork.leadership
        binding.sincerityNumberpicker.value = schoolWork.sincerity
        //동기부여
        binding.expectedDifficultyRating.rating = schoolWork.difficulty?.toFloat() ?: 0f
        binding.schoolWorkEditBtn.setOnClickListener { //버튼
            Log.d(
                "활동 수정 유저 정보", "현재 사용자: ${viewModel.currentUser.userId}," +
                        "활동을 만든 사용자: ${schoolWork.madeBy}"
            )
            if(viewModel.currentUser.userId != schoolWork.madeBy){
                Toast.makeText(this, "활동을 변경할 권한이 없습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            showEditPopUp()
        }
        binding.deleteBtn.setOnClickListener {
            Log.d(
                "활동 삭제 유저 정보", "현재 사용자: ${viewModel.currentUser.userId}," +
                        "활동을 만든 사용자: ${schoolWork.madeBy}"
            )
            if (viewModel.currentUser.userId != schoolWork.madeBy) {
                Toast.makeText(this, "활동을 삭제할 권한이 없습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            showDeletePopUp()
        }
    }

    private fun bindNumberPickers() = with(binding) {
        val numberPickerList: List<NumberPicker> = mutableListOf(
            leadershipNumberpicker,
            academicNumberpicker,
            cooperationNumberpicker,
            sincerityNumberpicker,
            careerNumberpicker
        )
        numberPickerList.forEach {
            with(it) {
                maxValue = 3
                minValue = 0
            }
        }
        moneyNumberpicker.maxValue = 10
        moneyNumberpicker.minValue = 1
    }

    //저장 확인 팝업
    private fun showEditPopUp() {
        AlertDialog.Builder(this)
            .setTitle("수정 확인")
            .setMessage("위 정보로 활동을 수정하시겠습니까?")
            .setPositiveButton("수정") { _, _ ->
                try {
                    viewModel.postEditSchoolWorkData(binding)
                    Toast.makeText(this, "활동이 수정되었습니다.", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this, "$e 오류가 발생했습니다. hottouk 카톡으로 보고해주세요.", Toast.LENGTH_SHORT)
                        .show()
                } finally {
                    finish()
                }
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
                try {
                    mSchoolWork?.let { viewModel.postDeleteSchoolWorkData(it) }
                    Toast.makeText(this, "활동이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                }catch (e: Exception) {
                    Toast.makeText(this, "$e 오류가 발생했습니다. hottouk 카톡으로 보고해주세요.", Toast.LENGTH_SHORT)
                        .show()
                }finally {
                    finish()
                }
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.cancel()
            }
            .create()
            .show()
    }
}