package com.lua.dreams.ui.sonhos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;

import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lua.dreams.MainActivity;
import com.lua.dreams.R;
import com.lua.dreams.ferramentas.EmptyRecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class SonhosAdapter extends EmptyRecyclerView.Adapter<SonhosAdapter.SonhosViewHolder> implements Filterable {
    private ArrayList<SonhosObject> sonhosList, sonhosListFull;
    private Context context;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    sonhosList = sonhosListFull;
                } else {
                    ArrayList<SonhosObject> filteredList = new ArrayList<>();
                    for (SonhosObject row : sonhosListFull) {

                        if (row.getData().toLowerCase().contains(charString.toLowerCase()) || row.getTitulo().contains(charSequence) || row.getSonho().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    sonhosList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = sonhosList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                sonhosList = (ArrayList<SonhosObject>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public SonhosAdapter(ArrayList<SonhosObject> sonhosList) {
        this.sonhosList = sonhosList;
        this.sonhosListFull = sonhosList;
    }

    @NonNull
    @Override
    public SonhosAdapter.SonhosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sonhos_item, viewGroup, false);
        EmptyRecyclerView.LayoutParams lp = new EmptyRecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        context = viewGroup.getContext();
        return new SonhosAdapter.SonhosViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SonhosAdapter.SonhosViewHolder SonhosViewHolder, int i) {
        SonhosViewHolder.mData.setText(this.sonhosList.get(i).data);
        SonhosViewHolder.mTitulo.setText(this.sonhosList.get(i).titulo);

        SonhosViewHolder.mlayout.setOnClickListener(v -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);

            builder.setIcon(context.getDrawable(R.drawable.image));
            builder.setTitle("Sonho de " + sonhosList.get(i).getData());
            layout.setGravity(Gravity.CENTER);
            final TextView espaco4 = new TextView(context);
            espaco4.setText("\n" + sonhosList.get(i).titulo + "\n\n");
            espaco4.setTextSize(16);
            espaco4.setGravity(Gravity.CENTER);
            layout.addView(espaco4);
            final TextView espaco2 = new TextView(context);
            espaco2.setText(sonhosList.get(i).sonho);
            espaco2.setTextSize(20);
            espaco2.setGravity(Gravity.CENTER);
            layout.addView(espaco2);
            builder.setView(layout);
            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
            builder.setNeutralButton("Editar", (dialog, which) -> {
                MaterialAlertDialogBuilder builder1 = new MaterialAlertDialogBuilder(context);
                LinearLayout layout1 = new LinearLayout(context);
                layout1.setOrientation(LinearLayout.VERTICAL);

                builder1.setIcon(context.getDrawable(R.drawable.image));
                builder1.setTitle("Editar sonho de " + sonhosList.get(i).getData());
                layout1.setGravity(Gravity.CENTER);
                final EditText espaco41 = new EditText(context);
                espaco41.setText(sonhosList.get(i).titulo);
                espaco41.setTextSize(16);
                espaco41.setGravity(Gravity.CENTER);
                layout1.addView(espaco41);
                final TextView espaco3 = new TextView(context);
                espaco3.setText("\n\n");
                layout1.addView(espaco3);
                final EditText espaco21 = new EditText(context);
                espaco21.setText(sonhosList.get(i).sonho);
                espaco21.setTextSize(20);
                espaco21.setGravity(Gravity.CENTER);
                layout1.addView(espaco21);
                builder1.setView(layout1);
                builder1.setPositiveButton("Gravar", (dialog1, which1) -> {
                    sonhosList.get(i).setTitulo(espaco41.getText().toString());
                    sonhosList.get(i).setSonho(espaco21.getText().toString());
                    String NewSonhosSave = "";
                    for(int j = 0 ; j < sonhosList.size(); j++){
                        NewSonhosSave = NewSonhosSave + "§" + sonhosList.get(j).data + "»" + sonhosList.get(j).titulo + "»" + sonhosList.get(j).sonho;
                    }
                    SharedPreferences.Editor editor = context.getSharedPreferences("MoonDreams", MODE_PRIVATE).edit();
                    editor.putString("MoonDreams", NewSonhosSave);
                    editor.apply();
                    notifyDataSetChanged();
                    dialog1.dismiss();
                });
                builder1.setNeutralButton("Apagar", (dialog12, which12) -> {
                    sonhosList.remove(i);
                    String NewSonhosSave = "";
                    for(int j = 0 ; j < sonhosList.size(); j++){
                        NewSonhosSave = NewSonhosSave + "§" + sonhosList.get(j).data + "»" + sonhosList.get(j).titulo + "»" + sonhosList.get(j).sonho;
                    }
                    SharedPreferences.Editor editor = context.getSharedPreferences("MoonDreams", MODE_PRIVATE).edit();
                    editor.putString("MoonDreams", NewSonhosSave);
                    editor.apply();
                    notifyDataSetChanged();
                    dialog12.dismiss();
                });
                builder1.show();
            });
            builder.show();
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return sonhosList == null ? 0 : sonhosList.size();
    }

    class SonhosViewHolder extends EmptyRecyclerView.ViewHolder {
        final TextView mData;
        final TextView mTitulo;
        final LinearLayout mlayout;

        SonhosViewHolder(View view) {
            super(view);
            mData = view.findViewById(R.id.data_sonho);
            mTitulo = view.findViewById(R.id.titulo_sonho);
            mlayout = view.findViewById(R.id.layout_sonhos_item);
        }
    }
}
