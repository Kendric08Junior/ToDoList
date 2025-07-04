package com.example.notepad

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notepad.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()


        binding.textView.setOnClickListener {
            var intent = Intent(this, Signin::class.java)
            startActivity(intent)
        }
        binding.button.setOnClickListener {


            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()
            if (email.isNotEmpty() && pass.isNotEmpty()) {

               firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener { it: Task<AuthResult>->
                 if (it.isSuccessful) {
                     val intent= Intent(this, MainActivity::class.java)
                     startActivity(intent)
                 }else{
                     Toast.makeText(this,"incorrect password or email", Toast.LENGTH_SHORT).show()
                 }
               }
            } else {
                Toast.makeText(this, "Make sure all fields are full", Toast.LENGTH_SHORT).show()
            }
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }
    }
}