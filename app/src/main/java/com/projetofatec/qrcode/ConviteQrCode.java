package com.projetofatec.qrcode;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class ConviteQrCode extends AppCompatActivity {
    private ImageView imgConvite;
    private Button btnMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convite_qr_code);
        imgConvite = findViewById(R.id.imgConvite);
        btnMenu = findViewById(R.id.btnMenu);
        // Recuperar o URI da imagem passada pela MainActivity
        String imageUriString = getIntent().getStringExtra("image_uri");
        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            // Exibir a imagem usando Glide
            Glide.with(this).load(imageUri).into(imgConvite);
        }

        // Retornar ao menu
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConviteQrCode.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}