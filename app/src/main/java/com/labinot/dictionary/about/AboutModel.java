package com.labinot.dictionary.about;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.labinot.dictionary.R;

import java.util.ArrayList;
import java.util.List;

public class AboutModel {

    private List<AboutModel> aboutModelsList = new ArrayList<>();
    private int icon;
    private String title;
    private Context context;
    private int tintColor;
    private String content_ID;
    private Intent intent;


    public AboutModel(Context context) {
        this.context = context;
    }

    public AboutModel() {
        this.context = context;
    }

    public List<AboutModel> getAboutModelsList() {
        return aboutModelsList;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTintColor() {
        return tintColor;
    }

    public void setTintColor(int tintColor) {
        this.tintColor = tintColor;
    }

    public String getContent_ID() {
        return content_ID;
    }

    public void setContent_ID(String content_ID) {
        this.content_ID = content_ID;
    }

    public void addNewItem(AboutModel model){

        aboutModelsList.add(model);

    }

    public void addFacebook(String id,String title){

        AboutModel facebookElement = new AboutModel();
        facebookElement.setTitle(title);
        facebookElement.setIcon(R.drawable.ic_facebook);
        facebookElement.setTintColor(context.getResources().getColor(R.color.facebookColor));
        facebookElement.setContent_ID(id);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);

        if(isAppInstalled(context,"com.facebook.katana")){

            intent.setPackage("com.facebook.katana");

            int VersionCode = 0;


            try {
                VersionCode = context.getPackageManager().getPackageInfo("com.facebook.katana",0).versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            Uri uri;

            if(VersionCode >= 3002850){

                uri = Uri.parse("fb://facewebmodal/f?href=" + "http://m.facebook.com/" + id);

            }else{

                uri = Uri.parse("fb://page/" + id);
            }

              intent.setData(uri);
        }else{

            intent.setData(Uri.parse("http://m.facebook.com/" + id));

        }

        facebookElement.setIntent(intent);
        addNewItem(facebookElement);

    }

    public void addInstagram(String id,String title){

        AboutModel instagramElement = new AboutModel();
        instagramElement.setTitle(title);
        instagramElement.setIcon(R.drawable.ic_instagram);
        instagramElement.setTintColor(context.getResources().getColor(R.color.instagramColor));
        instagramElement.setContent_ID(id);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("http://instagram.com/_u/" + id));

        if(isAppInstalled(context,"com.instagram.android"))
            intent.setPackage("com.instagram.android");

        instagramElement.setIntent(intent);
        addNewItem(instagramElement);

    }

    public void addEmail(String email,String title){

        AboutModel emailelement = new AboutModel();
        emailelement.setTitle(title);
        emailelement.setIcon(R.drawable.ic_email);
        emailelement.setTintColor(context.getResources().getColor(R.color.emailColor));

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL,new String[]{email});

        emailelement.setIntent(intent);
        addNewItem(emailelement);
    }

     public void addGithub(String id,String title){

        AboutModel gitHub_element = new AboutModel();
        gitHub_element.setTitle(title);
        gitHub_element.setIcon(R.drawable.ic_github);
        gitHub_element.setTintColor(context.getResources().getColor(R.color.gitHubColor));
        gitHub_element.setContent_ID(id);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(String.format("https://github.com/%s", id)));

        gitHub_element.setIntent(intent);
        addNewItem(gitHub_element);

     }

     public void addWebside(String url,String title){

        if(!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;

        AboutModel websideElement = new AboutModel();
        websideElement.setTitle(title);
        websideElement.setIcon(R.drawable.ic_internet);
        websideElement.setTintColor(context.getResources().getColor(R.color.websiteColor));

        Uri uri = Uri.parse(url);

        Intent webintent = new Intent(Intent.ACTION_VIEW,uri);
        websideElement.setIntent(webintent);
        addNewItem(websideElement);

     }

     public void addYoutube(String id, String title){

        AboutModel youtubeElement = new AboutModel();
        youtubeElement.setTitle(title);
        youtubeElement.setIcon(R.drawable.ic_youtube);
        youtubeElement.setTintColor(context.getResources().getColor(R.color.youtubeColor));
        youtubeElement.setContent_ID(id);

        Intent intent = new Intent();

        if(isAppInstalled(context,"com.google.android.youtube")){

            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(String.format("http://youtube.com/channel/%s",id)));
            intent.setPackage("com.google.android.youtube");

        }else{

            intent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.youtube.com/channel/"+id));
        }

        youtubeElement.setIntent(intent);
        addNewItem(youtubeElement);


     }

     public void addPlayStore(String id, String title){

        AboutModel playstoreElement =new AboutModel();
        playstoreElement.setTitle(title);
        playstoreElement.setIcon(R.drawable.ic_playstore);
        playstoreElement.setTintColor(context.getResources().getColor(R.color.playStoreColor));
        playstoreElement.setContent_ID(id);

        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + id);
        Intent gotoMarket = new Intent(Intent.ACTION_VIEW,uri);
        playstoreElement.setIntent(gotoMarket);

        addNewItem(playstoreElement);

     }

     public void addCustomItem(int icon,int youColor,String title){

        AboutModel customElement = new AboutModel();
        customElement.setTitle(title);
        customElement.setIcon(icon);
        customElement.setTintColor(youColor);

        addNewItem(customElement);

     }

    static boolean isAppInstalled(Context context, String appName) {

        PackageManager packageManager = context.getPackageManager();

        boolean Installed = false;

        List<PackageInfo> packageInfos =packageManager.getInstalledPackages(0);

        for(PackageInfo packageInfo1: packageInfos){

            if(packageInfo1.packageName.equals(appName)){

                Installed = true;
                break;
            }

        }

        return Installed;

    }
}
