package com.julioflores.prueba1;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.json.JSONArray;
import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;

public class SalidasFragment extends Fragment {
    Main2Activity adaptadores;
    SwipeRefreshLayout swipere;
    ListView lista2;
    AsyncHttpClient cliente;
    Button boton1, boton2, boton3;
    EditText matepri2,op1;
    public SalidasFragment(){
    }
    ArrayAdapter a;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View observar = inflater.inflate(R.layout.fragment_salidas, container, false);
        lista2 = (ListView) observar.findViewById(R.id.listasa);
        boton1 = (Button) observar.findViewById(R.id.b12);
        boton2 = (Button) observar.findViewById(R.id.b22);
        boton3 = (Button) observar.findViewById(R.id.b32);
        matepri2 = (EditText) observar.findViewById(R.id.matpri2);
        op1 = (EditText) observar.findViewById(R.id.operacion1);
        swipere = observar.findViewById(R.id.swiperefrescar);
        cliente = new AsyncHttpClient();
        ObtenerAlmacen();
        swipere.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
           public void onRefresh() {
                ConnectivityManager conectividad1 = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo lanets = conectividad1.getActiveNetworkInfo();
                if(lanets != null && lanets.isConnected()) {
                    ObtenerAlmacen();
                    boton1.setVisibility(View.VISIBLE);
                    boton2.setVisibility(View.VISIBLE);
                    swipere.setRefreshing(false);
                }else{
                    Toast.makeText(getActivity(), "No hay Internet, intentarlo más tarde o verifica su conexión",Toast.LENGTH_SHORT).show();
                    boton1.setVisibility(View.INVISIBLE);
                    boton2.setVisibility(View.INVISIBLE);
                    swipere.setRefreshing(false);
                }
            }
        });
        boton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bus = matepri2.getText().toString().replaceAll(" ", "%20") ;
                String url = "https://appsionmovil.000webhostapp.com/AlmacenMP_consultar.php?MateriaPrima="+ bus;
                cliente.post(url, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        if(statusCode == 200){
                            listaralmacen(new String(responseBody));
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) { }
                });
            }
        });
        return observar;
    }

    private void ObtenerAlmacen(){
        String url = "https://appsionmovil.000webhostapp.com/Almacenmp.php";
        cliente.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    listaralmacen(new String(responseBody));
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) { }
        });
    }
    ArrayAdapter<Almacen> hol;
    public void listaralmacen(String respuesta) {
        final ArrayList<Almacen> lista = new ArrayList<Almacen>();
        try{
            final JSONArray jsonarreglo = new JSONArray(respuesta);
            for (int i=0; i<jsonarreglo.length(); i++){
                Almacen t = new Almacen();
                t.setId(jsonarreglo.getJSONObject(i).getInt("ID"));
                t.setRack(jsonarreglo.getJSONObject(i).getInt("Rack"));
                t.setFila(jsonarreglo.getJSONObject(i).getInt("Fila"));
                t.setColumna(jsonarreglo.getJSONObject(i).getInt("Columna"));
                t.setMateriaprima(jsonarreglo.getJSONObject(i).getString("MateriaPrima"));
                t.setLotemp(jsonarreglo.getJSONObject(i).getInt("LoteMP"));
                t.setCantidad(jsonarreglo.getJSONObject(i).getInt("Cantidad"));
                t.setPersona(jsonarreglo.getJSONObject(i).getString("Persona"));
                t.setObservaciones(jsonarreglo.getJSONObject(i).getString("Observaciones"));
                t.setFechahora(jsonarreglo.getJSONObject(i).getString("FechayHora"));
                lista.add(t);
            }
            //final ArrayAdapter<Almacen> a = new ArrayAdapter<Almacen>(getActivity(),android.R.layout.simple_list_item_1, lista);
            //lista2.setAdapter(a);
            adaptadores = new Main2Activity(getActivity(), lista);
            lista2.setAdapter(adaptadores);
            lista2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final Almacen valor1 = (Almacen) lista2.getItemAtPosition(position);
                    AlertDialog.Builder mibuild = new AlertDialog.Builder(getActivity());
                    View mview = getLayoutInflater().inflate(R.layout.operacion, null);
                    mibuild.setTitle("Cantidades Restantes");
                    mibuild.setMessage("Ingrese Valor");
                    final String ide1 = String.valueOf(valor1.getId());
                    final int primervalor = valor1.getCantidad();
                    String val3 = String.valueOf(primervalor);
                    Toast.makeText(getActivity(), val3,Toast.LENGTH_SHORT).show();
                    final AlertDialog.Builder builder = mibuild.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String val1 = String.valueOf(op1.getText().toString());
                            //int val2 = Integer.parseInt(val1);
                            //int restar = primervalor - val2;
                            //String total = String.valueOf(restar);
                            Toast.makeText(getActivity(), val1,Toast.LENGTH_SHORT).show();
//                            String url = "https://appsionmovil.000webhostapp.com/AlmacenMP_CantidadOperacion.php?Cantidad=" + total + "&ID=" + ide1;
//                            cliente.post(url, new AsyncHttpResponseHandler() {
//                                @Override
//                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) { if (statusCode == 200) { } }
//                                @Override
//                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) { }
//                            });
                        }
                    });
                    mibuild.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    mibuild.setView(mview);
                    AlertDialog dialog = mibuild.create();
                    dialog.show();
                }
            });
        }catch (Exception e1){
            e1.printStackTrace();
        }
    }
}