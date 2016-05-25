package mos.edu.client.movieasker.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

import mos.edu.client.movieasker.R;
import mos.edu.client.movieasker.app.Constants;
import mos.edu.client.movieasker.dto.ShortFilmDTO;
import mos.edu.client.movieasker.holder.FilmViewHolder;
import mos.edu.client.movieasker.listener.OnItemClickListener;

public class FilmListAdapter extends RecyclerView.Adapter<FilmViewHolder> {
    private static final int ITEM_LAYOUT = R.layout.item_film;

    public static final DisplayImageOptions IMAGE_OPTIONS = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .showImageForEmptyUri(R.drawable.no_poster)
            .showImageOnFail(R.drawable.no_poster)
            .showImageOnLoading(R.drawable.no_poster)
            .displayer(new RoundedBitmapDisplayer(25))
            .build();

    private final List<ShortFilmDTO> films = new ArrayList<>();

    private OnItemClickListener itemClickListener = null;

    @Override
    public FilmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(ITEM_LAYOUT, parent, false);
        return new FilmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FilmViewHolder holder, int position) {
        final ShortFilmDTO film = films.get(position);

        holder.setOnItemClickListener(itemClickListener);
        ImageLoader.getInstance().displayImage(
                Constants.URI.POSTERS + film.getPosterUrl(),
                holder.posterImageView,
                IMAGE_OPTIONS
        );
        holder.alternativeNameTextView.setText(film.getAlternativeName());
        holder.yearTextView.setText(String.valueOf(film.getYear()));
    }

    @Override
    public int getItemCount() {
        return films.size();
    }

    public void addFilms(List<ShortFilmDTO> films) {
        this.films.addAll(films);
        super.notifyDataSetChanged();
    }

    public ShortFilmDTO getFilm(int position) {
        return films.get(position);
    }

    public boolean isEmpty() {
        return films.isEmpty();
    }

    public void clearFilms() {
        films.clear();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

}
