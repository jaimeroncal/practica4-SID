package es.unizar.eina.p4sid;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.google.gson.Gson;

public class MyAsyncTask extends AsyncTask<String, Void, String>
{
    private MainActivity mActivity = null;

    static class Description {
        String content;
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


    public MyAsyncTask(MainActivity activity)
    {
        attach(activity);
    }
    @Override
    protected String doInBackground(String... params)
    {
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) new URL(params[0]).openConnection();
            /*
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            Log.d("Respuesta",response.toString());*/
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            Gson gson = new Gson();
            Result resultado = gson.fromJson(reader, Result.class);
            Log.d("print",resultado.photos.photo.toString());
            for (Photo p : resultado.photos.photo){
                Log.d("resultados", p.title);
            }
            //...
            return "Holasfsf";
        } catch (IOException e) {
            //e.printStackTrace();
            Log.e("ERROR",e.toString());
        }
        return "Error";
    }
    @Override
    protected void onPostExecute(String resultado)
    {
        if (mActivity == null)
            Log.i("MyAsyncTask", "Me salto onPostExecute() -- no hay nueva activity");
        else
            mActivity.setupAdapter(resultado);
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