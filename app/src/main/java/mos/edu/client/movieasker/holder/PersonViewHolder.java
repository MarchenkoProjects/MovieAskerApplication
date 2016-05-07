package mos.edu.client.movieasker.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import mos.edu.client.movieasker.R;

public class PersonViewHolder extends RecyclerView.ViewHolder {

    public ImageView personPhotoImageView;
    public TextView personNameRuTextView;

    public PersonViewHolder(View itemView) {
        super(itemView);

        personPhotoImageView = (ImageView) itemView.findViewById(R.id.person_photo_item);
        personNameRuTextView = (TextView) itemView.findViewById(R.id.person_name_ru_item);
    }

}