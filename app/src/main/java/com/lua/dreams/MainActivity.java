package com.lua.dreams;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.lua.dreams.ui.sonhos.SonhosFragment;
import com.lua.dreams.ui.sonhos.SonhosObject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


            setContentView(R.layout.activity_main);
            loadFragment(new SonhosFragment());
            BottomNavigationView navigation = findViewById(R.id.nav_view);
            navigation.setOnNavigationItemSelectedListener(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionsmenu, menu);
        return true;
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.navigation_sonhos:
                fragment = new SonhosFragment();
                break;
            case R.id.navigation_sonhosBackup:
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
                LinearLayout layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.VERTICAL);
                builder.setIcon(getDrawable(R.drawable.image));
                builder.setTitle(getString(R.string.copiadeseguranca));
                layout.setGravity(Gravity.CENTER);
                final TextView espaco4 = new TextView(this);
                espaco4.setText(getString(R.string.pretendesfazercopiadeseguranca));
                espaco4.setTextSize(19);
                espaco4.setGravity(Gravity.CENTER);
                layout.addView(espaco4);
                final TextView espaco2 = new TextView(this);
                espaco2.setText(getString(R.string.oficheirovaiserguardadoondeescolher));
                espaco2.setTextSize(25);
                espaco2.setGravity(Gravity.CENTER);
                layout.addView(espaco2);
                builder.setView(layout);
                builder.setPositiveButton(getString(R.string.ok), (dialog, which) -> BackupSonhos());
                builder.setNeutralButton(getString(R.string.cancelar), (dialog, which) -> dialog.dismiss());
                builder.show();
                break;
        }
        return loadFragment(fragment);
    }

    private void getPermissions() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    private static final int WRITE_REQUEST_CODE = 101;
    private void BackupSonhos() {
        getPermissions();
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, "MoonDreams_BackupSonhos.txt");

        startActivityForResult(intent, WRITE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WRITE_REQUEST_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    if (data != null
                            && data.getData() != null) {
                        writeInFile(data.getData());
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(getSupportFragmentManager().getFragments().isEmpty()){
            this.finish();
        }
    }

    private void writeInFile(@NonNull Uri uri) {
        OutputStream outputStream;
        try {
            SharedPreferences prefs = getSharedPreferences("MoonDreams", MODE_PRIVATE);
            String SonhosFull = prefs.getString("MoonDreams", "");
            String[] SonhosDivided = SonhosFull.split("§");
            ArrayList<SonhosObject> sonhos = new ArrayList<>();
            for(int i = 0 ; i < SonhosDivided.length; i++){
                SonhosObject sonhosNew = new SonhosObject();
                String[] Sonho = SonhosDivided[i].split("»");
                if(Sonho[0].length() > 2){
                    sonhosNew.id = String.valueOf(i);
                    sonhosNew.data = Sonho[0];
                    sonhosNew.titulo = Sonho[1];
                    sonhosNew.sonho = Sonho[2];
                    sonhos.add(sonhosNew);
                }
            }
            if(!sonhos.isEmpty()){
                String filetoSaveString = "";
                for(int j = 0; j < sonhos.size(); j++){
                    filetoSaveString = filetoSaveString + sonhos.get(j).getId() + " - Sonho do dia " + sonhos.get(j).getData() + "\n\n" + sonhos.get(j).getTitulo() + "\n\n" + sonhos.get(j).getSonho() + "\n\n_____________________________________" + "\n\n\n";
                }
                outputStream = getContentResolver().openOutputStream(uri);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
                bw.write(filetoSaveString);
                bw.flush();
                bw.close();
                Snackbar.make(findViewById(android.R.id.content), getString(R.string.foifeitaumacopiadeseguranca), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left)
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
