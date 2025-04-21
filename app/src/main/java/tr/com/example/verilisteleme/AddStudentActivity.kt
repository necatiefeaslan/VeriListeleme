package tr.com.example.verilisteleme

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import tr.com.example.verilisteleme.databinding.ActivityAddStudentBinding

class AddStudentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStudentBinding
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val surname = binding.etSurname.text.toString().trim()
            val number = binding.etNumber.text.toString().trim()
            val note = binding.etNote.text.toString().trim()
            val performance = binding.etPerformance.text.toString().trim()

            if (name.isNotEmpty() && surname.isNotEmpty()) {
                val student = hashMapOf(
                    "name" to name,
                    "surname" to surname,
                    "number" to number,
                    "note" to note,
                    "performance" to performance
                )

                db.collection("students")
                    .add(student)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Öğrenci eklendi", Toast.LENGTH_SHORT).show()
                        finish() // MainActivity'e geri dön
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Hata oluştu: ${it.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
            } else {
                Toast.makeText(this, "Tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            }

        }
        binding.btnExit.setOnClickListener {
            finish()
        }
    }
}

