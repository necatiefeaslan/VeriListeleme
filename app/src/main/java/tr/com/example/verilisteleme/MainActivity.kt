package tr.com.example.verilisteleme

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import tr.com.example.verilisteleme.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val db = Firebase.firestore
    private val studentList = mutableListOf<Student>()
    private lateinit var adapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Adapter'ı oluşturuyoruz ve RecyclerView'e bağlıyoruz
        adapter = StudentAdapter(studentList) { student ->
            val intent = Intent(this, StudentDetailActivity::class.java)
            intent.putExtra("student", student)
            startActivity(intent)
        }


        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // FloatingActionButton ile öğrenci ekleme ekranına yönlendirme
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddStudentActivity::class.java)
            startActivity(intent)
        }

        // Öğrencileri Firestore'dan yükleme
        loadStudents()

        // Swipe-to-dismiss özelliği ekleniyor
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Öğrenciyi silmek için Firestore'dan silme işlemi yapılacak
                val student = studentList[viewHolder.adapterPosition]
                deleteStudent(student)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun loadStudents() {
        db.collection("students")
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                studentList.clear()
                snapshot?.forEach { doc ->
                    val student = doc.toObject(Student::class.java)
                    student.id = doc.id  // Firestore'dan alınan belge kimliğini ekliyoruz
                    studentList.add(student)
                }
                adapter.notifyDataSetChanged()
            }
    }

    private fun deleteStudent(student: Student) {
        val studentRef = db.collection("students").document(student.id)  // Firestore'dan alınan ID'yi kullanıyoruz
        studentRef.delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Öğrenci silindi", Toast.LENGTH_SHORT).show()
                loadStudents()  // Silindikten sonra listeyi tekrar yükleyelim
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Hata oluştu: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
