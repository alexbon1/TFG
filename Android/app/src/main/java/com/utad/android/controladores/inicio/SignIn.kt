package com.utad.android.controladores.inicio

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.utad.android.R
import com.utad.android.controladores.dialogs.Error.Companion.showError
import com.utad.android.controladores.inventario.AjustesUsuario
import com.utad.android.entitys.UsersEntity
import com.utad.android.code.modelo.AsincronoEjecutorDeConexiones
import com.utad.android.databinding.ActivitySignInBinding
import com.utad.android.storage.Mochila
import com.utad.android.storage.UtilidadesJSON
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*

class SignIn : AppCompatActivity() {
    val NOMBREVISTA = "SignIn";
    private lateinit var binding: ActivitySignInBinding
    private  var pre:String? = null

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        val intent = intent
        pre =intent.getStringExtra("origen")
        binding = ActivitySignInBinding.inflate(layoutInflater)
        binding.buttonNext.isEnabled = false
        binding.buttonNext.setBackgroundColor(Color.GRAY)

        binding.buttonNext.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                if (validacionesCampos()) {
                    if(pre.equals(MainActivity.NOMBREVISTA)){
                        if (tryToSignIn()){
                            val intent = Intent(this@SignIn, CreadorPersonaje::class.java)
                            intent.putExtra("origen", NOMBREVISTA)
                            if (!isFinishing) {
                                startActivity(intent)
                            }
                        }
                    }else if(pre.equals(AjustesUsuario.NOMBREVISTA)){
                        if (tryToChange()){
                            val intent = Intent(this@SignIn, MainActivity::class.java)
                            intent.putExtra("origen", NOMBREVISTA)
                            if (!isFinishing) {
                                startActivity(intent)
                            }
                        }
                    }
                }
            }
        }

        binding.buttonReturn.setOnClickListener {
            finish()
        }


        binding.editTextEmail.addTextChangedListener(textWatcher)
        binding.editTextUsername.addTextChangedListener(textWatcher)
        binding.editTextName.addTextChangedListener(textWatcher)
        binding.editTextSurname.addTextChangedListener(textWatcher)
        binding.editTextPassword.addTextChangedListener(textWatcher)

        val months = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")
        val years = arrayListOf<String>()
        val currentYear = LocalDate.now().year
        for (i in currentYear downTo currentYear - 70) {
            years.add(i.toString())
        }

        val monthAdapter = ArrayAdapter(this, R.layout.custom_spinner_item, months)
        monthAdapter.setDropDownViewResource(R.layout.custom_spinner_item)
        binding.monthSpinner.adapter = monthAdapter

        val yearAdapter = ArrayAdapter(this, R.layout.custom_spinner_item, years)
        yearAdapter.setDropDownViewResource(R.layout.custom_spinner_item)
        binding.yearSpinner.adapter = yearAdapter

        // Listener para detectar cambios en el mes o año seleccionado
        binding.monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                updateDaySpinner()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No hacemos nada si no se selecciona nada
            }
        }

        binding.yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                updateDaySpinner()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        if (pre.equals(AjustesUsuario.NOMBREVISTA)){
            val string = Mochila.user.fechaDeNacimiento
            val s2 = string.split('-')
            val dia = Integer.parseInt(s2[2])
            val mes = Integer.parseInt(s2[1])
            val ano = buscarAnoEnSpinner(Integer.parseInt(s2[0]))
            binding.buttonNext.text="Aceptar"
            binding.editTextPassword.visibility=View.INVISIBLE
            binding.editTextPassword.setText(Mochila.user.pwd)
            binding.yearSpinner.setSelection(ano-1)
            binding.monthSpinner.setSelection(mes-1)
            updateDaySpinner()
            binding.daySpinner.setSelection(dia-1)
            binding.editTextUsername.setText(Mochila.user.username)
            binding.editTextName.setText(Mochila.user.nombre)
            binding.editTextSurname.setText(Mochila.user.apellidos)
            binding.editTextEmail.setText(Mochila.user.email)
            binding.textViewTitle.text="Modificar"
        }
        setContentView(binding.root)

    }

    private fun buscarAnoEnSpinner(year: Int): Int {
        for (i in 0..<binding.yearSpinner.adapter.count) {
            if (binding.yearSpinner.adapter.getItem(i).toString() == year.toString()) {
                return i
            }
        }
        return 0
    }

    private suspend fun tryToChange(): Boolean {
        val fechaNacimiento = binding.yearSpinner.selectedItem.toString()+"-"+binding.monthSpinner.selectedItem.toString()+"-"+binding.daySpinner.selectedItem.toString();
        val username = binding.editTextUsername.text.toString();
        val nombre = binding.editTextName.text.toString()
        val apellido = binding.editTextSurname.text.toString()
        val email = binding.editTextEmail.text.toString()
        val pwd = binding.editTextPassword.text.toString()
        val user = UsersEntity(
            username,
            nombre,
            apellido,
            email,
            fechaNacimiento,
            pwd
        )
        user.id=Mochila.user.id
        return AsincronoEjecutorDeConexiones(AsincronoEjecutorDeConexiones.METODOS.MODIFY_ALL_USER, user).execute() as Boolean
    }

    private suspend fun tryToSignIn(): Boolean {
        val fechaNacimiento = binding.yearSpinner.selectedItem.toString()+"-"+binding.monthSpinner.selectedItem.toString()+"-"+binding.daySpinner.selectedItem.toString();
        val username = binding.editTextUsername.text.toString();
        val nombre = binding.editTextName.text.toString()
        val apellido = binding.editTextSurname.text.toString()
        val email = binding.editTextEmail.text.toString()
        val pwd = binding.editTextPassword.text.toString() // Asegúrate de que este valor sea válido
        val user = UsersEntity(
            username,
            nombre,
            apellido,
            email,
            fechaNacimiento,
            pwd
        )
        val ejecutor: AsincronoEjecutorDeConexiones = AsincronoEjecutorDeConexiones(AsincronoEjecutorDeConexiones.METODOS.SIGN_IN,user)
        val result = ejecutor.execute()
        return if (result != null && result is Boolean) {
            if (!result){
                showError(this,"Error en la creacion del usuario")
            }else{
                UtilidadesJSON.GuardarUser(applicationContext,user)
            }
            result
        }else{
            false
        }
    }


    private fun obtenerFechaEnMilisegundos(dia: Int, mes: Int, ano: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, ano)
        calendar.set(Calendar.MONTH, mes - 1) // Los meses en Calendar empiezan desde 0
        calendar.set(Calendar.DAY_OF_MONTH, dia)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
    private suspend fun validacionesCampos(): Boolean {
        return isValidEmail(binding.editTextEmail.text.toString()) and isValidUsername(binding.editTextUsername.text.toString()) and validarPWD(binding.editTextPassword.text.toString())
    }

    private suspend fun isValidUsername(username: String): Boolean {
        if (pre== MainActivity.NOMBREVISTA){
            val args = arrayOf(username)
            val ejecutor = AsincronoEjecutorDeConexiones(AsincronoEjecutorDeConexiones.METODOS.EXISTE_USERNAME,args)
            val result = ejecutor.execute()
            return if (result != null && result is Boolean) {
                if (result){
                    showError(this,"Usuario no valido")
                }
                !result

            }else{
                false
            }
        }else{
            return true
        }
    }
    private fun validarPWD(pwd: String): Boolean {
        val longitudRequerida = 8
        val contieneMayuscula = pwd.any { it.isUpperCase() }
        val contieneMinuscula = pwd.any { it.isLowerCase() }
        val contieneDigito = pwd.any { it.isDigit() }
        val contieneCaracterEspecial = pwd.any { !it.isLetterOrDigit() }

        if (pwd.length < longitudRequerida) {
            showError(this, "La contraseña debe tener al menos 8 caracteres.")
            return false
        }

        if (!contieneMayuscula) {
            showError(this, "La contraseña debe incluir al menos una letra mayúscula.")
            return false
        }

        if (!contieneMinuscula) {
            showError(this, "La contraseña debe incluir al menos una letra minúscula.")
            return false
        }

        if (!contieneDigito) {
            showError(this, "La contraseña debe incluir al menos un dígito.")
            return false
        }

        if (!contieneCaracterEspecial) {
            showError(this, "La contraseña debe incluir al menos un carácter especial.")
            return false
        }

        // Si la contraseña cumple con todas las condiciones, retornamos true
        return true
    }



    private fun updateDaySpinner() {
        var selectedYear = 0
        var selectedMonth=0
        if ( binding.yearSpinner.selectedItem == null) {
            return
        }else{
             selectedYear = binding.yearSpinner.selectedItem.toString().toInt()
        }
        if ( binding.monthSpinner.selectedItemPosition == null) {
            return
        }else{
            selectedMonth = binding.monthSpinner.selectedItemPosition + 1 // Los meses comienzan desde 0
        }
        val daysInMonth = getDaysInMonth(selectedMonth, selectedYear)

        val days = arrayListOf<String>()
        for (i in 1..daysInMonth) {
            days.add(i.toString())
        }

        val dayAdapter = ArrayAdapter(this, R.layout.custom_spinner_item, days)
        dayAdapter.setDropDownViewResource(R.layout.custom_spinner_item)
        binding.daySpinner.adapter = dayAdapter
    }

    private fun getDaysInMonth(month: Int, year: Int): Int {
        return when (month) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
            else -> throw IllegalArgumentException("Invalid month")
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // No necesitamos implementar esta función en este caso
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let {
                val filteredText = removeDangerousCharacters(it.toString())
                if (filteredText != it.toString()) {
                    val array = arrayOf( binding.editTextUsername,binding.editTextPassword,binding.editTextName,binding.editTextSurname,binding.editTextEmail)
                    for (a in array){
                        if(a.text.toString() == it.toString()){
                            a.setText(filteredText)
                        }
                    }
                }
            }
            // Verifica si todos los campos de texto están llenos y activa el botón según sea necesario
            activacionButton(isEditTextFilled())
        }


        override fun afterTextChanged(s: Editable?) {
            // No necesitamos implementar esta función en este caso
        }

        private fun removeDangerousCharacters(text: String): String {
            // Utiliza una expresión regular para eliminar los caracteres peligrosos para SQL
            return text.replace(Regex("[\"';*-]"), "")
        }
    }

    private fun activacionButton(editTextFilled: Boolean) {
        if (editTextFilled){
            binding.buttonNext.setBackgroundColor(Color.WHITE)
        }else{
            binding.buttonNext.setBackgroundColor(Color.GRAY)
        }
        binding.buttonNext.isEnabled = editTextFilled
    }

    private fun isEditTextFilled(): Boolean {
        return binding.editTextEmail.text.isNotEmpty() &&
                binding.editTextUsername.text.isNotEmpty() &&
                binding.editTextName.text.isNotEmpty() &&
                binding.editTextSurname.text.isNotEmpty() &&
                binding.editTextPassword.text.isNotEmpty()
    }
    fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
        val ret = emailRegex.matches(email)
        if (!ret){
            showError(this,"Email no valido")
        }
        return ret
    }
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {

    }
}
