package com.ebaysearch.shriram.myebaycustomsearchapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookDialog;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;


public class DetailsActivity extends ActionBarActivity {
    public static JSONObject productsData;
    private static JSONObject basicInfo;
    private static JSONObject sellerInfo;
    private static JSONObject shippingInfo;
    private static ArrayList<String> basicInfoLabelsList;
    private static ArrayList<String> sellerInfoLabelsList;
    private static ArrayList<String> shippingInfoLabelsList;
    private static ArrayList<String> basicInfoList;
    private static ArrayList<String> sellerInfoList;
    private static ArrayList<String> shippingInfoList;
    private static AlertDialog.Builder dialog;
    private static String itemURL;
    private static String itemTitle;
    private static String itemImgURL;
    private static String itemPrice;
    private static String itemLocation;
    private static CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        View someView = findViewById(R.id.details);
        View root = someView.getRootView();
        root.setBackgroundColor(getResources().getColor(android.R.color.white));
        String itemNo = getIntent().getStringExtra("itemNo");
        try {
            basicInfo = (JSONObject)((JSONObject) productsData.get("item"+itemNo)).get("basicInfo");
            ImageView productImgView = (ImageView)findViewById(R.id.productImage);
            String productImgURL = (String)basicInfo.get("pictureURLSuperSize");
            if("".equals(productImgURL)){
                productImgURL = (String)basicInfo.get("galleryURL");
            }
            new DownloadImageTask(productImgView).execute(productImgURL);
            TextView productTitleView = (TextView)findViewById(R.id.productTitle);
            productTitleView.setText((String)basicInfo.get("title"));
            TextView productPriceView = (TextView)findViewById(R.id.productPrice);
            String shippingCost = (String)basicInfo.get("shippingServiceCost");
            if("".equals(shippingCost) || "0.0".equals(shippingCost)){
                shippingCost = "(FREE Shipping)";
            }
            else{
                shippingCost = "(+ $"+shippingCost+" Shipping)";
            }
            productPriceView.setText("Price: $"+(String)basicInfo.get("convertedCurrentPrice")+" "+shippingCost);
            TextView productLocationView = (TextView)findViewById(R.id.productLocation);
            productLocationView.setText((String)basicInfo.get("location"));
            String topratedListing = (String)basicInfo.get("topRatedListing");
            ImageView topratedIconView = (ImageView)findViewById(R.id.topratedicon);
            //productLocationView.setText((String)basicInfo.get("location"));
            if(!"true".equals(topratedListing)){
                topratedIconView.setVisibility(View.INVISIBLE);
            }
            ImageView buynowView = (ImageView)findViewById(R.id.buynowicon);
            buynowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse((String) basicInfo.get("viewItemURL")));
                        startActivity(browserIntent);
                    }
                    catch(JSONException e){
                        Log.e("TAG","Exception in going to URL!");
                    }
                }
            });

            ImageView fbView = (ImageView)findViewById(R.id.facebookicon);
            itemURL = (String) basicInfo.get("viewItemURL");
            itemTitle = (String) basicInfo.get("title");
            itemImgURL = productImgURL;
            itemPrice = productPriceView.getText()+"";
            itemLocation = (String)basicInfo.get("location");

            sellerInfo = (JSONObject)((JSONObject) productsData.get("item"+itemNo)).get("sellerInfo");
            shippingInfo = (JSONObject)((JSONObject) productsData.get("item"+itemNo)).get("shippingInfo");
        }
        catch(JSONException e){
            Log.e("TAG",e.getMessage());
        }
        basicInfoLabelsList = new ArrayList<String>();
        basicInfoLabelsList.add("Category Name");
        basicInfoLabelsList.add("Condition");
        basicInfoLabelsList.add("Buying Format");
        basicInfoList = new ArrayList<String>();
        try {
            basicInfoList.add((String) basicInfo.get("categoryName"));
            basicInfoList.add((String) basicInfo.get("conditionDisplayName"));
            basicInfoList.add((String) basicInfo.get("listingType"));
            Collections.replaceAll(basicInfoList, null, "N/A");
            Collections.replaceAll(basicInfoList, "", "N/A");
        }
        catch(JSONException e){
            Log.e("TAG","Exception while populating BasicInfo grid data");
        }
        sellerInfoLabelsList = new ArrayList<String>();
        sellerInfoLabelsList.add("User Name");
        sellerInfoLabelsList.add("Feedback Score");
        sellerInfoLabelsList.add("Positive Feedback");
        sellerInfoLabelsList.add("Feedback Rating");
        sellerInfoLabelsList.add("Top Rated");
        sellerInfoLabelsList.add("Store");
        sellerInfoList = new ArrayList<String>();
        try {
            sellerInfoList.add((String) sellerInfo.get("sellerUserName"));
            sellerInfoList.add((String) sellerInfo.get("feedbackScore"));
            sellerInfoList.add((String) sellerInfo.get("positiveFeedbackPercent"));
            sellerInfoList.add((String) sellerInfo.get("feedbackRatingStar"));
            sellerInfoList.add((String) sellerInfo.get("topRatedSeller"));
            sellerInfoList.add((String) sellerInfo.get("sellerStoreName"));
            Collections.replaceAll(sellerInfoList, null, "N/A");
            Collections.replaceAll(sellerInfoList, "", "N/A");
        }
        catch(JSONException e){
            Log.e("TAG","Exception while populating SellerInfo grid data");
        }
        shippingInfoLabelsList = new ArrayList<String>();
        shippingInfoLabelsList.add("Shipping Type");
        shippingInfoLabelsList.add("Handling Time");
        shippingInfoLabelsList.add("Shipping Locations");
        shippingInfoLabelsList.add("Expedited Shipping");
        shippingInfoLabelsList.add("One Day Shipping");
        shippingInfoLabelsList.add("Returns Accepted");
        shippingInfoList = new ArrayList<String>();
        try {
            shippingInfoList.add(((String) shippingInfo.get("shippingType")));
            shippingInfoList.add((String) shippingInfo.get("handlingTime"));
            shippingInfoList.add((String) shippingInfo.get("shipToLocations"));
            shippingInfoList.add((String) shippingInfo.get("expeditedShipping"));
            shippingInfoList.add((String) shippingInfo.get("oneDayShippingAvailable"));
            shippingInfoList.add((String) shippingInfo.get("returnsAccepted"));
            Collections.replaceAll(shippingInfoLabelsList, null, "N/A");
            Collections.replaceAll(shippingInfoList, "", "N/A");
        }
        catch(JSONException e){
            Log.e("TAG","Exception while populating ShippingInfo grid data");
        }
        ((ImageView)findViewById(R.id.basicInfoButton)).performClick();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onFacebookShare(View view){
        Log.e("TAG","ON FB SHARE");
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        ShareDialog shareDialog = new ShareDialog(this);
        Log.e("TAG","2");
        // this part is optional
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                String text = "Posted Story, ID: "+result.getPostId();
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                Log.e("TAG", "POST SHARED SUCCESFULLY!");
            }

            @Override
            public void onCancel() {

                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                String text = "Post Cancelled";
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                Log.e("TAG","POST NOT SHARED!");
            }

            @Override
            public void onError(FacebookException e) {
                Log.e("TAG","POST SHARE ERROR!");
            }
        });
        Log.e("TAG","3");
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(itemTitle)
                    .setContentDescription(itemPrice+", Location: "+itemLocation)
                    .setContentUrl(Uri.parse(itemURL))
                    .setImageUrl(Uri.parse(itemImgURL))
                    .build();
            Log.e("TAG","4");
            shareDialog.show(linkContent);
            Log.e("TAG","5");
        }
    }

    public void onDetailsButtonClick(View view){
        ImageView imgView = (ImageView)view;
        ImageView basicInfoButton = (ImageView)findViewById(R.id.basicInfoButton);
        ImageView sellerButton = (ImageView)findViewById(R.id.sellerButton);
        ImageView shippingButton = (ImageView)findViewById(R.id.shippingButton);

        if(imgView.getId() == basicInfoButton.getId()){
            imgView.setImageResource(R.mipmap.ic_basicinfo_clicked);
            sellerButton.setImageResource(R.mipmap.ic_seller);
            shippingButton.setImageResource(R.mipmap.ic_shipping);
            int pos = R.id.textView;
            for( int i = 0 ; i < basicInfoLabelsList.size(); i++ ){
                TextView tv = (TextView)findViewById(pos++);
                tv.setText(basicInfoLabelsList.get(i));
                tv = (TextView)findViewById(pos++);
                tv.setText(basicInfoList.get(i));
            }
            for( int i = 3 ; i < basicInfoLabelsList.size()+3; i++ ){
                TextView tv = (TextView)findViewById(pos++);
                tv.setText("");
                tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                tv = (TextView)findViewById(pos++);
                tv.setText("");
                tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
        }
        if(imgView.getId() == sellerButton.getId()){
            imgView.setImageResource(R.mipmap.ic_seller_clicked);
            basicInfoButton.setImageResource(R.mipmap.ic_basicinfo);
            shippingButton.setImageResource(R.mipmap.ic_shipping);
            int pos = R.id.textView;
            for( int i = 0 ; i < sellerInfoLabelsList.size(); i++ ){
                TextView tv = (TextView)findViewById(pos++);
                tv.setText(sellerInfoLabelsList.get(i));
                tv = (TextView)findViewById(pos++);
                if(i==4 && "true".equals(sellerInfoList.get(i))) {
                    tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.yes, 0, 0, 0);
                    tv.setText("");
                }
                else if(i==4 && "false".equals(sellerInfoList.get(i))) {
                    tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.no, 0, 0, 0);
                    tv.setText("");
                }
                else {
                    tv.setText(sellerInfoList.get(i));
                    tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        }
        if(imgView.getId() == shippingButton.getId()){
            imgView.setImageResource(R.mipmap.ic_shipping_clicked);
            basicInfoButton.setImageResource(R.mipmap.ic_basicinfo);
            sellerButton.setImageResource(R.mipmap.ic_seller);
            int pos = R.id.textView;
            for( int i = 0 ; i < shippingInfoLabelsList.size(); i++ ){
                TextView tv = (TextView)findViewById(pos++);
                tv.setText(shippingInfoLabelsList.get(i));
                tv = (TextView)findViewById(pos++);
                if((i==3 || i==4 || i==5) &&  "true".equals(shippingInfoList.get(i))) {
                    tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.yes, 0, 0, 0);
                    tv.setText("");
                }
                else if((i==3 || i==4 || i==5) && "false".equals(shippingInfoList.get(i))) {
                    tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.no, 0, 0, 0);
                    tv.setText("");
                }
                else
                    tv.setText(shippingInfoList.get(i));
            }
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
