package com.example.sa_app

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun EmployeeFormScreen(
    nextId: String,
    branches: List<Branch>,
    onSave: (Employee) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    var branchIndex by rememberSaveable { mutableStateOf(-1) }
    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var birthday by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var position by rememberSaveable { mutableStateOf("") }
    var salary by rememberSaveable { mutableStateOf("") }
    var loginId by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var submitted by rememberSaveable { mutableStateOf(false) }
    var branchMenu by remember { mutableStateOf(false) }

    // เงื่อนไขที่ถูกต้องของแต่ละช่อง
    val birthdayDate = try { LocalDate.parse(birthday) } catch (e: Exception) { null }
    val okBranch = branchIndex in branches.indices
    val okFirst = firstName.isNotBlank()
    val okLast = lastName.isNotBlank()
    val okBirthday = birthdayDate != null && !birthdayDate.isAfter(LocalDate.now())
    val okPhone = phone.length == 10
    val okEmail = Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
    val okPosition = position.isNotBlank()
    val okSalary = salary.isNotEmpty()
    val okLogin = loginId.trim().length >= 4
    val okPassword = password.length >= 6
    val okConfirm = confirmPassword == password
    val allValid = okBranch && okFirst && okLast && okBirthday && okPhone &&
            okEmail && okPosition && okSalary && okLogin && okPassword && okConfirm

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("เพิ่มพนักงานใหม่", style = MaterialTheme.typography.headlineMedium)
        Text("รหัสพนักงาน: $nextId", style = MaterialTheme.typography.bodyLarge)

        // สาขา
        Box {
            OutlinedButton(
                onClick = { branchMenu = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (okBranch) "สาขา: ${branches[branchIndex].name}"
                    else "เลือกสาขา"
                )
            }
            DropdownMenu(expanded = branchMenu, onDismissRequest = { branchMenu = false }) {
                branches.forEachIndexed { index, b ->
                    DropdownMenuItem(
                        text = { Text("${b.branchId} - ${b.name}") },
                        onClick = {
                            branchIndex = index
                            branchMenu = false
                        }
                    )
                }
            }
        }
        if (submitted && !okBranch) {
            Text("กรุณาเลือกสาขา", color = MaterialTheme.colorScheme.error)
        }

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("ชื่อ") },
            singleLine = true,
            isError = submitted && !okFirst,
            supportingText = { if (submitted && !okFirst) Text("กรุณากรอกชื่อ") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("นามสกุล") },
            singleLine = true,
            isError = submitted && !okLast,
            supportingText = { if (submitted && !okLast) Text("กรุณากรอกนามสกุล") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = birthday,
            onValueChange = { birthday = it.filter { c -> c.isDigit() || c == '-' }.take(10) },
            label = { Text("วันเกิด (ปปปป-ดด-วว เช่น 1998-05-21)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = submitted && !okBirthday,
            supportingText = { if (submitted && !okBirthday) Text("วันเกิดไม่ถูกต้อง") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it.filter { c -> c.isDigit() }.take(10) },
            label = { Text("เบอร์โทร (10 หลัก)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            isError = submitted && !okPhone,
            supportingText = { if (submitted && !okPhone) Text("เบอร์โทรต้องมี 10 หลัก") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("อีเมล / Gmail") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = submitted && !okEmail,
            supportingText = { if (submitted && !okEmail) Text("อีเมลไม่ถูกต้อง") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = position,
            onValueChange = { position = it },
            label = { Text("ตำแหน่ง") },
            singleLine = true,
            isError = submitted && !okPosition,
            supportingText = { if (submitted && !okPosition) Text("กรุณากรอกตำแหน่ง") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = salary,
            onValueChange = { salary = it.filter { c -> c.isDigit() }.take(9) },
            label = { Text("เงินเดือน (บาท)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = submitted && !okSalary,
            supportingText = { if (submitted && !okSalary) Text("กรุณากรอกเงินเดือน") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = loginId,
            onValueChange = { loginId = it.filter { c -> !c.isWhitespace() } },
            label = { Text("ชื่อผู้ใช้ (login_id)") },
            singleLine = true,
            isError = submitted && !okLogin,
            supportingText = { if (submitted && !okLogin) Text("ต้องมีอย่างน้อย 4 ตัวอักษร") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("รหัสผ่าน") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = submitted && !okPassword,
            supportingText = { if (submitted && !okPassword) Text("ต้องมีอย่างน้อย 6 ตัวอักษร") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("ยืนยันรหัสผ่าน") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = submitted && !okConfirm,
            supportingText = { if (submitted && !okConfirm) Text("รหัสผ่านไม่ตรงกัน") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text("ยกเลิก")
            }
            Button(
                onClick = {
                    submitted = true
                    if (allValid) {
                        onSave(
                            Employee(
                                employeeId = nextId,
                                idBranch = branches[branchIndex].idBranch,
                                firstName = firstName.trim(),
                                lastName = lastName.trim(),
                                birthday = birthday,
                                phone = phone,
                                email = email.trim(),
                                position = position.trim(),
                                salary = salary.toInt(),
                                loginId = loginId.trim(),
                                password = password
                            )
                        )
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("บันทึก")
            }
        }
    }
}