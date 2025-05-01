package com.example.anfeca

import org.junit.Assert.*
import org.junit.Test

class ValidacionRegistroTest {

    //Fragmento del registro de datos que checa longitudes
    private fun validarCampos(nombre: String, correo: String, contrasena: String): Boolean {
        return nombre.isNotBlank() && correo.isNotBlank() && contrasena.length >= 6
    }

    @Test
    fun camposValidos_devuelveTrue() {
        val resultado = validarCampos("Ana", "ana@correo.com", "123456")
        assertTrue(resultado)
    }

    @Test
    fun nombreVacio_devuelveFalse() {
        val resultado = validarCampos("", "ana@correo.com", "123456")
        assertFalse(resultado)
    }

    @Test
    fun correoVacio_devuelveFalse() {
        val resultado = validarCampos("Ana", "", "123456")
        assertFalse(resultado)
    }

    @Test
    fun contrasenaCorta_devuelveFalse() {
        val resultado = validarCampos("Ana", "ana@correo.com", "123")
        assertFalse(resultado)
    }
}
