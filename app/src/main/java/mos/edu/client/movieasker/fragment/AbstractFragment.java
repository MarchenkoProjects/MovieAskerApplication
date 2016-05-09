package mos.edu.client.movieasker.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import mos.edu.client.movieasker.R;
import mos.edu.client.movieasker.activity.FilmActivity;
import mos.edu.client.movieasker.adapter.FilmListAdapter;
import mos.edu.client.movieasker.app.Constants;
import mos.edu.client.movieasker.dto.ShortFilmDTO;
import mos.edu.client.movieasker.listener.OnEndlessScrollListener;
import mos.edu.client.movieasker.listener.OnItemClickListener;
import mos.edu.client.movieasker.task.LoadContentTask;

public abstract class AbstractFragment extends Fragment implements OnItemClickListener {
    protected static final int FRAGMENT_LAYOUT = R.layout.fragment_content;

    private final String title;
    protected final FilmListAdapter adapter;

    protected TextView contentMessageTextView;
    private Button repeatLoadingButton;
    private ProgressBar loadingContentProgressBar;

    protected LoadContentTask loadContentTask;

    protected AbstractFragment(String title) {
        this.title = title;
        this.adapter = new FilmListAdapter();
        this.loadContentTask = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(FRAGMENT_LAYOUT, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(endlessScrollListener);

        contentMessageTextView = (TextView) view.findViewById(R.id.empty_content_text_view);
        setMessageContent();

        repeatLoadingButton = (Button) view.findViewById(R.id.repeat_loading_content_button);
        repeatLoadingButton.setOnClickListener(repeatLoadingContentClick);

        loadingContentProgressBar = (ProgressBar) view.findViewById(R.id.loading_content_progressbar);

        firstLoadContent();
        showMessageContent();

        return view;
    }

    @Override
    public void onItemClick(View view, int position) {
        final ShortFilmDTO film = adapter.getFilm(position);
        showFilmDescription(film.getIdFilm());
    }

    public String getTitle() {
        return title;
    }

    public FilmListAdapter getAdapter() {
        return adapter;
    }

    public void updateAdapter() {
        adapter.clearFilms();
        firstLoadContent();
    }

    public void showLoadingProgressBar(boolean isShow) {
        if (isShow) {
            loadingContentProgressBar.setVisibility(View.VISIBLE);
        } else {
            loadingContentProgressBar.setVisibility(View.GONE);
        }
    }

    public void showRepeatLoadingButton(boolean isShow) {
        if (isShow) {
            repeatLoadingButton.setVisibility(View.VISIBLE);
        } else {
            repeatLoadingButton.setVisibility(View.GONE);
        }
    }

    public void showMessageContent() {
        if (!adapter.isEmpty()) {
            contentMessageTextView.setVisibility(View.GONE);
        } else {
            contentMessageTextView.setVisibility(View.VISIBLE);
        }
    }

    protected abstract boolean contentLoad(int page, int size);

    protected abstract void setMessageContent();

    private final OnEndlessScrollListener endlessScrollListener = new OnEndlessScrollListener() {
        @Override
        public boolean onLoadMore(int page, int size) {
            return contentLoad(page, size);
        }
    };

    private final View.OnClickListener repeatLoadingContentClick =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    firstLoadContent();
                }
            };

    private void firstLoadContent() {
        if (loadContentTask == null) {
            if (adapter.isEmpty()) {
                contentLoad(Constants.FIRST_PAGE_NUMBER, Constants.ELEMENTS_ON_PAGE);
            }
        }
        else {
            if (loadContentTask.getStatus() == AsyncTask.Status.RUNNING) {
                loadingContentProgressBar.setVisibility(View.VISIBLE);
            }
            else {
                loadContentTask = null;
                firstLoadContent();
            }
        }
    }

    private void showFilmDescription(int filmId) {
        final Intent filmActivity = new Intent(getContext(), FilmActivity.class);
        filmActivity.putExtra(FilmActivity.FILM_ID_PARAM, filmId);
        startActivity(filmActivity);
    }

}
