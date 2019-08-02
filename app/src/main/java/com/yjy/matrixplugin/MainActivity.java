package com.yjy.matrixplugin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yjy.matrixplugin.io.TestIOActivity;
import com.yjy.matrixplugin.issue.IssuesMap;
import com.yjy.matrixplugin.resource.TestLeakActivity;
import com.yjy.matrixplugin.sqlitelint.TestSQLiteLintActivity;
import com.yjy.matrixplugin.trace.TestTraceMainActivity;

public class MainActivity extends AppCompatActivity {
    private static final int EXTERNAL_STORAGE_REQ_CODE = 0x1;


    @Override
    protected void onResume() {
        super.onResume();
        IssuesMap.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button testTrace = (Button) findViewById(R.id.test_trace);
        testTrace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestTraceMainActivity.class);
                startActivity(intent);
            }
        });

        Button testIO = (Button) findViewById(R.id.test_io);
        testIO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestIOActivity.class);
                startActivity(intent);
            }
        });

        Button testLeak = (Button) findViewById(R.id.test_leak);
        testLeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestLeakActivity.class);
                startActivity(intent);
            }
        });

        Button testSQLiteLint = (Button) findViewById(R.id.test_sqlite_lint);
        testSQLiteLint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestSQLiteLintActivity.class);
                startActivity(intent);
            }
        });

    }


    private void requestPer() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "please give me the permission", Toast.LENGTH_SHORT).show();

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        EXTERNAL_STORAGE_REQ_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case EXTERNAL_STORAGE_REQ_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                break;
        }
    }
}
