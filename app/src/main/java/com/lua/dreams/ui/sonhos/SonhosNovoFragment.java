package com.lua.dreams.ui.sonhos;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.lua.dreams.MainActivity;
import com.lua.dreams.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;


public class SonhosNovoFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private View root;
    public MaterialButton dialog_bt_date, btn_gravar_Sonho;
    private DatePickerDialog datePickerDialog ;
    private int Year, Month, Day ;
    public TextInputEditText tituloSonho, oSonho;

    public SonhosNovoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_sonhos_novo, container, false);
        Calendar calendar = Calendar.getInstance();

        Year = calendar.get(Calendar.YEAR) ;
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

        dialog_bt_date = root.findViewById(R.id.dialog_bt_date);
        btn_gravar_Sonho = root.findViewById(R.id.btn_gravar_sonho);
        dialog_bt_date.setOnClickListener(view -> {

            datePickerDialog = DatePickerDialog.newInstance(SonhosNovoFragment.this, Year, Month, Day);

            datePickerDialog.setThemeDark(true );

            datePickerDialog.showYearPickerFirst(false);

            datePickerDialog.setAccentColor(Color.parseColor("#FF5722"));

            datePickerDialog.setTitle("Escolhe a data");

            datePickerDialog.show(getFragmentManager(), "DatePickerDialog");

        });
        tituloSonho = root.findViewById(R.id.titulo_sonho_novo_novo);
        oSonho = root.findViewById(R.id.texto_sonho_novo_novo);
        btn_gravar_Sonho.setOnClickListener(v -> {
            if(!dialog_bt_date.getText().equals("ESCOLHER A DATA")){
                if(!tituloSonho.getText().toString().equals("")){
                    if(!oSonho.getText().toString().equals("")){
                        SharedPreferences prefs = root.getContext().getSharedPreferences("MoonDreams", MODE_PRIVATE);
                        String SonhosFull = prefs.getString("MoonDreams", "");
                        SharedPreferences.Editor editor = root.getContext().getSharedPreferences("MoonDreams", MODE_PRIVATE).edit();
                        editor.putString("MoonDreams", SonhosFull + "§" + dialog_bt_date.getText().toString() + "»" + tituloSonho.getText().toString() + "»" + oSonho.getText().toString());
                        editor.apply();
                        Toast.makeText(root.getContext(), "Sonho gravado com sucesso!", Toast.LENGTH_LONG).show();
                        tituloSonho.setText("");
                        oSonho.setText("");
                        dialog_bt_date.setText("ESCOLHER A DATA");
                    }else {
                        Toast.makeText(SonhosNovoFragment.this.getContext(), "Escreve o sonho", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(SonhosNovoFragment.this.getContext(), "Escreve o titulo do sonho", Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(SonhosNovoFragment.this.getContext(), "Escolhe a data do sonho!", Toast.LENGTH_LONG).show();
            }
        });

        return root;

    }

    @Override
    public void onDateSet(DatePickerDialog view, int Year, int Month, int Day) {
        dialog_bt_date.setText(Day + "-" + (Month + 1) + "-" + Year);
    }


}
