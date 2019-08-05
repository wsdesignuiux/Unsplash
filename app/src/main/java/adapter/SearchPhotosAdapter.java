package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import e.wolfsoft1.unsplash.R;
import model.AllPhotosModel;

public class SearchPhotosAdapter extends RecyclerView.Adapter<SearchPhotosAdapter.MyViewHolder> {

    private List<AllPhotosModel> searchPhotoList;
    private Context context;

    public SearchPhotosAdapter(List<AllPhotosModel> searchPhotoList, Context context) {
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Picasso.get()
                .load(searchPhotoList.get(position).getUrls().getThumb())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imageUrl);
    }

    @Override
    public int getItemCount() {
        return searchPhotoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageUrl;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageUrl = itemView.findViewById(R.id.all_photos);
        }
    }
}
