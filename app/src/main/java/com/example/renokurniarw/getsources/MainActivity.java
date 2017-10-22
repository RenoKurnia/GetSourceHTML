package com.example.renokurniarw.getsources;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.renokurniarw.getsources.R;
import com.example.renokurniarw.getsources.SourceGetter;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    TextView get_HTML;
    Spinner spin;
    EditText url_source;
    ArrayAdapter<CharSequence> list_spinner;
    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spin = (Spinner) findViewById(R.id.spinner_protokol);
        url_source = (EditText) findViewById(R.id.input);
        get_HTML = (TextView) findViewById(R.id.sumber_HTML);
        loading = (ProgressBar) findViewById(R.id.loading);

        list_spinner = ArrayAdapter.createFromResource(this, R.array.protokol, android.R.layout.simple_spinner_item);
        list_spinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(list_spinner);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                Log.e("Error" + Thread.currentThread().getStackTrace()[2], paramThrowable.getLocalizedMessage());
            }
        });
        if (getSupportLoaderManager().getLoader(0) != null) {
            getSupportLoaderManager().initLoader(0, null, this);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exiting Program")
                .setMessage("Are you sure?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void Connector(View view) {
        String link_url, protokol, url;
        protokol = spin.getSelectedItem().toString();
        url = url_source.getText().toString();
        InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

        //checking the Domain Input, fixed, get help by cipowela repo
        if (!url.isEmpty()) {
            if (url.contains(".") && !(url.contains(" "))) {
                if (url.split("\\.").length > 1) {
                    if (IsConnect()) {
                        get_HTML.setText("");
                        loading.setVisibility(View.VISIBLE);
                        link_url = protokol + url;
                        Bundle Humblebundle = new Bundle();
                        Humblebundle.putString("url_link", link_url);
                        getSupportLoaderManager().restartLoader(0, Humblebundle, this);

                    } else {
                        Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                        get_HTML.setText("Connection error, cannot Estabilished connection");

                    }
                } else {
                    get_HTML.setText("Unknown Domain Format");
                }
            } else {
                get_HTML.setText("Invalid URL");
            }

        } else {
            get_HTML.setText("URL cannot empty");
        }
    }

    public boolean IsConnect() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new SourceGetter(this, args.getString("the_link"));
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        loading.setVisibility(View.GONE);
        get_HTML.setText(data);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
    }
}