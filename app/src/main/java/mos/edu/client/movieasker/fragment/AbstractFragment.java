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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import mos.edu.client.movieasker.R;
import mos.edu.client.movieasker.activity.FilmActivity;
import mos.edu.client.movieasker.activity.dialog.DialogManager;
import mos.edu.client.movieasker.adapter.FilmListAdapter;
import mos.edu.client.movieasker.app.Constants;
import mos.edu.client.movieasker.app.ThisApplication;
import mos.edu.client.movieasker.dto.ShortFilmDTO;

public abstract class AbstractFragment extends Fragment implements FilmListAdapter.OnItemClickListener {
    protected static final int FRAGMENT_LAYOUT = R.layout.fragment_content;

    private final String title;
    protected final FilmListAdapter adapter;

    protected TextView contentMessageTextView;
    private Button repeatLoadingButton;
    private ProgressBar loadingContentProgressBar;

    protected LoadContentTask loadContentTask = null;

    protected AbstractFragment(String title) {
        this.title = title;
        this.adapter = new FilmListAdapter();
    }

    public String getTitle() {
        return title;
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

    private final OnEndlessScrollListener endlessScrollListener = new OnEndlessScrollListener() {
        @Override
        public boolean onLoadMore(int page, int size) {
            return contentLoad(page, size);
        }
    };

    protected abstract boolean contentLoad(int page, int size);

    protected abstract void setMessageContent();

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

    private void showMessageContent() {
        if (!adapter.isEmpty()) {
            contentMessageTextView.setVisibility(View.GONE);
        }
        else {
            contentMessageTextView.setVisibility(View.VISIBLE);
        }
    }

    private void showBadInternetConnectionDialog() {
        DialogManager.showDialog(this.getContext(), DialogManager.BAD_INTERNET_CONNECTION);
    }

    private void showFilmDescription(int filmId) {
        final Intent filmActivity = new Intent(getContext(), FilmActivity.class);
        filmActivity.putExtra(FilmActivity.FILM_ID_PARAM, filmId);
        startActivity(filmActivity);
    }

    protected static abstract class OnEndlessScrollListener extends RecyclerView.OnScrollListener {

        private int currentPage = 0;
        private boolean loading = false;
        private int prevTotalItemCount = 0;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (dy > 0) {
                final LinearLayoutManager layoutManager =
                        (LinearLayoutManager) recyclerView.getLayoutManager();

                final int totalItemCount = layoutManager.getItemCount();

                if (loading && (totalItemCount > prevTotalItemCount)) {
                    prevTotalItemCount = totalItemCount;
                    loading = false;
                }

                final int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                final int visibleItemCount = layoutManager.getChildCount();

                if (!loading && (firstVisibleItemPosition + visibleItemCount >= totalItemCount)) {
                    currentPage++;
                    loading = onLoadMore(currentPage, Constants.ELEMENTS_ON_PAGE);
                    if (!loading) {
                        currentPage--;
                    }
                }
            }
        }

        public abstract boolean onLoadMore(int page, int size);
    }

    protected static class LoadContentTask extends AsyncTask<String, Void, Collection<ShortFilmDTO>> {

        private AbstractFragment fragment;
        private final String contentUri;

        public LoadContentTask(AbstractFragment fragment, String contentUri) {
            this.fragment = fragment;
            this.contentUri = contentUri;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fragment.repeatLoadingButton.setVisibility(View.GONE);
            fragment.loadingContentProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Collection<ShortFilmDTO> doInBackground(String... params) {
            final RestTemplate template = ThisApplication.getInstance().getRestTemplate();

            final ResponseEntity<ShortFilmDTO> response;
            try {
                response = template.getForEntity(contentUri, ShortFilmDTO.class, (Object[]) params);
                if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                    return Collections.emptyList();
                }
            } catch (RestClientException e) {
                return null;
            }

            return response.getBody().getContent();
        }

        @Override
        protected void onPostExecute(Collection<ShortFilmDTO> films) {
            fragment.loadingContentProgressBar.setVisibility(View.GONE);

            if (films == null) {
                fragment.showBadInternetConnectionDialog();
                fragment.repeatLoadingButton.setVisibility(View.VISIBLE);
            } else if (!films.isEmpty()) {
                fragment.adapter.addFilms(new ArrayList<>(films));
                fragment.showMessageContent();
            }

            fragment = null;
            super.onPostExecute(films);
        }

    }

}
