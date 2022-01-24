package com.example.permissionlibraryexample;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.permissionlibrary.MyPermissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    MyPermissions myPermissions;
    public ActivityResultLauncher<String[]> multiplePermissionLauncher = registerForActivityResult
            (new ActivityResultContracts.RequestMultiplePermissions(), (Map<String, Boolean> isGranted) -> {

                List<String> grantedPermissions = new ArrayList<>();
                List<String> manualPermissions = new ArrayList<>();


                for (Map.Entry<String, Boolean> permissionEntry : isGranted.entrySet()) {
                    if (permissionEntry.getValue()) {
                        grantedPermissions.add(permissionEntry.getKey());
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionEntry.getKey())) {
                            myPermissions.requestPermission(permissionEntry.getKey());
                        } else {
                            manualPermissions.add(permissionEntry.getKey());
                        }
                    }
                }

                myPermissions.printGrantedPermissions(grantedPermissions);
                myPermissions.launchManualSettingsDialog(manualPermissions.toArray(new String[0]));
            });
    public ActivityResultLauncher<Intent> manuallyPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    myPermissions.checkPermissions();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myPermissions = new MyPermissions(this, multiplePermissionLauncher, manuallyPermissionLauncher);

        // request all the permissions Location,Contact,Camera.
        myPermissions.launchRequestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_CONTACTS, Manifest.permission.CAMERA});
    }
}