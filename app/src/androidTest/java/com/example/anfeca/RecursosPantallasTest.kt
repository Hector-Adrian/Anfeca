package com.example.anfeca

import org.junit.Assert.*
import org.junit.Test
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

class RecursosTest {

    // Simula que un video se puede reproducir
    @Test
    fun testCargaVideoDesdeUrl() {
        val url = URL("https://res.cloudinary.com/dbtognomm/video/upload/v1746321507/navida_u5aazz.mp4")
        val connection = url.openConnection() as HttpURLConnection
        connection.connectTimeout = 5000
        connection.readTimeout = 5000
        connection.connect()

        val contentLength = connection.contentLength
        assertTrue("El video debería tener contenido", contentLength > 0)
        assertEquals(HttpURLConnection.HTTP_OK, connection.responseCode)

        connection.disconnect()
    }

    // Simula descargar un documento desde la red y guardarlo en disco local
    @Test
    fun testDescargarPdfLeccion() {
        val url = URL("https://res.cloudinary.com/dbtognomm/raw/upload/v1746325848/Lorem_Ipsum_pulfqd.docx")
        val connection = url.openConnection() as HttpURLConnection
        connection.connect()
        val input = connection.inputStream

        val archivo = File("leccion_descargada.docx")
        archivo.outputStream().use { output ->
            input.copyTo(output)
        }

        assertTrue("El documento debería haberse guardado", archivo.exists())
        assertTrue("El archivo no debería estar vacío", archivo.length() > 0)

        archivo.delete()
        connection.disconnect()
    }

    // Simula acceso a un archivo descargado
    @Test
    fun testAccesoLeccionSinConexion() {
        val archivo = File("leccion_offline.pdf")
        archivo.writeText("Este es un ejemplo de lección")

        assertTrue("El contenido debe estar disponible sin conexión", archivo.exists())
        assertEquals("Este es un ejemplo de lección", archivo.readText())

        archivo.delete()
    }
}
