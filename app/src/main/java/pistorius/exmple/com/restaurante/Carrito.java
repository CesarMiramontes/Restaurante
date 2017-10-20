package pistorius.exmple.com.restaurante;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by pistorius on 16/09/2017.
 */

public class Carrito extends AppCompatActivity implements View.OnClickListener {

    private ListView list;
    private ArrayList<String> pedidos = new ArrayList<>();
    private ArrayList<Integer> imagen = new ArrayList<>();
    private ArrayList<Integer> cantidad = new ArrayList<>();
    private Button button;
    private String message = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carrito);
        button = (Button) findViewById(R.id.pay);
        button.setOnClickListener((View.OnClickListener) this);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        pedidos = intent.getStringArrayListExtra("pedidos");
        imagen = extras.getIntegerArrayList("imagen");
        cantidad = extras.getIntegerArrayList("cantidad");

        String platillos[] = new String[imagen.size()];
        Integer imageId[] = new Integer[imagen.size()];

        for(int i = 0; i < imagen.size(); i++){
            platillos[i] = pedidos.get(i) + " " + cantidad.get(i);
            imageId[i] = imagen.get(i);
        }

        CustomList adapter = new
                CustomList(this, platillos, imageId);
        list = (ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("result", message);
        setResult(RESULT_OK, data);
        super.finish();
    }

    @Override
    public void onClick(View view){
        message = "ok";
        Toast.makeText(getApplicationContext(), "La Cuenta ha sido paga", Toast.LENGTH_LONG).show();
    }

}
