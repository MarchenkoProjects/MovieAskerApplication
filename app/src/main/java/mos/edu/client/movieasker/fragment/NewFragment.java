package mos.edu.client.movieasker.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mos.edu.client.movieasker.Constants;
import mos.edu.client.movieasker.R;
import mos.edu.client.movieasker.ThisApplication;
import mos.edu.client.movieasker.adapter.FilmListAdapter;
import mos.edu.client.movieasker.dto.Film;

public class NewFragment extends AbstractFragment {
    private static final int FRAGMENT_LAYOUT = R.layout.fragment_new;

    private static NewFragment instance = new NewFragment();

    public static NewFragment getInstance() {
        return instance;
    }

    public NewFragment() {
        setTitle(ThisApplication.getInstance().getString(R.string.new_item_title));
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

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(ThisApplication.getInstance());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        FilmListAdapter adapter = new FilmListAdapter(createMockFilmList());
        recyclerView.setAdapter(adapter);

        return view;
    }

    private List<Film> createMockFilmList() {

        final String posterExt = ".jpg";

        List<Film> films = new ArrayList<>();
        films.add(new Film(1, Constants.URI.POSTERS_URI + "1" + posterExt, "Властелин колец: Братство кольца", 2001));
        films.add(new Film(2, Constants.URI.POSTERS_URI + "2" + posterExt, "Властелин колец: Две башни", 2002));
        films.add(new Film(3, Constants.URI.POSTERS_URI + "3" + posterExt, "Властелин колец: Возвращение короля", 2003));
        films.add(new Film(4, Constants.URI.POSTERS_URI + "4" + posterExt, "Хоббит: Нежданное путешествие", 2012));
        films.add(new Film(5, Constants.URI.POSTERS_URI + "5" + posterExt, "Хоббит: Пустошь Смауга", 2013));
        films.add(new Film(6, Constants.URI.POSTERS_URI + "6" + posterExt, "Хоббит: Битва пяти воинств", 2014));
        films.add(new Film(7, Constants.URI.POSTERS_URI + "7" + posterExt, "Гарри Поттер и Философский камень", 2001));
        films.add(new Film(8, Constants.URI.POSTERS_URI + "8" + posterExt, "Гарри Поттер и Тайная комната", 2002));
        films.add(new Film(9, Constants.URI.POSTERS_URI + "9" + posterExt, "Гарри Поттер и Узник Азкабана", 2004));
        films.add(new Film(10, Constants.URI.POSTERS_URI + "10" + posterExt, "Гарри Поттер и Кубок огня", 2005));
        films.add(new Film(11, Constants.URI.POSTERS_URI + "11" + posterExt, "Гарри Поттер и Орден Феникса", 2007));
        films.add(new Film(12, Constants.URI.POSTERS_URI + "12" + posterExt, "Гарри Поттер и Принц-полукровка", 2009));
        films.add(new Film(13, Constants.URI.POSTERS_URI + "13" + posterExt, "Гарри Поттер и Дары смерти: Часть 1", 2010));
        films.add(new Film(14, Constants.URI.POSTERS_URI + "14" + posterExt, "Гарри Поттер и Дары смерти: Часть 2", 2011));
        films.add(new Film(15, Constants.URI.POSTERS_URI + "15" + posterExt, "Одиннадцать друзей Оушена", 2002));
        return films;
    }

}
