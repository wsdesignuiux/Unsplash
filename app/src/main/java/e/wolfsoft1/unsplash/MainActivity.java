package e.wolfsoft1.unsplash;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import adapter.AllPhotosAdapter;
import adapter.SearchPhotosAdapter;
import model.AllPhotosModel;
import model.Result;
import model.RetroPhoto;
import network.AllPhotosNetwork;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Url;

public class MainActivity extends AppCompatActivity {

    private AllPhotosAdapter photosAdapter;
    private SearchPhotosAdapter searchPhotosAdapter;
    private ProgressDialog progressDialog;
    private StaggeredGridLayoutManager layoutManager;
    private boolean isLoading;
    private List<AllPhotosModel> allPhotsModelList;
    private List<Result> searchPhotoList;

    private int page = 1;
    private int i;
    private int pastVisibleItems;

    RecyclerView recyclerView;
    private int previouspage;
    private ProgressBar progressbar;
    private EditText searchPhotos;
    ImageButton search;
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchPhotos = findViewById(R.id.search_photos);
        searchPhotos.setCursorVisible(false);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        search = findViewById(R.id.search);

        searchPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                searchPhotos.setCursorVisible(true);

                searchPhotos.requestFocus();

                // to open forcefully keyboard
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

            }
        });

        allPhotsModelList = new ArrayList<>();
        searchPhotoList = new ArrayList<>();

        recyclerView = findViewById(R.id.all_photos_recy);

        loadAllPhotos();

        isLoading = true;
        getData(page);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
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
                        && !isLoading && previouspage < page) {
                    isLoading = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getData(page);

                        }
                    }, 200);

                }

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                page = 1;
                searchPhoto();
                allPhotsModelList.clear();
                previouspage = 0;

                if (searchPhotos.getText().toString().isEmpty() && searchPhotos.getText().toString() == null) {
                    Toast.makeText(MainActivity.this, "No results Found", Toast.LENGTH_SHORT).show();
                } else {
                    getSearchData(page);
                }


            }
        });


        searchPhotos.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 0) {
                    loadAllPhotos();
                    searchPhotoList.clear();
                }
            }
        });

    }

    private void loadAllPhotos() {

//        recyclerView.setHasFixedSize(true);
//        recyclerView.setItemViewCacheSize(50);
//        recyclerView.setDrawingCacheEnabled(true);
//        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        photosAdapter = new AllPhotosAdapter(allPhotsModelList, this);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(photosAdapter);

    }

    private void searchPhoto() {
        recyclerView.setAdapter(null);
        searchPhotosAdapter = new SearchPhotosAdapter(searchPhotoList, MainActivity.this);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(searchPhotosAdapter);

    }


    private void getData(int pageno) {

        GetService getService = AllPhotosNetwork.getRetrofitResponse().create(GetService.class);
        Call<List<AllPhotosModel>> call = getService.getAllPhotos(GetService.CLIENT_ID, String.valueOf(pageno));

//        method is Asynchronous which means you can move on to another task before it finishes

        call.enqueue(new Callback<List<AllPhotosModel>>() {

            @Override
            public void onResponse(Call<List<AllPhotosModel>> call, Response<List<AllPhotosModel>> response) {

                progressDialog.dismiss();
                List<AllPhotosModel> model = response.body();

                if (model != null && previouspage < page) {

                    if (allPhotsModelList.size() == 0) {
                        allPhotsModelList.addAll(model);
                        photosAdapter.notifyDataSetChanged();
                        previouspage = page;
                        page++;

                    } else {

                        int previousposition = allPhotsModelList.size();
                        allPhotsModelList.addAll(model);
                        photosAdapter.notifyItemChanged(previousposition, model.size());
                        previouspage = page;
                        page++;
                    }
                    isLoading = false;

                }
            }

            @Override
            public void onFailure(Call<List<AllPhotosModel>> call, Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getSearchData(int pageno) {

        final String keyword = searchPhotos.getText().toString().trim();

        GetService getServiceSearch = AllPhotosNetwork.getRetrofitResponse().create(GetService.class);
        Call<RetroPhoto> call = getServiceSearch.getSearchList(GetService.CLIENT_ID, String.valueOf(pageno), keyword);

        call.enqueue(new Callback<RetroPhoto>() {
            @Override
            public void onResponse(Call<RetroPhoto> call, Response<RetroPhoto> response) {

                RetroPhoto model = response.body();
                List<Result> result = model.getResults();
                searchPhotoList.addAll(result);

                searchPhotosAdapter.notifyDataSetChanged();
                if (result != null && previouspage < page) {
                    isLoading = false;
                }

            }

            @Override
            public void onFailure(Call<RetroPhoto> call, Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
