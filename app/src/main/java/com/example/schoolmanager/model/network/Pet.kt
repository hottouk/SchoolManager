package com.example.schoolmanager.model.network

import android.content.Context
import android.os.Parcelable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.schoolmanager.R
import com.example.schoolmanager.databinding.FragmentStudentPetDetailsBinding
import com.example.schoolmanager.databinding.ItemStudentPetBinding
import kotlinx.parcelize.Parcelize
import java.nio.charset.Charset

@Parcelize
data class Pet(
    //펫정보
    val petId: String,
    val petName: String,
    val subject: String,
    val petUrlImage: String,

    //펫유저 정보
    val belongToUser: String = "",
    val userName: String,
    val userStudentNumber: String = "",
    val userProfileImageUrl: String = "",

    //레벨
    var petLevel: Int,
    var petExp: Long = 0,

    //능력치
    var leadership: Int = 0,
    var academicAbility: Int = 0,
    var cooperation: Int = 0,
    var sincerity: Int = 0,
    var career: Int = 0,
    var money: Int = 0,

    //생기부 정보
    var petSimpleInfo: String = "",
    var petDetailInfo: String = "",
) : Parcelable {
    constructor() : this(
        "", "", "", "", "", "", "", "",
        1, 0, 0, 0, 0, 0, 0, 0, "", ""
    )

    init {
        getLevel()
    }

    fun getLevel() {
        petExp = (leadership + academicAbility + cooperation + sincerity + career).toLong()
        when (petExp) {
            in 0 until 25 -> {
                petLevel = 1
            }
            in 25 until 80 -> {
                petLevel = 2
            }
            in 50 until 200 -> {
                petLevel = 3
            }
            in 200 until 500 -> {
                petLevel = 4
            }
            else -> {
                petLevel = 5
            }
        }
    }

    fun setPetImage(context: Context, type: String, imageView: ImageView) {
        when (type) {
            "grass" -> {
                Glide.with(context)
                    .load(R.drawable.pet_grass1)
                    .circleCrop()
                    .into(imageView)
            }
            "water" -> {
                Glide.with(context)
                    .load(R.drawable.pet_water1)
                    .circleCrop()
                    .into(imageView)
            }

            else -> {
                Glide.with(context)
                    .load(R.drawable.pet_fire1)
                    .circleCrop()
                    .into(imageView)
            }
        }
    }

    fun setItemProgressBar(binding: ItemStudentPetBinding) {
        when (this.petLevel) {
            1 -> {
                binding.progressBar.max = 25
                binding.progressBar.progress = this.petExp.toInt()
            }
            2 -> {
                binding.progressBar.max = 80
                binding.progressBar.progress = this.petExp.toInt() - 25
            }
            3 -> {
                binding.progressBar.max = 200
                binding.progressBar.progress = this.petExp.toInt() - 80
            }
            4 -> {
                binding.progressBar.max = 500
                binding.progressBar.progress = this.petExp.toInt() - 200
            }
            else -> {
                binding.progressBar.max = 1000
                binding.progressBar.progress = this.petExp.toInt() - 500
            }
        }

    }

    fun setDetailProgressBar(binding: FragmentStudentPetDetailsBinding) {
        when (this.petLevel) {
            1 -> {
                binding.progressBar.max = 25
                binding.progressBar.progress = this.petExp.toInt()
                binding.levelUpExpTextview.text = "/25"
            }
            2 -> {
                binding.progressBar.max = 80
                binding.progressBar.progress = this.petExp.toInt() - 25
                binding.levelUpExpTextview.text = "/80"
            }
            3 -> {
                binding.progressBar.max = 200
                binding.progressBar.progress = this.petExp.toInt() - 80
                binding.levelUpExpTextview.text = "/200"
            }
            4 -> {
                binding.progressBar.max = 500
                binding.progressBar.progress = this.petExp.toInt() - 200
                binding.levelUpExpTextview.text = "/500"
            }
            else -> {
                binding.progressBar.max = 1000
                binding.progressBar.progress = this.petExp.toInt() - 500
                binding.levelUpExpTextview.text = "/1000"
            }
        }

    }

}