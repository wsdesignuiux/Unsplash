package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import e.wolfsoft1.unsplash.R;
import e.wolfsoft1.unsplash.UserPhotoDetails;
import model.AllPhotosModel;
import model.Result;

public class SearchPhotosAdapter extends RecyclerView.Adapter<SearchPhotosAdapter.MyViewHolder> {

    private List<Result> searchPhotoList;
    private List<AllPhotosModel> allPhotsModelList;
    private Context context;
    private ImageView imageUrl;

    public SearchPhotosAdapter(List<Result> searchPhotoList, Context context) {
        this.searchPhotoList = searchPhotoList;
        this.context = context;
    }


    @NonNull
    @Override
    public SearchPhotosAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_photos, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
                final Result model = searchPhotoList.get(position);

        if (model.getUrls().getThumb() != null && !(model.getUrls().getThumb().isEmpty())) {
            Picasso.get()
                    .load(model.getUrls().getThumb())
                    .placeholder(R.drawable.bg)
                    .into(imageUrl);

        } else {
            Toast.makeText(context, "There is no data", Toast.LENGTH_SHORT).show();
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, UserPhotoDetails.class);
                intent.putExtra("objectModel1", model);

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, holder.itemcard, ViewCompat.getTransitionName(holder.itemcard));

                context.startActivity(intent, options.toBundle());
//                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return searchPhotoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView itemcard;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageUrl = itemView.findViewById(R.id.all_photos);
            itemcard = itemView.findViewById(R.id.item_card);
        }
    }
}
