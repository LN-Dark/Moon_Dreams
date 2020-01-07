package com.lua.dreams.ui.sonhos;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.lua.dreams.MainActivity;
import com.lua.dreams.R;
import com.lua.dreams.ferramentas.EmptyRecyclerView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class SonhosFragment extends Fragment {
    private View root;
    private ArrayList<SonhosObject> Sonhos;
    private SonhosAdapter mSonhosAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_sonhos, null);

        ExtendedFloatingActionButton floatingActionButton = root.findViewById(R.id.float_addSonhos);
        floatingActionButton.setOnClickListener(v -> ((MainActivity)getActivity()).loadFragment(new SonhosNovoFragment()));
        SharedPreferences prefs = root.getContext().getSharedPreferences("MoonDreams", MODE_PRIVATE);
        String SonhosFull = prefs.getString("MoonDreams", "");
        String[] SonhosDivided = SonhosFull.split("§");

        Sonhos = new ArrayList<>();
        for(int i = 0 ; i < SonhosDivided.length; i++){
            SonhosObject sonhosNew = new SonhosObject();
            String[] Sonho = SonhosDivided[i].split("»");
            if(Sonho[0].length() > 2){
                sonhosNew.id = String.valueOf(i);
                sonhosNew.data = Sonho[0];
                sonhosNew.titulo = Sonho[1];
                sonhosNew.sonho = Sonho[2];
                Sonhos.add(sonhosNew);
            }
        }

        InitializeRecyclerView();
        mSonhosAdapter.notifyDataSetChanged();
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        menu.findItem(R.id.action_search).setVisible(true);
        searchView.setImeOptions(searchView.getImeOptions() | EditorInfo.IME_ACTION_SEARCH | EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_FLAG_NO_FULLSCREEN);
        searchView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mSonhosAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void InitializeRecyclerView() {
            EmptyRecyclerView mSonhosList = root.findViewById(R.id.recyclerView_Sonhos);
            mSonhosList.setNestedScrollingEnabled(false);
            mSonhosList.setHasFixedSize(false);
            Display display = this.getActivity().getWindowManager().getDefaultDisplay();
            DisplayMetrics outMetrics = new DisplayMetrics();
            display.getMetrics(outMetrics);
            LinearLayoutManager lLayout;
            lLayout = new LinearLayoutManager(root.getContext(), EmptyRecyclerView.VERTICAL, false);
            mSonhosList.setLayoutManager(lLayout);
            mSonhosAdapter = new SonhosAdapter(Sonhos);
            mSonhosList.setAdapter(mSonhosAdapter);

    }
}