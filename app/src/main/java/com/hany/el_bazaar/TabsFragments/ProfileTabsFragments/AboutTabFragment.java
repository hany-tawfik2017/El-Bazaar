package com.hany.el_bazaar.TabsFragments.ProfileTabsFragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hany.el_bazaar.Defaults;
import com.hany.el_bazaar.R;

import java.util.Map;

/**
 * Created by Hany on 12/8/2018.
 */

public class AboutTabFragment extends Fragment {

    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
    PackageManager packageManager;
    TextView brandTitle, brandName, aboutContent;
    ImageView facebookImage, googleImage, instagramImage,separatorImage;
    String facebookUrl, googleUrl, instagramUrl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_tab_fragment, container, false);
        facebookImage = view.findViewById(R.id.facebook_contact);
        googleImage = view.findViewById(R.id.google_contact);
        instagramImage = view.findViewById(R.id.instagram_contact);
        separatorImage = view.findViewById(R.id.separator2);
        brandTitle = view.findViewById(R.id.brand_title);
        brandName = view.findViewById(R.id.brand_name);
        aboutContent = view.findViewById(R.id.about_content);
        packageManager = getActivity().getPackageManager();
        if (Defaults.getDefaults("userType", getActivity()) != null && Defaults.getDefaults("userType", getActivity()).equals("Vendor")) {
            brandTitle.setVisibility(View.VISIBLE);
            brandName.setVisibility(View.VISIBLE);
            separatorImage.setVisibility(View.VISIBLE);
        }
        getAboutUser();
        return view;
    }

    private void getAboutUser() {
        Query query = reference.orderByChild("name").equalTo(Defaults.getDefaults("userName", getActivity()));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    setAboutContent(map);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setAboutContent(Map<String, Object> map) {
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {

                if (entry.getValue() instanceof Map) {
                    Map<String, Object> mapObj = (Map<String, Object>) entry.getValue();
                    if (Defaults.getDefaults("userType", getActivity()) != null && Defaults.getDefaults("userType", getActivity()).equals("Vendor")) {
                        if (mapObj.get("brandName") != null)
                            brandName.setText((CharSequence) mapObj.get("brandName"));
                    }
                    if (mapObj.get("aboutMap") != null) {
                        Map<String, String> aboutMap = (Map<String, String>) mapObj.get("aboutMap");
                        if (aboutMap.get("aboutText") != null)
                            aboutContent.setText(aboutMap.get("aboutText"));
                        if (aboutMap.get("facebookUrl") != null)
                            facebookUrl = aboutMap.get("facebookUrl");
                        if (aboutMap.get("googleUrl") != null)
                            googleUrl = aboutMap.get("googleUrl");
                        if (aboutMap.get("instagramUrl") != null)
                            instagramUrl = aboutMap.get("instagramUrl");
                    }
                }
            }
        }
        setSocialMedia();
    }

    private void setSocialMedia() {
        facebookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String facebookPage = "";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if (facebookUrl != null) {
                    facebookPage = facebookUrl.split("/")[facebookUrl.split("/").length - 1];
                }

                String uri = "";
                try {
                    int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
                    if (versionCode >= 3002850)
                        uri = facebookUrl != null ? "fb://facewebmodal/f?href=" + facebookUrl : "fb://facewebmodal/f?href=";
                    else
                        uri = "fb://page/" + facebookPage;
                    intent.setPackage("com.facebook.katana");
                } catch (PackageManager.NameNotFoundException e) {
                    uri = facebookUrl != null ? facebookUrl : "http://www.facebook.com";
                }


                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        });
        instagramImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = instagramUrl != null ? instagramUrl : "https://www.instagram.com";
                String profileUser = instagramUrl != null ? instagramUrl.split("/")[instagramUrl.split("/").length - 1] : "";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                try {
                    if (packageManager.getPackageInfo("com.instagram.android", 0) != null) {
                        uri = "https://www.instagram.com/_u/" + profileUser;
                        intent.setPackage("com.instagram.android");
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        });
        googleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = googleUrl != null ? googleUrl : "https://plus.google.com/";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                try {
                    if (packageManager.getPackageInfo("com.google.android.apps.plus", 0) != null) {
                        intent.setPackage("com.google.android.apps.plus");
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        });
    }
}
