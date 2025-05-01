package com.example.anfeca

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class RegistroUsuarioTest {

    @Test
    fun crearUsuarioYGuardarEnFirestore() = runBlocking {
        val email = "testuser_${System.currentTimeMillis()}@example.com"
        val password = "123456"
        val nombre = "Test User"
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()

        val latch = CountDownLatch(1)
        var success = false

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val uid = authResult.user?.uid
                if (uid != null) {
                    val datos = mapOf("name" to nombre, "email" to email)
                    db.collection("Usuarios").document(uid)
                        .set(datos)
                        .addOnSuccessListener {
                            success = true
                            latch.countDown()
                        }
                        .addOnFailureListener {
                            println("Error al guardar en Firestore: ${it.message}")
                            latch.countDown()
                        }
                } else {
                    println("UID nulo luego de crear usuario.")
                    latch.countDown()
                }
            }
            .addOnFailureListener {
                println("Error al crear usuario: ${it.message}")
                latch.countDown()
            }

        latch.await(15, TimeUnit.SECONDS)

        assertTrue("Registro fallido o Firestore no guardó datos", success)
    }
}