package com.example.schoolmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.schoolmanager.databinding.ItemStudentPetBinding
import com.example.schoolmanager.model.network.Pet
import com.example.schoolmanager.view.student.StudentViewModel

class StudentPetRvAdapter(val viewModel: StudentViewModel) :
    ListAdapter<Pet, StudentPetRvAdapter.StudentPetItemViewHolder>(differCallback) {

    private var itemClickListener: ((Pet) -> Unit)? = null //리스너

    private var selectedStudents: MutableList<Pet> = mutableListOf() //선택 학생 넣는 변수

    var isSchoolWorkSelected: Boolean = false

    inner class StudentPetItemViewHolder(private val binding: ItemStudentPetBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindViews(item: Pet) {
            with(binding) {
                viewModel.getSelectedStudent(selectedStudents)
                changeBackground(binding, R.color.item_gray_bg)
                studentNameContentTextview.text = "${item.userName}의 펫"
                studentNumberContentTextview.text = item.userStudentNumber
                studentLevelContentTextview.text = item.petLevel.toString()
                Glide.with(binding.root.context)
                    .load(item.userProfileImageUrl)
                    .into(binding.userProfileImageview)

                item.setPetImage(
                    binding.root.context,
                    item.petUrlImage,
                    binding.studentCharacterImageview
                )
                item.setItemProgressBar(binding)

                if (isSchoolWorkSelected) { //학습자 선택 모드시
                    checkBox.visibility = View.VISIBLE
                    binding.root.setOnClickListener {
                        applySelection(binding, item)
                    }
                } else {
                    checkBox.visibility = View.GONE
                    root.setOnClickListener {
                        itemClickListener?.let { it(item) } //외부 리스너 설정
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StudentPetRvAdapter.StudentPetItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = ItemStudentPetBinding.inflate(inflater, parent, false)
        return StudentPetItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StudentPetItemViewHolder, position: Int) {
        val item = currentList[position]
        holder.bindViews(item)
    }


    //다중 클릭 관련 부분
    private fun applySelection(binding: ItemStudentPetBinding, studentPet: Pet) {
        if (selectedStudents.contains(studentPet)) {
            selectedStudents.remove(studentPet)
            viewModel.getSelectedStudent(selectedStudents)
            binding.checkBox.isChecked = false
            changeBackground(binding, R.color.item_gray_bg)

        } else {
            selectedStudents.add(studentPet)
            viewModel.getSelectedStudent(selectedStudents)
            binding.checkBox.isChecked = true
            changeBackground(binding, R.color.palette_selection)
        }
    }

    private fun changeBackground(binding: ItemStudentPetBinding, colorId: Int) {
        binding.innerCover.setBackgroundColor(
            ContextCompat.getColor(
                binding.root.context,
                colorId
            )
        )
    }

    //외부 참조 함수
    fun setOnItemClickListener(listener: (Pet) -> Unit) {
        itemClickListener = listener
    }

    fun getNumberOfSelectedStudents() = selectedStudents.size
    fun getSelectedStudents() = selectedStudents
    fun releaseSelection() = selectedStudents.clear()

    companion object {
        val differCallback = object : DiffUtil.ItemCallback<Pet>() {
            override fun areItemsTheSame(oldItem: Pet, newItem: Pet): Boolean {
                return oldItem.petId == newItem.petId
            }

            override fun areContentsTheSame(oldItem: Pet, newItem: Pet): Boolean {
                return oldItem == newItem
            }
        }
    }
}

