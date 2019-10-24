package com.example.fitfactory.presentation.fragments.exercises.exercise

import androidx.lifecycle.ViewModel
import com.example.fitfactory.data.models.Exercise

class ExerciseViewModel : ViewModel() {

    fun getExercises(): List<Exercise>{
        return listOf(
            Exercise(
                name = "Face pull",
                images = listOf(
                    "https://cdn.muscleandstrength.com/sites/default/files/cable-face-pull.jpg",
                    "https://cdn.muscleandstrength.com/sites/default/files/field/feature-wide-image/article/ultimate-guide-to-face-pulls-wide.jpg")
            ),
            Exercise(
                name = "Wiosłowanie hantlami",
                images = listOf(
                    "https://www.fabrykasily.pl/upload/gallery/2018/07/id_9695_1531134585_1260x841.jpg",
                    "https://www.fabrykasily.pl/upload/gallery/2018/07/id_9699_1531134832_1260x841.jpg")
            ),
            Exercise(
                name = "Face pull",
                images = listOf(
                    "https://cdn.muscleandstrength.com/sites/default/files/cable-face-pull.jpg",
                    "https://cdn.muscleandstrength.com/sites/default/files/field/feature-wide-image/article/ultimate-guide-to-face-pulls-wide.jpg")
            )
        )
    }
}