package com.example.anfeca.datos

import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log

val db = FirebaseFirestore.getInstance()

// Crear un nuevo usuario
fun crearUsuario(nombre: String, email: String) {
    val user = hashMapOf(
        "name" to nombre,
        "email" to email
    )

    // Guardamos al usuario con su nombre como ID del documento
    db.collection("Usuarios")
        .document(nombre)
        .set(user)
        .addOnSuccessListener {
            crearProgresoUsuario(nombre)
        }
        .addOnFailureListener { e ->
        }
}




// Función para crear la subcolección "progress" para un usuario
fun crearProgresoUsuario(nombreUsuario: String) {
    val progreso = hashMapOf(
        "Leccion1" to false,
        "Leccion2" to false,
        "Leccion3" to false
    )

    db.collection("Usuarios")
        .document(nombreUsuario)
        .collection("Progreso")
        .document("Curso1")
        .set(progreso)
        .addOnSuccessListener {
        }
        .addOnFailureListener { e ->
        }
}


// Crear un nuevo curso
fun crearCurso(nombre: String, descripcion: String){
    val course = hashMapOf(
        "name" to nombre,
        "description" to descripcion
    )

    db.collection("courses")
        .add(course)
        .addOnSuccessListener { documentReference ->
            println("Curso creado con ID: ${documentReference.id}")
        }
        .addOnFailureListener { e ->
            println("Error al crear el curso ${e}")
        }
}

// Crear una nueva lección
fun crearLeccion(courseId: String, titulo: String, contenido: String){
    val lesson = hashMapOf(
        "courseId" to courseId,
        "title" to titulo,
        "content" to contenido
    )

    db.collection("lessons")
        .add(lesson)
        .addOnSuccessListener { documentReference ->
            println("Lección creada con ID: ${documentReference.id}")
        }
        .addOnFailureListener { e ->
            println("Error al crear la lección ${e}")
        }
}




