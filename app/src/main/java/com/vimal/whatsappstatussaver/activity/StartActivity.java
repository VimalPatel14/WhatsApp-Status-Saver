package com.vimal.whatsappstatussaver.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tedpark.tedpermission.rx2.TedRx2Permission;
import com.vimal.whatsappstatussaver.R;

public class StartActivity extends AppCompatActivity {

    Button btn_wahst_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        btn_wahst_up = findViewById(R.id.whast_up);

        btn_wahst_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TedRx2Permission.with(StartActivity.this)
                        .setRationaleTitle("Can we read your storage?")
                        .setRationaleMessage("We need your permission to access your storage")
                        .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .request()
                        .subscribe(permissionResult -> {
                                    if (permissionResult.isGranted()) {
                                        Intent intent = new Intent(StartActivity.this, StatusSaverActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getBaseContext(),
                                                "Permission Denied\n" + permissionResult.getDeniedPermissions().toString(), Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                }
                        );


            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        System.exit(0);
    }
}