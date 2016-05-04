package mos.edu.client.movieasker.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import mos.edu.client.movieasker.R;
import mos.edu.client.movieasker.app.Constants;
import mos.edu.client.movieasker.dto.FilmDTO;

public class PersonListAdapter extends RecyclerView.Adapter<PersonListAdapter.PersonViewHolder> {
    private static final int ITEM_LAYOUT = R.layout.person_item;

    private static final DisplayImageOptions IMAGE_OPTIONS = new DisplayImageOptions.Builder()
            .cacheInMemory(false)
            .cacheOnDisk(false)
            .showImageForEmptyUri(R.drawable.no_poster)
            .showImageOnFail(R.drawable.no_poster)
            .showImageOnLoading(R.drawable.no_poster)
            .build();

    private final List<FilmDTO.PersonDTO> persons = new ArrayList<>();

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(ITEM_LAYOUT, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        final FilmDTO.PersonDTO person = persons.get(position);

        ImageLoader.getInstance().displayImage(
                Constants.URI.POSTERS + person.getFotoUrl(),
                holder.personPhotoImageView,
                IMAGE_OPTIONS
        );
        holder.personNameRuTextView.setText(person.getNameRu());
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    public void setPersons(List<FilmDTO.PersonDTO> persons) {
        this.persons.addAll(persons);
        this.notifyDataSetChanged();
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        private ImageView personPhotoImageView;
        private TextView personNameRuTextView;

        public PersonViewHolder(View itemView) {
            super(itemView);

            personPhotoImageView = (ImageView) itemView.findViewById(R.id.person_photo_item);
            personNameRuTextView = (TextView) itemView.findViewById(R.id.person_name_ru_item);
        }

    }

}
