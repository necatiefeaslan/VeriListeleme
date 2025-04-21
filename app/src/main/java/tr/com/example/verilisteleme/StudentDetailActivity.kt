package tr.com.example.verilisteleme

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import tr.com.example.verilisteleme.databinding.ActivityStudentDetailBinding

class StudentDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val student = intent.getSerializableExtra("student", Student::class.java)


        student?.let {
            binding.tvDetailName.text = "${it.name} ${it.surname}"
            binding.tvDetailNumber.text = "Numara: ${it.number}"
            binding.tvDetailNote.text = "Not: ${it.note}"
            binding.tvDetailPerformance.text = "Performans: ${it.performance}"
        }

        binding.button.setOnClickListener {
            finish()
        }
    }
}
