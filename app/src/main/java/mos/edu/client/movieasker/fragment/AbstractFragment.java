package mos.edu.client.movieasker.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mos.edu.client.movieasker.R;
import mos.edu.client.movieasker.ThisApplication;
import mos.edu.client.movieasker.adapter.FilmListAdapter;
import mos.edu.client.movieasker.dto.FilmDTO;

public abstract class AbstractFragment extends Fragment {
    private static final int FRAGMENT_LAYOUT = R.layout.fragment_content;

    private String title;

    protected FilmListAdapter adapter;

    protected TextView contentEmptyTextView;

    public AbstractFragment() {
        adapter = new FilmListAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(FRAGMENT_LAYOUT, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(ThisApplication.getInstance());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(endlessScrollListener);

        contentEmptyTextView = (TextView) view.findViewById(R.id.empty_content_text_view);

        showMessageContentEmpty();

        return view;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private OnEndlessScrollListener endlessScrollListener =
            new OnEndlessScrollListener() {

                private int index = 0;
                private int index2 = 3;

                @Override
                public boolean onLoadMore(int page, int size) {
                    List<FilmDTO> films = adapter.getFilms();

                    Log.i("Rest", "onLoadMore! List Size: " + films.size());

                    films.addAll(NewFragment.getInstance().createMockFilmList().subList(index, index2));

                    index = index2;
                    index2 = index2 + 3;

                    int listSize = NewFragment.getInstance().createMockFilmList().size();
                    if (index2 > listSize) {
                        index = 0;
                        index2 = 3;
                    }

                    adapter.setFilms(films);
                    return true;
                }

            };

    private void showMessageContentEmpty() {
        if (adapter.getItemCount() > 0) {
            contentEmptyTextView.setVisibility(View.GONE);
        }
        else {
            contentEmptyTextView.setVisibility(View.VISIBLE);
        }
    }

    private abstract class OnEndlessScrollListener extends RecyclerView.OnScrollListener {

        private int currentPage = 0;
        private boolean loading = false;
        private int prevTotalItemCount = 0;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (dy > 0) {
                LinearLayoutManager layoutManager =
                        (LinearLayoutManager) recyclerView.getLayoutManager();

                int totalItemCount = layoutManager.getItemCount();

                if (loading && (totalItemCount > prevTotalItemCount)) {
                    prevTotalItemCount = totalItemCount;
                    loading = false;
                }

                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                int visibleItemCount = layoutManager.getChildCount();

                if (!loading && (firstVisibleItemPosition + visibleItemCount >= totalItemCount)) {
                    currentPage++;
                    loading = onLoadMore(currentPage, totalItemCount);
                    if (!loading) {
                        currentPage--;
                    }
                }
            }
        }

        public abstract boolean onLoadMore(int page, int size);

    }

}
