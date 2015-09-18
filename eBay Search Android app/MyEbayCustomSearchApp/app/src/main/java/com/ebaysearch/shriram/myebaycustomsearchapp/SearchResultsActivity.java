package com.ebaysearch.shriram.myebaycustomsearchapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class SearchResultsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        View someView = findViewById(R.id.resultsList);
        View root = someView.getRootView();
        root.setBackgroundColor(getResources().getColor(android.R.color.white));
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Log.e(this.getClass().getSimpleName(),"App started1!");
        TextView resultsFor = (TextView)findViewById(R.id.resultsFor);
        resultsFor.setText("Results for '"+getIntent().getStringExtra("keyword")+"'");
       try {
            new SearchResultsAsyncTask()
                    .execute("http://shriramapp1.elasticbeanstalk.com/processForm.php?keyword="
                            + URLEncoder.encode(getIntent().getStringExtra("keyword"), "UTF-8")
                            + "&minPrice="
                            + URLEncoder.encode(getIntent().getStringExtra("minPrice"), "UTF-8")
                            + "&maxPrice="
                            + URLEncoder.encode(getIntent().getStringExtra("maxPrice"), "UTF-8")
                            + "&sortOrder="
                            + URLEncoder.encode(getIntent().getStringExtra("sortBy"), "UTF-8")
                            + "&results="
                            + "5");
        }
        catch (UnsupportedEncodingException e){
            Log.e(this.getClass().getSimpleName(),"Exception while forming the URL!");
        }
        Log.e(this.getClass().getSimpleName(),"App started2!");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_results, menu);
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

    private void refreshResultsList(List<String> titleList,List<String> priceList,List<String> iconList,List<String> linksList) {
        ListView searchResultsList = (ListView)findViewById(R.id.resultsList);
        searchResultsList.setAdapter(new MySimpleArrayAdapter(this,titleList,priceList,iconList,linksList));
    }

    private class SearchResultsAsyncTask extends AsyncTask<String, String, String> {
        // make a request to the specified url
        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                // make a HTTP request
                Log.e("Test","URL : "+uri[0]);
                response = httpclient.execute(new HttpGet(uri[0]));
                //Log.e("Test","URL Response : "+response.getEntity().getContent()+" Code : "+response.getStatusLine());
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    out.close();
                    responseString = out.toString();
                } else {
                    // close connection
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (Exception e) {
                Log.d("Test", "Couldn't make a successful request! "+e.getMessage());
            }
            Log.d("Test", "RESPONSE1 : "+responseString);
            return responseString;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            Log.d("Test", "RESPONSE2 : "+response);
            ArrayList<String> titleList = new ArrayList<String>();
            ArrayList<String> priceList = new ArrayList<String>();
            ArrayList<String> iconList = new ArrayList<String>();
            ArrayList<String> linksList = new ArrayList<String>();

            try {
                // convert the String response to a JSON object
                JSONObject jsonResponse = new JSONObject(response);
                DetailsActivity.productsData = jsonResponse;
                Iterator<String> keys = jsonResponse.keys();

                // fetch the items in the response
                while(keys.hasNext()) {
                    String key = keys.next();
                    if(key.equals("resultCount") && (((String)jsonResponse.get(key)).equals("0"))){
                        Log.e("RES","No results found!");
                        MainActivity.noResultsFlag = true;
                        finish();
                    }
                    if(key.startsWith("item") && jsonResponse.get(key) instanceof JSONObject) {
                        JSONObject item = (JSONObject)jsonResponse.get(key);
                        JSONObject basicInfo = (JSONObject)item.get("basicInfo");
                        titleList.add((String) basicInfo.get("title"));
                        String shippingCost = (String) basicInfo.get("shippingServiceCost");
                        if(("0.0").equals(shippingCost) || ("").equals(shippingCost))
                            shippingCost = "(FREE Shipping)";
                        else
                            shippingCost = "(+ $"+shippingCost+" Shipping)";
                        priceList.add("Price: $"+(String) basicInfo.get("convertedCurrentPrice")+" "+shippingCost);
                        String imgURL = "";
                        if(null != basicInfo.get("galleryURL"))
                            imgURL = (String) basicInfo.get("galleryURL");
                        if("".equals(imgURL) && null != basicInfo.get("pictureURLSuperSize"))
                            iconList.add((String) basicInfo.get("pictureURLSuperSize"));
                        else
                            iconList.add(imgURL);
                        linksList.add((String) basicInfo.get("viewItemURL"));
                    }
                }

                // refresh the ListView
                refreshResultsList(titleList,priceList,iconList,linksList);

            } catch (JSONException e) {
                Log.d("Test", "Couldn't successfully parse the JSON response!");
            }
        }
    }

    private class MySimpleArrayAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final List<String> titles;
        private final List<String> prices;
        private final List<String> icons;
        private final List<String> links;

        public MySimpleArrayAdapter(Context context, List<String> titles,List<String> prices,List<String> icons,List<String> links) {
            super(context, R.layout.search_results_listitem, titles);
            this.context = context;
            this.titles = titles;
            this.prices = prices;
            this.icons = icons;
            this.links = links;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.search_results_listitem, parent, false);
            TextView itemTitleView = (TextView) rowView.findViewById(R.id.itemTitle);
            TextView itemPriceView = (TextView) rowView.findViewById(R.id.itemPrice);
            ImageView itemIconView = (ImageView) rowView.findViewById(R.id.itemIcon);
            TextView itemLinkView = (TextView) rowView.findViewById(R.id.itemLink);
            itemTitleView.setText(titles.get(position));
            itemPriceView.setText(prices.get(position));
            itemLinkView.setText(links.get(position));
            new DownloadImageTask(itemIconView).execute(icons.get(position));

            return rowView;
        }
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

    /** Called when the user clicks on any image in the search results*/
    public void viewProductImage(View view) {
        RelativeLayout vwParentRow = (RelativeLayout)view.getParent();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(((TextView)vwParentRow.getChildAt(3)).getText()+""));
        startActivity(browserIntent);
    }

    /** Called when the user clicks on any product's title in the search results*/
    public void viewProductDetails(View view) {
        Intent intent = new Intent(this, DetailsActivity.class);
        RelativeLayout vwParentRow = (RelativeLayout)view.getParent();
        int position = ((ListView)vwParentRow.getParent()).getPositionForView(vwParentRow);
        intent.putExtra("itemNo",position+"");
        startActivity(intent);
    }
}
