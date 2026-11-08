package com.example.sa_app

data class Branch(
    val idBranch: Long,
    val branchId: String,   // เช่น B001
    val name: String
)

data class Employee(
    val employeeId: String,   // เช่น E001
    val idBranch: Long,
    val firstName: String,
    val lastName: String,
    val birthday: String,     // รูปแบบ yyyy-MM-dd ตรงกับ DATE ของ MySQL
    val phone: String,
    val email: String,
    val position: String,
    val salary: Int,
    val loginId: String,
    val password: String      // ตอนส่งขึ้นเซิร์ฟเวอร์ต้อง hash ก่อน
)