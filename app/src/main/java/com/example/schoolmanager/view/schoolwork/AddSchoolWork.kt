package com.example.schoolmanager.view.schoolwork

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.NumberPicker
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import com.example.schoolmanager.util.KeyValue
import com.example.schoolmanager.R
import com.example.schoolmanager.databinding.ActivityAddSchoolBinding
import com.example.schoolmanager.model.network.SchoolWork
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AddSchoolWork : AppCompatActivity() {

    private val viewModel : SchoolWorkViewModel by viewModels()
    private var mBinding: ActivityAddSchoolBinding? = null
    private val binding get() = mBinding!!

    //---------------------------------------------------------------------------------------생명주기

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddSchoolBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

    //--------------------------------------------------------------------------------------사용자함수

    private fun bindViews() {
        with(binding) {
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
            //버튼
            schoolActivitySaveBtn.setOnClickListener {
                showPopUp()
            }
        }
    }

    //저장 확인 팝업창 띄우기
    private fun showPopUp() {
        AlertDialog.Builder(this)
            .setTitle("저장 확인")
            .setMessage("입력한 활동을 저장하시겠습니까?")
            .setPositiveButton("저장") { _, _ ->
                viewModel.postSchoolWork(binding)
                finish()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.cancel()
            }
            .create()
            .show()
    }
}