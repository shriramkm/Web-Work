package com.ebaysearch.shriram.myebaycustomsearchapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;


public class MainActivity extends ActionBarActivity {
    public static boolean noResultsFlag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("LOAD","MAIN ACTIVITY LOAD");
    }


    @Override
    protected void onResume()
    {
        // TODO Auto-generated method stub
        super.onResume();
        Log.e("LOAD","RESUME!!!");
        if(noResultsFlag){
            TextView validationText = (TextView)findViewById(R.id.validationText);
            Log.e("TAG","No results found!!!");
            validationText.setText("No Results Found");
            validationText.setTextColor(Color.BLUE);
            noResultsFlag = false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    /** Called when the user clicks the CLEAR button */
    public void clearForm(View view) {
        Context context = getApplicationContext();

        EditText keyword = (EditText)findViewById(R.id.keyword);
        keyword.setText("");

        EditText minPrice = (EditText)findViewById(R.id.minPrice);
        minPrice.setText("");

        EditText maxPrice = (EditText)findViewById(R.id.maxPrice);
        maxPrice.setText("");

        TextView validationText = (TextView)findViewById(R.id.validationText);
        validationText.setText("");

        Spinner sortBy = (Spinner)findViewById(R.id.sortBy);
        sortBy.setSelection(0);

        int duration = Toast.LENGTH_SHORT;
        //Toast toast = Toast.makeText(context, text, duration);
        //toast.show();
    }

    /** Called when the user clicks the SEARCH button */
    public void search(View view) {
        validateSearch(view);
    }

    public boolean validateSearch(View view){
        EditText keyword = (EditText)findViewById(R.id.keyword);
        EditText minPrice = (EditText)findViewById(R.id.minPrice);
        EditText maxPrice = (EditText)findViewById(R.id.maxPrice);
        TextView validationText = (TextView)findViewById(R.id.validationText);
        Spinner sortBy = (Spinner)findViewById(R.id.sortBy);
        validationText.setTextColor(Color.RED);
        if(("").equals(keyword.getText().toString().trim())){
            validationText.setText("Please enter a keyword");
            return false;
        }
        if(!"".equals(minPrice.getText().toString()) && !Pattern.matches("^[0-9]\\d*(\\.\\d+)?$",minPrice.getText().toString())){
            validationText.setText("Price From must be a positive integer or decimal number");
            return false;
        }
        if(!"".equals(maxPrice.getText().toString()) && !Pattern.matches("^[0-9]\\d*(\\.\\d+)?$",maxPrice.getText().toString())){
            validationText.setText("Price To must be a positive integer or decimal number");
            return false;
        }
        if(!"".equals(minPrice.getText().toString()) && !"".equals(maxPrice.getText().toString()) && Double.parseDouble(maxPrice.getText().toString()) < Double.parseDouble(minPrice.getText().toString())){
            validationText.setText("Price To must not be less than Price From");
            return false;
        }
        validationText.setText("");
        Intent intent = new Intent(this, SearchResultsActivity.class);
        intent.putExtra("keyword", keyword.getText().toString());
        intent.putExtra("minPrice", minPrice.getText().toString());
        intent.putExtra("maxPrice", maxPrice.getText().toString());
        String sortByValue = "BestMatch";
        if(sortBy.getSelectedItemPosition()==1){
            sortByValue = "CurrentPriceHighest";
        }
        else if(sortBy.getSelectedItemPosition()==2){
            sortByValue = "PricePlusShippingHighest";
        }
        else if(sortBy.getSelectedItemPosition()==3){
            sortByValue = "PricePlusShippingLowest";
        }
        intent.putExtra("sortBy", sortByValue);
        startActivity(intent);
        return true;
    }
}
