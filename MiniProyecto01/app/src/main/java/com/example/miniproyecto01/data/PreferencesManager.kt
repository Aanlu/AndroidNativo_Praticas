package com.example.miniproyecto01.data
import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("RegistroPreferencias", Context.MODE_PRIVATE)

    companion object {
        const val KEY_MATRICULA = "key_matricula"
        const val KEY_NOMBRE = "key_nombre"
        const val KEY_CARRERA = "key_carrera"
        const val KEY_TURNO = "key_turno"
        const val KEY_ACTIVO = "key_activo"
    }

    fun saveRegistro(
        matricula: String,
        nombre: String,
        carrera: String,
        turno: String,
        activo: Boolean
    ) {
        val editor =sharedPreferences.edit()
        editor.putString(KEY_MATRICULA, matricula)
        editor.putString(KEY_NOMBRE, nombre)
        editor.putString(KEY_CARRERA, carrera)
        editor.putString(KEY_TURNO, turno)
        editor.putBoolean(KEY_ACTIVO, activo)
        editor.apply()
    }

    fun getMatricula(): String {
        return sharedPreferences.getString(KEY_MATRICULA, "") ?: ""
    }

    fun getNombre(): String {
        return sharedPreferences.getString(KEY_NOMBRE, "") ?: ""
    }

    fun getCarrera(): String {
        return sharedPreferences.getString(KEY_CARRERA, "") ?: ""
    }

    fun getTurno(): String {
        return sharedPreferences.getString(KEY_TURNO, "Matutino") ?: "Matutino"
    }

    fun getActivo(): Boolean {
        return sharedPreferences.getBoolean(KEY_ACTIVO, true)
    }

    fun clearPreferenias() {
        sharedPreferences.edit().clear().apply()
    }

}