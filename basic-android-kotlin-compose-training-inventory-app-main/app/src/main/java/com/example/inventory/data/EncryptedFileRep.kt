package com.example.inventory.data

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import com.example.inventory.appContext
import kotlinx.serialization.json.Json
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.StandardCharsets


class EncryptedFileRep(val selectedUri: Uri) {
    private val masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private fun copyStream(input: InputStream, output: OutputStream) {
        val buffer = ByteArray(1024)
        var bytesRead: Int
        while (input.read(buffer).also { bytesRead = it } != -1) {
            output.write(buffer, 0, bytesRead)
        }
    }
    fun WriteToFile(item:Item){
        val file: File = File(appContext!!.getExternalFilesDir(null), "secret_data")
        val encryptedFile = EncryptedFile.Builder(
            file,
            appContext!!,
            masterKey,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()

        //to JSON
        val itemJsonStr = Json.encodeToString(Item.serializer(), item)


        // write to the encrypted file
        encryptedFile.openFileOutput().use {
            it.write(itemJsonStr.toByteArray(StandardCharsets.UTF_8))
            it.flush()
        }

        //read from file and write to destination
        appContext!!.contentResolver.openOutputStream(selectedUri)?.use {
            file.inputStream().use{input ->
                copyStream(input, it)
            }

        }

        file.delete()
    }

    fun ReadFromFile():Item?{
        try {
            val file: File = File(appContext!!.getExternalFilesDir(null), "secret_data")
            val encryptedFile = EncryptedFile.Builder(
                file,
                appContext!!,
                masterKey,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build()

            selectedUri.let { appContext!!.contentResolver.openInputStream(it) }?.use{inp ->
                encryptedFile.openFileOutput().use { out ->
                    copyStream(inp, out)
                }
            }

            val fileContent = ByteArray(36000)
            val numBytesRead: Int
            encryptedFile.openFileInput().use {
                numBytesRead = it.read(fileContent)
            }
            file.delete()


            val itemJsonStr = String(fileContent, 0, numBytesRead)
            val item = Json.decodeFromString(Item.serializer(), itemJsonStr)
            item.creationMethod = "file"
            item.id = 0
            return item

        }
        catch (exception: Exception){
            Log.d("READ ERROR", exception.toString())
            Toast.makeText(appContext!!, "Reading Error", Toast.LENGTH_SHORT).show()
            return null
        }
    }

}