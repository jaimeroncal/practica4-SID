package es.unizar.eina.p4sid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.MatrixCursor;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private final static String TAG = "MainActivity";
    private MyAsyncTask myTask = null;
    // PARA LA LISTA DE PEDIDOS
    private ListView mList;
    private TextView mText;
    private List<MyAsyncTask.Photo> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        // Para rellenar la lista de elementos
        mList = (ListView)findViewById(R.id.list);
        mText = (TextView)findViewById(R.id.empty);

        // Botón para añadir producto
        ImageButton botonAnadir = (ImageButton) findViewById(R.id.search);
        botonAnadir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                llamarATarea();
            }
        });

        // AL CLICKAR EN UN ELEMENTO DE LA LISTA
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // SE LLAMA A LA SEGUNDA ACTIVIDAD
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                // CON PARAMETROS
                intent.putExtra("title",lista.get(i).title);
                intent.putExtra("description",lista.get(i).description._content);
                intent.putExtra("tags",lista.get(i).tags);
                intent.putExtra("url",lista.get(i).url_h);
                startActivity(intent);
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
            myTask.execute("https://www.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=bf75df07beefce33b3099fb9092a485f&extras=description,url_h%2Ctags&format=json&nojsoncallback=1");
        }
        else
            myTask.attach(this);
        Toast.makeText(this, "Buscando en Flickr", Toast.LENGTH_LONG).show();
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
    public void setupAdapter(List<MyAsyncTask.Photo> list)
    {
        fillData(list);
    }
    private void fillData(List<MyAsyncTask.Photo> list) {

        // CREANDO CURSOR CON LOS RESULTADOS
        String[] columns = new String[] { "_id", "owner", "secret", "server", "farm",
                "title", "ispublic", "isfriend", "isfamily", "description", "tags", "url_h" };
        MatrixCursor matrixCursor= new MatrixCursor(columns);

        int i=0;
        for(MyAsyncTask.Photo p : list){
            matrixCursor.addRow(new Object[]{i,p.owner,  p.secret, p.server, p.farm,
                    p.title, p.ispublic, p.isfriend, p.isfamily, p.description._content, p.tags, p.url_h});
        }

        // SE GUARDA LA LISTA
        lista = list;

        // PARSEANDO CURSOR A LISTVIEW
        String[] from = new String[] { "title", "description", "url_h" };
        int[] to = new int[] { R.id.titulo, R.id.descripcion, R.id.enlace };

        SimpleCursorAdapter photos =
                new SimpleCursorAdapter(this, R.layout.flickr_row, matrixCursor, from, to);
        mList.setAdapter(photos);

        // SI NO HAY FOTOS SE MUESTRA MENSAJE DE VACÍO
        if (photos.isEmpty()){
            mText.setText("No se ha encontrado ninguna imagen.");
        } else {
            mText.setVisibility(View.INVISIBLE);
        }
    }
}
