package com.projetofatec.qrcode.activites;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.projetofatec.qrcode.MainActivity;
import com.projetofatec.qrcode.R;

import java.util.HashMap;
import java.util.Map;

public class RegistrarActivity extends AppCompatActivity {

    private EditText edtNomeRegistrar, edtEmailRegistrar, edtSenhaRegistrar;
    private Button btnRegistrar1;
    private TextView txtVoltar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnRegistrar1 = findViewById(R.id.btnRegistrar1);
        edtNomeRegistrar = findViewById(R.id.edtNomeRegistrar);
        edtEmailRegistrar = findViewById(R.id.edtEmailRegistrar);
        edtSenhaRegistrar = findViewById(R.id.edtSenhaRegistrar);
        txtVoltar = findViewById(R.id.txtVoltar);

        txtVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrarActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnRegistrar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String nomeStr = edtNomeRegistrar.getText().toString().trim();
        String emailStr = edtEmailRegistrar.getText().toString().trim();
        String passwordStr = edtSenhaRegistrar.getText().toString().trim();

        if (nomeStr.isEmpty() || emailStr.isEmpty() || passwordStr.isEmpty()) {
            Toast.makeText(RegistrarActivity.this, "Nome, Email e senha precisam ser preenchidos", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(emailStr, passwordStr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                Log.d(TAG, "Usuário criado com sucesso");
                                saveUserToFirestore(user, nomeStr);
                            } else {
                                Log.e(TAG, "Usuário é nulo após criação");
                            }
                        } else {
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Erro desconhecido";
                            Toast.makeText(RegistrarActivity.this, "O cadastro falhou: " + errorMessage, Toast.LENGTH_LONG).show();
                            Log.e(TAG, "Erro ao criar usuário: " + errorMessage);
                        }
                    }
                });
    }

    private void saveUserToFirestore(FirebaseUser user, String nome) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("uid", user.getUid());
        userData.put("email", user.getEmail());
        userData.put("nome", nome);

        db.collection("usuarios").document(user.getUid())
                .set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(RegistrarActivity.this, "Usuário registrado na Firestore", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Usuário salvo com sucesso no Firestore");
                        updateUI(user, nome);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistrarActivity.this, "Erro ao salvar usuário: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Erro ao salvar usuário no Firestore: " + e.getMessage());
                    }
                });
    }

    private void updateUI(FirebaseUser user, String nome) {
        if (user != null) {
            Log.d(TAG, "Navegando para a MainActivity");
            Toast.makeText(RegistrarActivity.this, "Bem-vindo " + nome, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegistrarActivity.this, MainActivity.class);
            intent.putExtra("nomeUsuario", nome);
            startActivity(intent);
            finish();
        } else {
            Log.e(TAG, "Usuário é nulo ao tentar navegar para MainActivity");
        }
    }
}
