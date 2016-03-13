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

    private static NewFragment instance;

    public static NewFragment getInstance(Context context) {
        if (instance == null) {
            Bundle bundle = new Bundle();
            instance = new NewFragment();
            instance.setArguments(bundle);
            instance.context = context;
            instance.setTitle(context.getString(R.string.new_item_title));
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

        FilmListAdapter adapter = new FilmListAdapter(createMockFilmList());
        recyclerView.setAdapter(adapter);

        return view;
    }

    private List<Film> createMockFilmList() {
        List<Film> films = new ArrayList<>();
        films.add(new Film(1, "Властелин колец: Братство кольца", 2001));
        films.add(new Film(2, "Властелин колец: Две башни", 2002));
        films.add(new Film(3, "Властелин колец: Возвращение короля", 2003));
        films.add(new Film(4, "Хоббит: Нежданное путешествие", 2012));
        films.add(new Film(5, "Хоббит: Пустошь Смауга", 2013));
        films.add(new Film(6, "Хоббит: Битва пяти воинств", 2014));
        films.add(new Film(7, "Гарри Поттер и Философский камень", 2001));
        films.add(new Film(8, "Гарри Поттер и Тайная комната", 2002));
        films.add(new Film(9, "Гарри Поттер и Узник Азкабана", 2004));
        films.add(new Film(10, "Гарри Поттер и Кубок огня", 2005));
        films.add(new Film(11, "Гарри Поттер и Орден Феникса", 2007));
        films.add(new Film(12, "Гарри Поттер и Принц-полукровка", 2009));
        films.add(new Film(13, "Гарри Поттер и Дары смерти: Часть 1", 2010));
        films.add(new Film(14, "Гарри Поттер и Дары смерти: Часть 2", 2011));
        films.add(new Film(15, "Одиннадцать друзей Оушена", 2002));
        return films;
    }

}
