package mos.edu.client.movieasker.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mos.edu.client.movieasker.R;
import mos.edu.client.movieasker.dto.Film;

public class FilmListAdapter extends RecyclerView.Adapter<FilmListAdapter.FilmViewHolder> {
    private static final int ITEM_LAYOUT = R.layout.film_item;

    private List<Film> films;

    public FilmListAdapter(List<Film> films) {
        this.films = films;
    }

    @Override
    public FilmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(ITEM_LAYOUT, parent, false);
        return new FilmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FilmViewHolder holder, int position) {
        Film film = films.get(position);

        holder.alternativeNameTextView.setText(film.getAlternativeName());
        holder.yearTextView.setText(String.valueOf(film.getYear()));
    }

    @Override
    public int getItemCount() {
        return films.size();
    }

    public static class FilmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cardView;
        ImageView posterImageView;
        TextView alternativeNameTextView;
        TextView yearTextView;
        ImageView favoriteImageView;
        ImageView lookedImageView;
        TextView ratingTextView;

        public FilmViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_view_item);
            posterImageView = (ImageView) itemView.findViewById(R.id.poster_item);
            alternativeNameTextView = (TextView) itemView.findViewById(R.id.alternative_name_item);
            yearTextView = (TextView) itemView.findViewById(R.id.year_item);
            favoriteImageView = (ImageView) itemView.findViewById(R.id.favorite_item);
            favoriteImageView.setOnClickListener(this);
            lookedImageView = (ImageView) itemView.findViewById(R.id.looked_item);
            lookedImageView.setOnClickListener(this);
            ratingTextView = (TextView) itemView.findViewById(R.id.rating_item);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.favorite_item:
                    favoriteImageView.setImageResource(R.mipmap.ic_heart_on);
                    break;

                case R.id.looked_item:
                    lookedImageView.setImageResource(R.mipmap.ic_eye_on);
                    break;
            }
        }

    }

}
