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

        final String host = "http://192.168.0.109:8080/";
        final String posterPath = "posters/";
        final String posterExt = ".jpg";

        List<Film> films = new ArrayList<>();
        films.add(new Film(1, host + posterPath + "1" + posterExt, "Властелин колец: Братство кольца", 2001));
        films.add(new Film(2, host + posterPath + "2" + posterExt, "Властелин колец: Две башни", 2002));
        films.add(new Film(3, host + posterPath + "3" + posterExt, "Властелин колец: Возвращение короля", 2003));
        films.add(new Film(4, host + posterPath + "4" + posterExt, "Хоббит: Нежданное путешествие", 2012));
        films.add(new Film(5, host + posterPath + "5" + posterExt, "Хоббит: Пустошь Смауга", 2013));
        films.add(new Film(6, host + posterPath + "6" + posterExt, "Хоббит: Битва пяти воинств", 2014));
        films.add(new Film(7, host + posterPath + "7" + posterExt, "Гарри Поттер и Философский камень", 2001));
        films.add(new Film(8, host + posterPath + "8" + posterExt, "Гарри Поттер и Тайная комната", 2002));
        films.add(new Film(9, host + posterPath + "9" + posterExt, "Гарри Поттер и Узник Азкабана", 2004));
        films.add(new Film(10, host + posterPath + "10" + posterExt, "Гарри Поттер и Кубок огня", 2005));
        films.add(new Film(11, host + posterPath + "11" + posterExt, "Гарри Поттер и Орден Феникса", 2007));
        films.add(new Film(12, host + posterPath + "12" + posterExt, "Гарри Поттер и Принц-полукровка", 2009));
        films.add(new Film(13, host + posterPath + "13" + posterExt, "Гарри Поттер и Дары смерти: Часть 1", 2010));
        films.add(new Film(14, host + posterPath + "14" + posterExt, "Гарри Поттер и Дары смерти: Часть 2", 2011));
        films.add(new Film(15, host + posterPath + "15" + posterExt, "Одиннадцать друзей Оушена", 2002));
        return films;
    }

}
