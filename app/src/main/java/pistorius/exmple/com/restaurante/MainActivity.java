package pistorius.exmple.com.restaurante;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView list;
    private Button carrito;
    private Button historial;
    private ArrayList<String> pedidos = new ArrayList<>();
    private ArrayList<Integer> imagen = new ArrayList<>();
    private ArrayList<Integer> cantidad = new ArrayList<>();
    private ArrayList<String> h_pedidos = new ArrayList<>();
    private ArrayList<Integer> h_imagen = new ArrayList<>();
    private ArrayList<Integer> h_cantidad = new ArrayList<>();
    private int car = 0;

    private BroadcastReceiver breceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context arg0, Intent intent) {
            car = 1;
            Toast.makeText(getApplicationContext(), "Bateria baja, Guardaremos el carrito actual", Toast.LENGTH_LONG).show();

        }
    };

    private String[] platillos = {
            "Postre Bombon \t $12",
            "Brocheta \t $15",
            "Cafe \t $8",
            "Cupcake \t $10",
            "Desayuno de la casa\t $20",
            "Flan \t $10",
            "Hamburguesa \t $15",
            "Italiana \t $10",
            "Tostada y mermelada\t $10",
            "Nieve \t $10",
            "Paleta \t $8",
            "Palomtas \t $12",
            "Papas Francesas\t $15",
            "Pastel \t $10",
            "Pastelillo \t $10",
            "Pastelillo \t $10",
            "Postre Pepino\t $12",
            "Postre de la Casa\t $16",
            "Rollito \t $11",
            "Sushi \t $18",
            "Sushi Dulce \t $15",
            "Te \t $8",
            "Yoyo \t $8"
    } ;

    private Integer[] imageId = {
            R.drawable.bombon,
            R.drawable.brocheta,
            R.drawable.cafe,
            R.drawable.cupcake,
            R.drawable.desalluno,
            R.drawable.flan,
            R.drawable.hamburguesa,
            R.drawable.italiana,
            R.drawable.mermelada,
            R.drawable.nieve,
            R.drawable.paleta,
            R.drawable.palomitas,
            R.drawable.papas_fritas,
            R.drawable.pastel,
            R.drawable.pastelillo,
            R.drawable.pastelillo_fresa,
            R.drawable.pepino,
            R.drawable.postre,
            R.drawable.rollito,
            R.drawable.sushi,
            R.drawable.sushi_dulce,
            R.drawable.te,
            R.drawable.yoyo
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.registerReceiver(this.breceiver, new IntentFilter(Intent.ACTION_BATTERY_LOW));

        carrito = (Button) findViewById(R.id.charge);
        carrito.setOnClickListener((View.OnClickListener) this);
        historial = (Button) findViewById(R.id.history);
        historial.setOnClickListener((View.OnClickListener) this);

        CustomList adapter = new
                CustomList(MainActivity.this, platillos, imageId);
        list = (ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Cantidad");
                final EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);
                final int index = position;

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pedidos.add(platillos[+ index]);
                        imagen.add(imageId[+ index]);
                        cantidad.add(Integer.parseInt(input.getText().toString()));
                        //String m_Text = input.getText().toString();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putStringArrayList("pedidos", pedidos);
        savedInstanceState.putIntegerArrayList("cantidad", cantidad);
        savedInstanceState.putIntegerArrayList("imagen", imagen);
        savedInstanceState.putStringArrayList("h_pedidos", h_pedidos);
        savedInstanceState.putIntegerArrayList("h_cantidad", h_cantidad);
        savedInstanceState.putIntegerArrayList("h_imagen", h_imagen);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        cantidad = savedInstanceState.getIntegerArrayList("cantidad");
        imagen = savedInstanceState.getIntegerArrayList("imagen");
        pedidos = savedInstanceState.getStringArrayList("pedidos");
        h_cantidad = savedInstanceState.getIntegerArrayList("h_cantidad");
        h_imagen = savedInstanceState.getIntegerArrayList("h_imagen");
        h_pedidos = savedInstanceState.getStringArrayList("h_pedidos");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            if (data.hasExtra("result")) {
                if (data.getExtras().getString("result").equals("ok")) {
                    SharedPreferences sp = getSharedPreferences("preferencias", Context.MODE_PRIVATE);;
                    final SharedPreferences.Editor edTemp = sp.edit();
                    //edTemp.putStringSet(new HashSet<String>(h_pedidos));
                    Set<String> p = new HashSet<String>(pedidos);
                    //Set<String> c = new HashSet<String>(cantidad);
                    h_imagen = new ArrayList<>(imagen);
                    pedidos.clear();
                    cantidad.clear();
                    imagen.clear();
                }
            }
        }
    }

    public void ver_carrito () {
        Intent intent = new Intent(this, Carrito.class);
        intent.putExtra("pedidos", pedidos);
        intent.putExtra("imagen", imagen);
        intent.putExtra("cantidad", cantidad);
        startActivityForResult(intent, 1);
    }

    public void ver_historial() {
        Intent intent = new Intent(this, Historial.class);
        intent.putExtra("pedidos", h_pedidos);
        intent.putExtra("imagen", h_imagen);
        intent.putExtra("cantidad", h_cantidad);
        startActivityForResult(intent, 2);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.charge:
                ver_carrito();
                break;

            case R.id.history:
                ver_historial();
                break;

            default:

        }
    }

}

/*

            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(getApplicationContext(), android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(getApplicationContext());
            }
            builder.setTitle("Alerta")
                    .setMessage("Bateria baja, el carrito actual se guardara")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

 */