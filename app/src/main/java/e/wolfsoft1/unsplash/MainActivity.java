package e.wolfsoft1.unsplash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapter.AllPhotosAdapter;
import model.AllPhotosModel;
import network.AllPhotosNetwork;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Url;

public class MainActivity extends AppCompatActivity {

    private AllPhotosAdapter photosAdapter;
    private ProgressDialog progressDialog;
    private StaggeredGridLayoutManager layoutManager;
    private boolean isLoading;
    private List<AllPhotosModel> allPhotsModelList;
    private int page = 1;
    private int i;
    private int pastVisibleItems;
    private int previouspage = 1;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText searchPhotos = findViewById(R.id.search_photos);
        searchPhotos.setCursorVisible(false);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();


        allPhotsModelList = new ArrayList<>();

        recyclerView = findViewById(R.id.all_photos_recy);
        photosAdapter = new AllPhotosAdapter(allPhotsModelList, this);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(photosAdapter);

        isLoading = true;
        getData(page);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.e("aaa", "Scroller//// " + allPhotsModelList.size());
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                StaggeredGridLayoutManager manager =
                        (StaggeredGridLayoutManager) recyclerView.getLayoutManager();

                int[] lastPositions = new int[2];
                if (lastPositions == null)
                    lastPositions = new int[manager.getSpanCount()];

                manager.findLastCompletelyVisibleItemPositions(lastPositions);

                int visibleItemCount = manager.getChildCount();
                int totalItemCount = manager.getItemCount();
                int[] firstVisibleItems = manager.findFirstVisibleItemPositions(null);
                if (firstVisibleItems != null && firstVisibleItems.length > 0) {
                    pastVisibleItems = firstVisibleItems[0];
                }

                if ((visibleItemCount + pastVisibleItems) >= totalItemCount
                        && !isLoading && (page > previouspage)) {
                    isLoading = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            getData(page);

                        }
                    }, 200);

                }

//
//                int visibleItemCount = layoutManager.getChildCount();
//                int totalItemCount = layoutManager.getItemCount();
//                int[] firstVisibleItems = layoutManager.findFirstVisibleItemPositions(null);
//                int pastVisibleItems = 0;
//                if (firstVisibleItems != null && firstVisibleItems.length > 0) {
//                    pastVisibleItems = firstVisibleItems[0];
//                }
//
//                if ((visibleItemCount + pastVisibleItems) >= totalItemCount && !isLoading) {
//                    if (!isLoading) {
//                        isLoading = true;
//
//                        Toast.makeText(MainActivity.this, "Call"+String.valueOf(i), Toast.LENGTH_SHORT).show();
//                        i++;
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                getData(page);
//                            }
//                        }, 50);
//                    }
//                }

            }
        });

    }

    private void getData(final int pageno) {

        GetService getService = AllPhotosNetwork.getRetrofitResponse().create(GetService.class);
        Call<List<AllPhotosModel>> call = getService.getAllPhotos(GetService.CLIENT_ID, String.valueOf(pageno));

//        Log.e("TAG " , "URL" + call.toString());
//        method is Asynchronous which means you can move on to another task before it finishes

        call.enqueue(new Callback<List<AllPhotosModel>>() {

            @Override
            public void onResponse(Call<List<AllPhotosModel>> call, Response<List<AllPhotosModel>> response) {

                progressDialog.dismiss();

                List<AllPhotosModel> model = response.body();

//                Log.e("aaa", "Image URL " + model.get(i).getUrls().getThumb());
//                for (int i = 0; i < model.size(); i++) {
//                    Log.e("aaa", "Image URL " + model.get(i).getUrls().getThumb());
//                    Log.e("aaa", "get ID " + model.get(i).getId());
//                    Log.e("aaa", "getDescription " + model.get(i).getDescription());
                    if (model != null) {

                        if (allPhotsModelList.size() == 0) {
                            allPhotsModelList.addAll(model);
                            photosAdapter.notifyDataSetChanged();
                            Url url;
                            previouspage = page;

//                            page++;
                            page = pageno;

                            Toast.makeText(MainActivity.this, page, Toast.LENGTH_SHORT).show();
//                            Toast.makeText(MainActivity.this, String.valueOf(pageno), Toast.LENGTH_SHORT).show();
//                            page++;
//                            Log.e("aaa", "Array Size " + allPhotsModelList.size());

                        } else {
                            int previousposition = allPhotsModelList.size();
                            allPhotsModelList.addAll(model);
                            photosAdapter.notifyItemChanged(previousposition, model.size());
                            previouspage = page;
//                            page++;
//                            Log.e("aaa", String.valueOf(page));
//                            Log.e("aaa", "Array Size " + allPhotsModelList.size());
                        }
                        isLoading = false;
//                        photosAdapter = new AllPhotosAdapter(allPhotsModelList, MainActivity.this);
//                        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//                        recyclerView.setLayoutManager(layoutManager);
//                        recyclerView.setAdapter(photosAdapter);

                    }
                }
//            }

            @Override
            public void onFailure(Call<List<AllPhotosModel>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
