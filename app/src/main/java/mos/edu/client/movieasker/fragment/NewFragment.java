package mos.edu.client.movieasker.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mos.edu.client.movieasker.R;
import mos.edu.client.movieasker.adapter.FilmListAdapter;
import mos.edu.client.movieasker.dto.Film;

public class NewFragment extends AbstractFragment {
    private static final int FRAGMENT_LAYOUT = R.layout.fragment_new;

    private FilmListAdapter adapter;

    private static NewFragment instance;

    public static NewFragment getInstance(Context context) {
        if (instance == null) {
            Bundle bundle = new Bundle();
            instance = new NewFragment();
            instance.setArguments(bundle);
            instance.context = context;
            instance.setTitle(context.getString(R.string.tab_new));
        }
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(FRAGMENT_LAYOUT, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        adapter = new FilmListAdapter(createMockFilmList());
        recyclerView.setAdapter(adapter);

        return view;
    }

    private List<Film> createMockFilmList() {
        List<Film> films = new ArrayList<>();
        films.add(new Film("Властелин колец: Братство кольца", 2001));
        films.add(new Film("Властелин колец: Две башни", 2002));
        films.add(new Film("Властелин колец: Возвращение короля", 2003));
        films.add(new Film("Гарри Поттер и Философский камень", 2002));
        films.add(new Film("Гарри Поттер и Тайная комната", 2004));
        films.add(new Film("Гарри Поттер и Узник Азкабана", 2005));
        return films;
    }

}
