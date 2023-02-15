package com.example.schoolmanager.util

class KeyValue {
    companion object {
        //DB
        const val DB_USERS = "Users"
        const val DB_TEACHER_USERS = "Teachers"
        const val DB_SCHOOL_ACTIVITIES = "Activities"
        const val DB_SCHOOL_CLASSES = "Classes"
        const val DB_PETS = "Pets"
        const val DB_FAVORITES = "Favorites"
        const val DB_PARTICIPANTS = "IsParticipating"
        const val DB_PROPERTY_PET_IMG = "petUrlImage"

        //SharedPref
        const val SHARED_PREFERENCES = "sharedPref"
        const val SHARED_PREF_USER_INFO = "sharedPrefUser"


        const val INTENT_EXTRA_STUDENT = "studentInfo"
        const val INTENT_EXTRA_SCHOOL_WORK = "itemActivity"
        const val INTENT_EXTRA_SCHOOL_CLASS = "selectedClass"
        const val INTENT_EXTRA_SUBJECT = "selectedSubject"
        const val INTENT_EXTRA_USER_INFO = "userInfo"

        const val LOCAL_DB_STUDENT = "database_student"
        const val DB_STUDENTS = "Students"
        const val TEACHER_KEY = "kakao2618040363"

        const val FRAG_TO_FRAG_PET_KEY = "frag_to_frag_request_key"
        const val FRAG_BUNDLE_PET_KEY = "frag_to_frag_bundle_key"

        const val FRAG_TO_FRAG_SUBJECT_KEY = "frag_to_frag_subject_key"
        const val FRAG_BUNDLE_SUBJECT_KEY = "frag_to_frag_bundle_subject_key"

        const val PHOTO_PICKER_REQUEST_CODE = 1000
        const val REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 2000
        val spinnerItems = arrayOf("국어", "영어", "수학", "사회", "과학")
    }
}