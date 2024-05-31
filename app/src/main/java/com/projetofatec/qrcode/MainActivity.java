package com.projetofatec.qrcode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    public Button btnEntrar;
    public ImageView imgGif;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btnEntrar = findViewById(R.id.btnEntrar);
        imgGif = findViewById(R.id.imgGif);

        Glide.with(this)
                .asGif()
                .load(R.drawable.imgqrcode) // substitua "seu_gif" pelo nome do arquivo GIF na pasta drawable
                .into(imgGif);

         btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}