package com.example.schoolmanager.view.student

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolmanager.model.network.Pet
import com.example.schoolmanager.model.network.SchoolClass
import com.example.schoolmanager.model.network.SchoolWork
import com.example.schoolmanager.repository.Repository
import com.example.schoolmanager.util.KeyValue
import com.navercorp.nid.NaverIdLoginSDK.applicationContext
import kotlinx.coroutines.launch
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Workbook
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class StudentViewModel : ViewModel() {

    private val repo = Repository()

    //받아오는 펫 리스트
    private var mMyStudentPetList = MutableLiveData<MutableList<Pet>>()
    val myStudentMutablePetList : MutableList<Pet>? get() = mMyStudentPetList.value

    private var mSchoolWorkList = MutableLiveData<MutableList<SchoolWork>>()

    //엑셀 워크북
    private var mWorkBook: Workbook? = null
    private val workBook: Workbook get() = mWorkBook!!

    //엑셀 내보내기 모드
    var xlExportMode = MutableLiveData(false)

    //어댑터에서 선택한 학생목록
    private var mSelectedStudentPetList = MutableLiveData<MutableList<Pet>>()
    val selectedStudentPetList: LiveData<MutableList<Pet>> get() = mSelectedStudentPetList

    //어댑터에서 선택한 학습목록
    private var mSelectedSchoolWorkList = MutableLiveData<MutableList<SchoolWork>>()
    val selectedSchoolWorkList: LiveData<MutableList<SchoolWork>> get() = mSelectedSchoolWorkList

    private var mSelectedClass = MutableLiveData<SchoolClass>()
    val selectedClass: LiveData<SchoolClass> get() = mSelectedClass
    val selectedMutableClass: SchoolClass? get() = selectedClass.value

    private var mSelectedStudentPet = MutableLiveData<Pet>()
    val selectedStudentPet: LiveData<Pet> get() = mSelectedStudentPet



    fun selectClass(schoolClass: SchoolClass) = viewModelScope.launch {
        mSelectedClass.value = schoolClass
    }

    fun selectStudentPet(studentPet: Pet) = viewModelScope.launch {
        mSelectedStudentPet.value = studentPet
    }


    //나의 클래스에 소속되어있는 학생들 데이터 불러오기 from FireBase
    fun fetchMyStudentPetList(
        teacherId: String,
        classId: String
    ): LiveData<MutableList<Pet>> {
        viewModelScope.launch {
            mMyStudentPetList = MutableLiveData<MutableList<Pet>>()
            repo.getMyStudentPetInClassList(teacherId, classId).observeForever {
                mMyStudentPetList.value = it
            }
        }
        return mMyStudentPetList
    }

    //교사의 과목 활동 데이터 받아오기 from 파이어베이스 #네트워크
    fun fetchSchoolWorkList(teacherId: String, subject: String): LiveData<MutableList<SchoolWork>> {
        viewModelScope.launch {
            repo.getSchoolWorkList(teacherId, subject).observeForever {
                mSchoolWorkList.value = it
            }
        }
        return mSchoolWorkList
    }

    //선택된 학생들 어댑터에서 불러오기
    fun getSelectedStudent(selectedStudentPets: MutableList<Pet>) = viewModelScope.launch {
        mSelectedStudentPetList.value = selectedStudentPets
    }

    //선택된 학습활동 어댑터에서 불러오기
    fun getSelectedSchoolWork(selectedWorks: MutableList<SchoolWork>) = viewModelScope.launch {
        mSelectedSchoolWorkList.value = selectedWorks
    }

    fun putExpIntoStudent(
        students: MutableList<Pet>,
        schoolWorks: MutableList<SchoolWork>,
        teacherId: String,
        classId: String
    ) {
        Log.d("선택", "뷰모델 일: $schoolWorks")
        Log.d("선택", "뷰모델 학생: $students")
        students.forEach { studentPet ->
            for (i in 0 until schoolWorks.size) {
                with(schoolWorks[i]) {
                    studentPet.leadership += this.leadership
                    studentPet.career += this.career
                    studentPet.academicAbility += this.academicAbility
                    studentPet.cooperation += this.cooperation
                    studentPet.sincerity += this.sincerity
                    studentPet.money += this.money

                    studentPet.petSimpleInfo += " ${this.schoolWorkTitle}\n"
                    studentPet.petDetailInfo += " ${this.schoolWorkDetailInfo}."

                    studentPet.getLevel()
                }
            }
            try {
                postOneStudentPetData(teacherId, classId, studentPet)
            } catch (e: Exception) {
                Log.d("에러", "에러발생: $e")
            }
        }
    }

    //학생 한명 펫 데이터 업데이트
    private fun postOneStudentPetData(teacherId: String, classId: String, pet: Pet) =
        viewModelScope.launch {
            //교실 펫 정보 업데이트
            repo.classDB.child(teacherId)
                .child(classId)
                .child(KeyValue.DB_STUDENTS)
                .child(pet.belongToUser).setValue(pet)
            //학생 펫 정보 업데이트
            repo.studentDB.child(pet.belongToUser).child(KeyValue.DB_PETS).child(pet.petId)
                .setValue(pet)
        }

    //엑셀 파일 생성하기
    fun createExcelFile(petList: MutableList<Pet>, sheetName: String) = viewModelScope.launch {
        mWorkBook = HSSFWorkbook()
        val sheet = workBook.createSheet(sheetName)
        //헤더 생성
        var row: Row = sheet.createRow(0)  // 새로운 행 생성
        var cell: Cell = row.createCell(0) // 1행1열
        cell.setCellValue("학번")          // 헤더

        cell = row.createCell(1) // 1행2열 생성
        cell.setCellValue("이름")

        cell = row.createCell(2) // 1행3열 생성
        cell.setCellValue("생기부 누가기록")


        for (i in 0 until petList.size) { // 데이터 엑셀에 반복입력
            row = sheet.createRow(i + 1)
            cell = row.createCell(0) // 학번
            cell.setCellValue(petList[i].userStudentNumber) //배열[i]
            cell = row.createCell(1) // 이름
            cell.setCellValue(petList[i].userName)
            cell = row.createCell(2) // 생기부 내용
            cell.setCellValue(petList[i].petDetailInfo)
        }
    }

    //외부저장소에 엑셀파일로 저장하기
    fun storeExcelInStorage(context: Context, fileName: String) {
        viewModelScope.launch {
            val xlsFileName = "$fileName.xls"
            val xlsFile = File(context.getExternalFilesDir(null), xlsFileName)
            var fileOutputStream: FileOutputStream? = null

            try {
                fileOutputStream = FileOutputStream(xlsFile)
                workBook.write(fileOutputStream)
                Log.d("저장", "파일 저장: $xlsFile")

            } catch (e: IOException) {
                Log.e("저장", "쓰기 IO 에러 발생: ", e)

            } catch (e: Exception) {
                Log.e("저장", "에러 발생: ", e)

            } finally {
                try {
                    if (null != fileOutputStream) {
                        fileOutputStream.close()
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }

    //엑셀 파일 공유
    fun shareFile(context: Context, fileName: String) = viewModelScope.launch { //파일 공유
        val extRoot = context.getExternalFilesDir(null) //앱의 기본 저장 루트
        val someFile = "/$fileName.xls"                //파일이름 설정
        Log.d("공유", extRoot.toString())

        val xlsFile = File(extRoot, someFile) //파일 인스턴스를 만든다.
        Log.d("공유", xlsFile.toString())
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "application/excel" //엑셀파일 공유 시

        val contentUri: Uri = FileProvider.getUriForFile(
            applicationContext,
            applicationContext.packageName + ".fileProvider",
            xlsFile
        )

        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
        context.startActivity(Intent.createChooser(shareIntent, "엑셀 공유"))
    }

}