package e.wolfsoft1.unsplash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import model.AllPhotosModel;
import model.Result;

public class UserPhotoDetails extends AppCompatActivity {


    private ImageView photoDetails, profile, back;
    Button setWallpaper;
    TextView userName, name;

    AllPhotosModel allPhotosModel;
    Result searchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_photo_details);

        intializemethod();
        supportPostponeEnterTransition();
        TextView viewProfile = findViewById(R.id.view_profile);

        //get intent
        Intent intent = getIntent();
        allPhotosModel = (AllPhotosModel) intent.getSerializableExtra("objectModel");
        searchResult = (Result) intent.getSerializableExtra("objectModel1");
        Picasso.get()
                .load(allPhotosModel.getUser().getProfileImage().getLarge())
                .placeholder(R.drawable.ic_launcher_background)
                .fit()
                .into(profile);
        name.setText(allPhotosModel.getUser().getName());
        userName.setText("@" + allPhotosModel.getUser().getUsername());

        // method to load image in string format
        Picasso.get()
                .load(allPhotosModel.getUrls().getRegular())
                .placeholder(R.drawable.ic_launcher_background)
                .fit().centerCrop()
                .into(photoDetails, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        supportStartPostponedEnterTransition();
                    }

                    @Override
                    public void onError(Exception e) {
                        supportStartPostponedEnterTransition();
                    }

                });

        setWallpaper.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {

                WallpaperManager myWallpaperManager
                        = WallpaperManager.getInstance(getApplicationContext());
                try {

                    myWallpaperManager.setResource(R.drawable.bg);

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //to navigate in profile detailed activity
                Intent intent = new Intent(UserPhotoDetails.this, Profile.class);
                intent.putExtra("objectModel", allPhotosModel);

                Pair[] pairs = new Pair[3];
                pairs[0] = new Pair<View, String>(profile, "profile_pic");
                pairs[1] = new Pair<View, String>(name, "profile_name");
                pairs[2] = new Pair<View, String>(userName, "username");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(UserPhotoDetails.this, pairs);
                startActivity(intent);

            }
        });
        //finish task
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }


    private void intializemethod() {

        photoDetails = findViewById(R.id.photo_details);
        name = findViewById(R.id.name);
        userName = findViewById(R.id.user_name);
        setWallpaper = findViewById(R.id.set_wallpaper);
        profile = findViewById(R.id.profile_image);
        back = findViewById(R.id.back);

    }

    @Override
    public void onBackPressed() {
        //To support reverse transitions when user clicks the device back button
        supportFinishAfterTransition();
        super.onBackPressed();
    }


}
