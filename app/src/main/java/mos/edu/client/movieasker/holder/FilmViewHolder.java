package mos.edu.client.movieasker.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import mos.edu.client.movieasker.R;
import mos.edu.client.movieasker.listener.OnItemClickListener;

public class FilmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView posterImageView;
    public TextView alternativeNameTextView;
    public TextView yearTextView;

    private OnItemClickListener itemClickListener = null;

    public FilmViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        posterImageView = (ImageView) itemView.findViewById(R.id.poster_item);
        alternativeNameTextView = (TextView) itemView.findViewById(R.id.alternative_name_item);
        yearTextView = (TextView) itemView.findViewById(R.id.year_item);
    }

    @Override
    public void onClick(View view) {
        if (itemClickListener != null) {
            itemClickListener.onItemClick(view, getLayoutPosition());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

}
