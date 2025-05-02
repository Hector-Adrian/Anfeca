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
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    @Test
    fun testRegistroCorreoDuplicado() {
        val email = "testuser_@example.com"
        val password = "123456"
        val latch = CountDownLatch(1)
        var errorCode: String? = null

        // Intenta registrar un correo ya usado
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    errorCode = it.exception?.message
                }
                latch.countDown()
            }

        latch.await(10, TimeUnit.SECONDS)
        assertTrue(errorCode?.contains("email address is already in use") == true)
    }

    @Test
    fun testRegistroExitoso() {
        val email = "nuevo${System.currentTimeMillis()}@example.com"
        val password = "123456"
        val nombre = "Nuevo Usuario"
        val latch = CountDownLatch(1)
        var fueGuardado = false

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid!!
                val datos = mapOf("name" to nombre, "email" to email)
                db.collection("Usuarios").document(uid).set(datos)
                    .addOnSuccessListener {
                        fueGuardado = true
                        latch.countDown()
                    }
            }

        latch.await(10, TimeUnit.SECONDS)
        assertTrue(fueGuardado)
    }

    @Test
    fun testLoginInvalido() {
        val email = "testuser_@example.com"
        val password = "wrongpass"
        val latch = CountDownLatch(1)
        var errorCode: String? = null

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    errorCode = it.exception?.message
                    latch.await(10, TimeUnit.SECONDS)
                    assertTrue(true)
                }
                latch.countDown()
            }


    }

    @Test
    fun testRecuperacionEmailValido() {
        val email = "testuser_@example.com"
        val latch = CountDownLatch(1)
        var enviado = false

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                enviado = it.isSuccessful
                latch.countDown()
            }

        latch.await(10, TimeUnit.SECONDS)
        assertTrue(enviado)
    }

    @Test
    fun testRecuperacionEmailNoValido() {
        val email = "noexiste@example.com"
        val latch = CountDownLatch(1)
        var fallo = false

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                fallo = !it.isSuccessful
                latch.countDown()
            }

        latch.await(10, TimeUnit.SECONDS)
        assertTrue(fallo)
    }

}