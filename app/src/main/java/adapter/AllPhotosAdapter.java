package adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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

public class AllPhotosAdapter extends RecyclerView.Adapter<AllPhotosAdapter.MyViewHolder> {

    private List<AllPhotosModel> allPhotsModelList;
    private Context context;

    ImageView imageUrl;

    public AllPhotosAdapter(List<AllPhotosModel> allPhotsModelList, Context context) {
        this.allPhotsModelList = allPhotsModelList;
        this.context = context;

    }

    @NonNull
    @Override
    public AllPhotosAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_photos, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.setIsRecyclable(false);
        final AllPhotosModel model = allPhotsModelList.get(position);

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
                intent.putExtra("objectModel", model);

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, holder.itemcard, ViewCompat.getTransitionName(holder.itemcard));

                context.startActivity(intent, options.toBundle());
//                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return allPhotsModelList.size();
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
