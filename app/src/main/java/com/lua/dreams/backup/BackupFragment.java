package com.lua.dreams.backup;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.lua.dreams.MainActivity;
import com.lua.dreams.R;
import com.lua.dreams.ferramentas.TimePickerFragment;
import com.lua.dreams.ui.sonhos.SonhosNovoFragment;
import com.lua.dreams.ui.sonhos.SonhosObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class BackupFragment extends Fragment {
    private View root;
    public static int PICK_FILE = 1;

    public BackupFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_backup, container, false);
        MaterialButton btn_backupFicheiro = root.findViewById(R.id.btn_backup_ficheiro);
        MaterialButton btn_restoreFromFile = root.findViewById(R.id.btn_restore_ficheiro);
        MaterialButton btn_ficheiroReadable = root.findViewById(R.id.btn_Backup_ficheiro_Readable);
        ImageView btn_telegram = root.findViewById(R.id.btn_telegram);
        ImageView btn_paypal = root.findViewById(R.id.btn_paypal);
        ImageView btn_github = root.findViewById(R.id.btn_github);
        MaterialButton btnProgramarRelogio = root.findViewById(R.id.btnprogramclock);
        btnProgramarRelogio.setOnClickListener(v -> {
            TimePickerFragment dialog = new TimePickerFragment();
            dialog.show(getActivity().getSupportFragmentManager(), "");
        });
        btn_github.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/LN-Dark"));
            startActivity(browserIntent);
        });
        btn_telegram.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/LN_DarK"));
            startActivity(browserIntent);
        });
        btn_paypal.setOnClickListener(v -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(root.getContext());
            LinearLayout layout = new LinearLayout(root.getContext());
            layout.setOrientation(LinearLayout.VERTICAL);
            builder.setIcon(root.getContext().getDrawable(R.drawable.sweatdreams));
            builder.setTitle(getString(R.string.donate));
            layout.setGravity(Gravity.CENTER);
            final TextView espaco4 = new TextView(root.getContext());
            espaco4.setText(getString(R.string.thanksforyourdonation));
            espaco4.setTextSize(19);
            espaco4.setGravity(Gravity.CENTER);
            layout.addView(espaco4);
            final TextView espaco2 = new TextView(root.getContext());
            espaco2.setText("\n");
            espaco2.setTextSize(25);
            espaco2.setGravity(Gravity.CENTER);
            layout.addView(espaco2);
            builder.setView(layout);
            builder.setPositiveButton(getString(R.string.ok), (dialog, which) -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://paypal.me/pedrocruz77"));
                startActivity(browserIntent);
            });
            builder.setNeutralButton(getString(R.string.cancelar), (dialog, which) -> dialog.dismiss());
            builder.show();
        });
        btn_ficheiroReadable.setOnClickListener(v -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(root.getContext());
            LinearLayout layout = new LinearLayout(root.getContext());
            layout.setOrientation(LinearLayout.VERTICAL);
            builder.setIcon(root.getContext().getDrawable(R.drawable.image));
            builder.setTitle(getString(R.string.copiadeseguranca));
            layout.setGravity(Gravity.CENTER);
            final TextView espaco4 = new TextView(root.getContext());
            espaco4.setText(getString(R.string.pretendesfazercopiadeseguranca));
            espaco4.setTextSize(19);
            espaco4.setGravity(Gravity.CENTER);
            layout.addView(espaco4);
            final TextView espaco2 = new TextView(root.getContext());
            espaco2.setText(getString(R.string.oficheirovaidarparalernormalmente));
            espaco2.setTextSize(25);
            espaco2.setGravity(Gravity.CENTER);
            layout.addView(espaco2);
            builder.setView(layout);
            builder.setPositiveButton(getString(R.string.ok), (dialog, which) -> BackupSonhosReadable());
            builder.setNeutralButton(getString(R.string.cancelar), (dialog, which) -> dialog.dismiss());
            builder.show();
        });
        btn_restoreFromFile.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("text/plain");
            startActivityForResult(intent, PICK_FILE);
        });
        btn_backupFicheiro.setOnClickListener(v -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(root.getContext());
            LinearLayout layout = new LinearLayout(root.getContext());
            layout.setOrientation(LinearLayout.VERTICAL);
            builder.setIcon(root.getContext().getDrawable(R.drawable.image));
            builder.setTitle(getString(R.string.copiadeseguranca));
            layout.setGravity(Gravity.CENTER);
            final TextView espaco4 = new TextView(root.getContext());
            espaco4.setText(getString(R.string.pretendesfazercopiadeseguranca));
            espaco4.setTextSize(19);
            espaco4.setGravity(Gravity.CENTER);
            layout.addView(espaco4);
            final TextView espaco2 = new TextView(root.getContext());
            espaco2.setText(getString(R.string.oficheirovaiserguardadonumaformaespecifica));
            espaco2.setTextSize(25);
            espaco2.setGravity(Gravity.CENTER);
            layout.addView(espaco2);
            builder.setView(layout);
            builder.setPositiveButton(getString(R.string.ok), (dialog, which) -> BackupSonhos());
            builder.setNeutralButton(getString(R.string.cancelar), (dialog, which) -> dialog.dismiss());
            builder.show();
        });
        return root;
    }

    private void getPermissions() {
        if ( root.getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    private static final int WRITE_REQUEST_CODE = 101;
    private static final int WRITE_REQUEST_CODE_Readable = 102;
    private void BackupSonhos() {
        getPermissions();
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, "MoonDreams_BackupSonhos.txt");
        startActivityForResult(intent, WRITE_REQUEST_CODE);
    }
    private void BackupSonhosReadable() {
        getPermissions();
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, "MoonDreams_Sonhos.txt");
        startActivityForResult(intent, WRITE_REQUEST_CODE_Readable);
    }

    private String readTextFile(Uri uri){
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(applicationContext.getContentResolver().openInputStream(uri)));
            String line = "";

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        }else if (requestCode == WRITE_REQUEST_CODE_Readable) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    if (data != null
                            && data.getData() != null) {
                        writeInFileReadable(data.getData());
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    break;
            }
        }else if(requestCode == PICK_FILE){
            Uri uri = data.getData();
            String fileContent = readTextFile(uri);
            if(fileContent.contains("§")){
                if(fileContent.contains("»")){
                    try {
                        SharedPreferences prefs1 = root.getContext().getSharedPreferences("MoonDreams", MODE_PRIVATE);
                        String SonhosFull = prefs1.getString("MoonDreams", "");
                            SharedPreferences prefs = root.getContext().getSharedPreferences("MoonDreams", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("MoonDreams", SonhosFull + "§" + fileContent);
                            editor.apply();
                    }catch (Exception e){
                        Toast.makeText(BackupFragment.this.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(BackupFragment.this.getContext(), getString(R.string.ficheiroinvalido), Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(BackupFragment.this.getContext(), getString(R.string.ficheiroinvalido), Toast.LENGTH_LONG).show();
            }
        }
    }

    private Context applicationContext = MainActivity.getContextOfApplication();
    private void writeInFile(@NonNull Uri uri) {
        OutputStream outputStream;

        try {
            SharedPreferences prefs = applicationContext.getSharedPreferences("MoonDreams", Context.MODE_PRIVATE);
            String SonhosFull = prefs.getString("MoonDreams", "");
            if(!SonhosFull.isEmpty()){
                outputStream = applicationContext.getContentResolver().openOutputStream(uri);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
                bw.write(SonhosFull);
                bw.flush();
                bw.close();
                Toast.makeText(BackupFragment.this.getContext(), getString(R.string.copiadesegurancacompleta), Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeInFileReadable(@NonNull Uri uri) {
        OutputStream outputStream;
        try {
            SharedPreferences prefs = applicationContext.getSharedPreferences("MoonDreams", Context.MODE_PRIVATE);
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
                outputStream = applicationContext.getContentResolver().openOutputStream(uri);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
                bw.write(filetoSaveString);
                bw.flush();
                bw.close();
                Toast.makeText(BackupFragment.this.getContext(), getString(R.string.copiadesegurancacompleta), Toast.LENGTH_LONG).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
