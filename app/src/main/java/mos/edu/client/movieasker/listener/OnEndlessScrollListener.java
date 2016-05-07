package mos.edu.client.movieasker.listener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import mos.edu.client.movieasker.app.Constants;

public abstract class OnEndlessScrollListener extends RecyclerView.OnScrollListener {

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