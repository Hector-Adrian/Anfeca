package com.example.anfeca.datos
/*
import com.google.firebase.firestore.FirebaseFirestore

val db = FirebaseFirestore.getInstance()

// Crear un nuevo usuario
val user = hashMapOf(
    "name" to "Usuario 1",
    "email" to "usuario1@ejemplo.com"
)

db.collection("users")
.add(user)
.addOnSuccessListener { documentReference ->
    println("DocumentSnapshot added with ID: ${documentReference.id}")
    // Después de crear el usuario, crea la subcolección "progress"
    createProgressSubcollection(documentReference.id)
}
.addOnFailureListener { e ->
    println("Error adding document ${e}")
}

// Función para crear la subcolección "progress" para un usuario
fun createProgressSubcollection(userId: String) {
    val progress = hashMapOf(
        "completed" to false,
        "score" to 0
    )

    db.collection("users")
        .document(userId)
        .collection("progress")
        .document("lesson1") // Puedes usar el ID de la lección aquí
        .set(progress)
        .addOnSuccessListener {
            println("Subcolección 'progress' creada para el usuario ${userId}")
        }
        .addOnFailureListener { e ->
            println("Error al crear la subcolección 'progress' ${e}")
        }
}

// Crear un nuevo curso
val course = hashMapOf(
    "name" to "Curso básico de señas",
    "description" to "Aprende las señas fundamentales"
)

db.collection("courses")
.add(course)
.addOnSuccessListener { documentReference ->
    println("Curso creado con ID: ${documentReference.id}")
}
.addOnFailureListener { e ->
    println("Error al crear el curso ${e}")
}

// Crear una nueva lección
val lesson = hashMapOf(
    "courseId" to "course1", // Reemplaza con el ID del curso
    "title" to "Saludos",
    "content" to "Video explicativo de cómo saludar"
)

db.collection("lessons")
.add(lesson)
.addOnSuccessListener { documentReference ->
    println("Lección creada con ID: ${documentReference.id}")
}
.addOnFailureListener { e ->
    println("Error al crear la lección ${e}")
}
*/