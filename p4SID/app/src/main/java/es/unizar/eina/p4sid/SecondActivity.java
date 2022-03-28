package es.unizar.eina.p4sid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    private final static String TAG = "SecondActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_second);

        // LEEMOS TITULO
        TextView mTextTitle = (TextView)findViewById(R.id.titulo2);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        mTextTitle.setText(title);

        // LEEMOS DESCRIPCIÓN
        TextView mTextDescripcion = (TextView)findViewById(R.id.descripcion2);
        String description = intent.getStringExtra("description");
        mTextDescripcion.setText(description);

        // LEEMOS TAGS
        TextView mTextTags = (TextView)findViewById(R.id.tags2);
        String tags = intent.getStringExtra("tags");
        mTextTags.setText(tags);

        // LEEMOS URL y LLAMAMOS A TAREA PARA PEDIR IMAGEN
        ImageView mImage = (ImageView)findViewById(R.id.imagen2);
        String url = intent.getStringExtra("url");
        if (url == null || url.isEmpty()){
            TextView mNoImage = (TextView)findViewById(R.id.noimage);
            mNoImage.setVisibility(View.VISIBLE);
        } else {
            DownloadImageAsyncTask myTask = new DownloadImageAsyncTask(mImage);
            myTask.execute(url);
        }
    }
}
