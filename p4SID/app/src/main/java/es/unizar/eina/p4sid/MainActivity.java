package es.unizar.eina.p4sid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
public class MainActivity extends AppCompatActivity
{
    private final static String TAG = "MainActivity";
    private MyAsyncTask myTask = null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        // Botón para añadir producto
        ImageButton botonAnadir = (ImageButton) findViewById(R.id.search);
        botonAnadir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                llamarATarea();
            }
        });
    }

    public void llamarATarea (){
        // Para obtener el valor devuelto en onRetainCustomNonConfigurationInstance
        myTask = (MyAsyncTask) getLastCustomNonConfigurationInstance();
        if (myTask == null) {
            // Evita crear una AsyncTask cada vez que, por ejemplo, hay una rotación
            Log.i(TAG, "onCreate: About to create MyAsyncTask");
            myTask = new MyAsyncTask(this);
            myTask.execute("https://www.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=bf75df07beefce33b3099fb9092a485f&extras=description%2Ctags&format=json&nojsoncallback=1");
        }
        else
            myTask.attach(this);
        Toast.makeText(this, "Hola!", Toast.LENGTH_LONG).show();
    }

    /** Permite devolver un objeto y que persista entre cambios de configuración. Lo
     invoca el sistema cuando se va a destruir una actividad y se sabe que se va a
     crear otra nueva inmediatamente. Se llama entre onStop y onDestroy. */
    @Override
    public Object onRetainCustomNonConfigurationInstance()
    {
        // Además de devolver mi tarea, elimino la referencia en mActivity
        myTask.detach();
        // Devuelvo mi tarea, para que no se cree de nuevo cada vez
        return myTask;
    }
    public void setupAdapter(String resultado)
    {
        if (!resultado.isEmpty())
            Toast.makeText(MainActivity.this,
                    "Codigo de respuesta: " + resultado, Toast.LENGTH_LONG).show();
    }
}
