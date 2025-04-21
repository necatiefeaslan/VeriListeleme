package tr.com.example.verilisteleme

import java.io.Serializable

data class Student(
    val name: String = "",
    val surname: String = "",
    val number: String = "",
    val note: String = "",
    val performance: String = "",
    var id: String = "",
): Serializable
