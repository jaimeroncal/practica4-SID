package es.unizar.eina.p4sid;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;

public class MyAsyncTask extends AsyncTask<String, Void, List<MyAsyncTask.Photo>>
{
    private MainActivity mActivity = null;

    // CLASES PARA PARSEAR EL JSON CON GSON
    static class Description {
        String _content;
    }
    static class Photo {
        String id;
        String owner;
        String secret;
        String server;
        Integer farm;
        String title;
        Integer ispublic;
        Integer isfriend;
        Integer isfamily;
        Description description;
        String tags;
        String url_h;
    }

    static class Photos {
        Integer page;
        Integer pages;
        Integer perpage;
        String total;
        List<Photo> photo;
    }

    static class Result {
        Photos photos;
        String stat;
    }

    //METODOS DE LA CLASE
    public MyAsyncTask(MainActivity activity)
    {
        attach(activity);
    }

    @Override
    protected List<MyAsyncTask.Photo> doInBackground(String... params)
    {
        HttpURLConnection connection;

        try {
            connection = (HttpURLConnection) new URL(params[0]).openConnection();

            // Lecutura de json y parse a clase Result
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            Gson gson = new Gson();
            Result resultado = gson.fromJson(reader, Result.class);

            return resultado.photos.photo;
        } catch (IOException e) {
            //e.printStackTrace();
            Log.e("ERROR",e.toString());
        }
        List<Photo> empty = Collections.<Photo>emptyList();
        return empty;
    }

    @Override
    protected void onPostExecute(List<MyAsyncTask.Photo> list)
    {
        if (mActivity == null)
            Log.i("MyAsyncTask", "Me salto onPostExecute() -- no hay nueva activity");
        else
            mActivity.setupAdapter(list);
    }

    void detach()
    {
        this.mActivity = null;
    }

    void attach(MainActivity activity)
    {
        this.mActivity = activity;
    }
}