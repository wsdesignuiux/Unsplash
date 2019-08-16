package e.wolfsoft1.unsplash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import model.AllPhotosModel;

public class Profile extends AppCompatActivity {

    private AllPhotosModel allPhotosModel;
    private ImageView profileImage, locationIcon, webIcon;
    private TextView nameOfProfile, userName, location, websiteAddress, bioOfUser;
    private String image, profilename, username;
    View viewLine;
    private TextView relatedImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initializeMethod();
        supportPostponeEnterTransition();

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        allPhotosModel = (AllPhotosModel) intent.getSerializableExtra("objectModel");

        image = intent.getStringExtra("profile_pic");
        profilename = intent.getStringExtra("profile_name");
        username = intent.getStringExtra("username");

        if (allPhotosModel.getUser().getProfileImage().getLarge() != null) {
            Picasso.get()
                    .load(allPhotosModel.getUser().getProfileImage().getLarge())
                    .placeholder(R.drawable.placeholder)
                    .fit()
                    .into(profileImage, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            supportStartPostponedEnterTransition();
                        }

                        @Override
                        public void onError(Exception e) {
                            supportStartPostponedEnterTransition();
                        }

                    });
        }

        if (allPhotosModel.getUser().getName() != null) {
            nameOfProfile.setText(allPhotosModel.getUser().getName());
        }

        if (allPhotosModel.getUser().getUsername() != null) {
            userName.setText("@" + allPhotosModel.getUser().getUsername());
        }

        if (allPhotosModel.getUser().getPortfolioUrl() == null) {

            webIcon.setVisibility(View.GONE);
            viewLine.setVisibility(View.GONE);
            websiteAddress.setVisibility(View.GONE);

        } else {
            websiteAddress.setText(allPhotosModel.getUser().getPortfolioUrl().toString());
        }
        if (allPhotosModel.getUser().getLocation() == null) {
            location.setVisibility(View.GONE);
            viewLine.setVisibility(View.GONE);
            locationIcon.setVisibility(View.GONE);
        } else {
            location.setText(allPhotosModel.getUser().getLocation().toString());
        }

        if (allPhotosModel.getUser().getBio() == null) {
            bioOfUser.setVisibility(View.GONE);
        } else {
            bioOfUser.setText(allPhotosModel.getUser().getBio().toString());
        }

        if ((image == null) && (profilename == null) && (username == null) && (allPhotosModel.getUser().getPortfolioUrl() == null) && (allPhotosModel.getUser().getLocation() == null) && (allPhotosModel.getUser().getBio() == null)) {
            relatedImages.setVisibility(View.VISIBLE);
        } else {
            relatedImages.setVisibility(View.GONE);
        }
    }

    private void initializeMethod() {

        profileImage = findViewById(R.id.profile_image);
        nameOfProfile = findViewById(R.id.name_of_profile);
        userName = findViewById(R.id.user_name);
        location = findViewById(R.id.location);
        websiteAddress = findViewById(R.id.website_address);
        bioOfUser = findViewById(R.id.bio_of_user);
        locationIcon = findViewById(R.id.location_icon);
        webIcon = findViewById(R.id.web_icon);
        viewLine = findViewById(R.id.viewline);
        relatedImages = findViewById(R.id.related_images);

    }
}
