package com.android.cobasiapin.activity;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.cobasiapin.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Sementara tampilkan layout sederhana
        // Nanti akan diganti dengan Bottom Navigation
        setContentView(R.layout.activity_main);
    }
}