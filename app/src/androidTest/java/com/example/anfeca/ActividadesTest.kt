package com.example.anfeca

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class ActividadesTest {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Define qué curso y lección se prueba
    private val cursoId = "Curso1"
    private val leccionId = "LeccionP"

    @Test
    fun testCompletarYReiniciarLeccion() = runBlocking {
        val emailUsuario = auth.currentUser?.email ?: error("No hay usuario autenticado")

        // 1. Buscar nombre de usuario por correo
        val userSnapshot = db.collection("Usuarios")
            .whereEqualTo("email", emailUsuario)
            .get()
            .await()

        val userDoc = userSnapshot.documents.firstOrNull()
            ?: error("Usuario no encontrado")

        val nombreUsuario = userDoc.id

        //Marcar completado
        db.collection("Usuarios")
            .document(nombreUsuario)
            .collection("Progreso")
            .document(cursoId)
            .update(leccionId, true)
            .await()

        //Checar
        val progresoDoc = db.collection("Usuarios")
            .document(nombreUsuario)
            .collection("Progreso")
            .document(cursoId)
            .get()
            .await()

        val completado = progresoDoc.getBoolean(leccionId)
        assertEquals(true, completado)

        //Borrar  progreso
        db.collection("Usuarios")
            .document(nombreUsuario)
            .collection("Progreso")
            .document(cursoId)
            .update(leccionId, false)
            .await()

        // Checar
        val progresoReiniciado = db.collection("Usuarios")
            .document(nombreUsuario)
            .collection("Progreso")
            .document(cursoId)
            .get()
            .await()

        val reiniciado = progresoReiniciado.getBoolean(leccionId)
        assertEquals(false, reiniciado)
    }
}
