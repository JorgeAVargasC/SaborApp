package com.example.saborapp.views

// Desarrollo del SignUp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.saborapp.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    //ViewBinding
    private lateinit var binding: ActivitySignupBinding

    //ActionBar
    private lateinit var actionBar: ActionBar

    //ProgressDialog
    private lateinit var progressDialog: ProgressDialog

    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ActionBar Config
        actionBar = supportActionBar!!
        //actionBar.title = "Registrate"
        //actionBar.setDisplayHomeAsUpEnabled(true)
        //actionBar.setDisplayShowHomeEnabled(true)
        actionBar.hide()

        // Progress Dialog Config
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Creating account...")
        progressDialog.setCanceledOnTouchOutside(false)

        //init FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnSignUp.setOnClickListener{
            // Validamos los datos
            validateData()
        }
    }

    private fun validateData() {
        // Obtenemos los datos
        email = binding.etEmail.text.toString().trim()
        password = binding.passwordET.text.toString().trim()

        // Validamos los datos
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            // Datos invalidos
            binding.etEmail.error = "Formato de email invalido"
        }
        else if(password.length <6){
            // Password length is less than
            binding.passwordET.error = "La contraseña debe tener almenos 6 caracteres"
        }
        else{
            // Datos validos
            firebaseSignUp()
        }
    }

    private fun firebaseSignUp() {
        // Mostrar progreso
        progressDialog.show()

        // Crear Cuenta
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // Registro Exitoso
                progressDialog.dismiss()

                // Obtener Usuario Actual
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this,"Cuenta creada correctamente con email $email", Toast.LENGTH_SHORT).show()

                //Inciar Actividad
                startActivity(Intent(this, ProfileActivity::class.java))
                finish()
            }
            .addOnFailureListener{ e->
                // Registro Fallido
                Toast.makeText(this,"Fallo en el registro ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        // Volver a la actividad previa
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}