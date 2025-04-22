package tr.com.example.verilisteleme

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import tr.com.example.verilisteleme.databinding.ActivityStudentDetailBinding

class StudentDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentDetailBinding
    private var student: Student? = null
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        student = intent.getSerializableExtra("student", Student::class.java)
        setupViews()
        setupClickListeners()
    }

    private fun setupViews() {
        student?.let {
            binding.etDetailName.setText("${it.name} ${it.surname}")
            binding.etDetailNumber.setText(it.number)
            binding.etDetailNote.setText(it.note.toString())
            binding.etDetailPerformance.setText(it.performance.toString())
        }
    }

    private fun setupClickListeners() {
        binding.btnUpdate.setOnClickListener { updateStudent() }
        binding.btnBack.setOnClickListener { finish() }
        binding.btnClear.setOnClickListener { clearFields() }
    }

    private fun clearFields() {
        binding.etDetailName.setText("")
        binding.etDetailNumber.setText("")
        binding.etDetailNote.setText("")
        binding.etDetailPerformance.setText("")
        Toast.makeText(this, "Ad soyad, numara, not ve performans değerleri temizlendi", Toast.LENGTH_SHORT).show()
    }

    private fun isValidGrade(value: String): Boolean {
        return try {
            val number = value.toFloat()
            number in 0f..100f
        } catch (e: NumberFormatException) {
            false
        }
    }

    private fun updateStudent() {
        val fullName = binding.etDetailName.text.toString()
        val number = binding.etDetailNumber.text.toString()
        val note = binding.etDetailNote.text.toString()
        val performance = binding.etDetailPerformance.text.toString()

        if (fullName.isBlank() || number.isBlank() || note.isBlank() || performance.isBlank()) {
            Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            return
        }

        // Not ve performans kontrolü
        if (!isValidGrade(note)) {
            Toast.makeText(this, "Not değeri 0-100 arasında olmalıdır", Toast.LENGTH_SHORT).show()
            return
        }

        if (!isValidGrade(performance)) {
            Toast.makeText(this, "Performans değeri 0-100 arasında olmalıdır", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val nameParts = fullName.split(" ")
            val name = nameParts.firstOrNull() ?: ""
            val surname = nameParts.drop(1).joinToString(" ")

            student?.let { currentStudent ->
                val updatedData = hashMapOf(
                    "name" to name,
                    "surname" to surname,
                    "number" to number,
                    "note" to note,
                    "performance" to performance
                )

                db.collection("students")
                    .document(currentStudent.id)
                    .update(updatedData as Map<String, Any>)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Bilgiler güncellendi", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Güncelleme başarısız: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } ?: run {
                Toast.makeText(this, "Öğrenci bilgisi bulunamadı", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Güncelleme başarısız oldu", Toast.LENGTH_SHORT).show()
        }
    }
}
