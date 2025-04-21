package tr.com.example.verilisteleme

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tr.com.example.verilisteleme.databinding.ItemStudentCardBinding

class StudentAdapter(
    private val studentList: List<Student>,
    private val onItemClick: (Student) -> Unit // 👈 Detay için lambda fonksiyonu

) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    inner class StudentViewHolder(val binding: ItemStudentCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(student: Student) {
            binding.tvName.text = "${student.name} ${student.surname}"
            binding.tvNumber.text = "Numara: ${student.number}"

            // 👇 Burada tıklanma olayını tanımlıyoruz
            binding.root.setOnClickListener {
                onItemClick(student)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding = ItemStudentCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList[position]
        holder.bind(student)
    }

    override fun getItemCount(): Int = studentList.size
}
