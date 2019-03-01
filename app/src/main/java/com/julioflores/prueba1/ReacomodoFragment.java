package com.julioflores.prueba1;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class ReacomodoFragment extends Fragment {

    Main2Activity adaptadores;
    SwipeRefreshLayout swipere;
    ListView lista2;
    AsyncHttpClient cliente;
    EditText matepri_reacomodo;
    EditText ubicacion_reacomodo;
    String usuario;
    ArrayAdapter a;

    Handler customHandler = new Handler();
    public class contar extends CountDownTimer {
        public contar(long milienfuturo, long countdowninterval){
            super(milienfuturo,countdowninterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            if(matepri_reacomodo.getText().toString().isEmpty()){
                ObtenerAlmacen();
            }else{
                customHandler.removeCallbacks(actualizartimer);
                customHandler.removeCallbacks(actualizartimer);
            }
        }
        public void onFinish(){
            //Toast.makeText(getActivity(), "Actualizado",Toast.LENGTH_SHORT).show();
        }
    }
    private Runnable actualizartimer = new Runnable() {
        @Override
        public void run() {
            ReacomodoFragment.contar tiempo = new ReacomodoFragment.contar(5000, 5000);
            if(matepri_reacomodo.getText().toString().isEmpty()) {
                tiempo.start();
                customHandler.postDelayed(this, 1000);
            }else{
                tiempo.cancel();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View observar = inflater.inflate(R.layout.fragment_reacomodo, container, false);
        lista2 = (ListView) observar.findViewById(R.id.listareacomodo);

        matepri_reacomodo = (EditText) observar.findViewById(R.id.matprireacomodo);
        ubicacion_reacomodo = (EditText) observar.findViewById(R.id.ubicacion_reacomodo);
        customHandler.postDelayed(actualizartimer, 1000);
        swipere = observar.findViewById(R.id.swipereacomodar);
        cliente = new AsyncHttpClient();

        //usuario = "Javier Belausteguigoitia";
        //usuario = "Danya López";
        //usuario = "Tablet";
        //usuario = "Edgar Cruz";
        usuario = "Juan Antonio Muñoz";
        //usuario = "Edgar Gallardo";

        ObtenerAlmacen();
        return observar;
    }

    private void ObtenerAlmacen(){
        if (matepri_reacomodo.getText().toString().isEmpty()) {
            String url = "https://appsionmovil.000webhostapp.com/Almacenmp.php";
            cliente.post(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (statusCode == 200) { listaralmacen(new String(responseBody)); }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) { }
            });
        }else{
            CharSequence buscar;
            String buscar1 = matepri_reacomodo.getText().getFilters().toString();
            //Toast.makeText(getActivity(), buscar, Toast.LENGTH_SHORT).show();
            String url = "https://appsionmovil.000webhostapp.com/AlmacenMP_consultar.php?MateriaPrima="+ buscar1;
            cliente.post(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (statusCode == 200) { listaralmacen(new String(responseBody)); }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) { }
            });
        }
    }

    private void ObtenerAlmacen2(){

        String buscar2 = matepri_reacomodo.getText().toString();
        //Toast.makeText(getActivity(), buscar, Toast.LENGTH_SHORT).show();
        String url = "https://appsionmovil.000webhostapp.com/AlmacenMP_consultar2.php?MateriaPrima="+ buscar2;
        //Toast.makeText(getActivity(), url, Toast.LENGTH_SHORT).show();

        cliente.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) { listaralmacen(new String(responseBody)); }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) { }
        });

    }

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
                t.setCantidad(jsonarreglo.getJSONObject(i).getDouble("Cantidad"));
                t.setPersona(jsonarreglo.getJSONObject(i).getString("Persona"));
                t.setObservaciones(jsonarreglo.getJSONObject(i).getString("Observaciones"));
                t.setFechahora(jsonarreglo.getJSONObject(i).getString("FechayHora"));
                lista.add(t);
            }
            adaptadores = new Main2Activity(getActivity(), lista);
            lista2.setAdapter(adaptadores);
            matepri_reacomodo.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adaptadores.getFilter().filter(s);
                }
                @Override
                public void afterTextChanged(Editable s) { }
            });
            swipere.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    ObtenerAlmacen2();
                    ConnectivityManager conectividad1 = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo lanets = conectividad1.getActiveNetworkInfo();
                    if(lanets != null && lanets.isConnected()) {
                        matepri_reacomodo.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                adaptadores.getFilter().filter(s);
                            }
                            @Override
                            public void afterTextChanged(Editable s) { }
                        });
                        swipere.setRefreshing(false);
                    }else{
                        Toast.makeText(getActivity(), "No hay Internet, intentarlo más tarde o verifica su conexión",Toast.LENGTH_SHORT).show();
                        swipere.setRefreshing(false);
                    }
                }
            });
            lista2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final Almacen valor1 = (Almacen) lista2.getItemAtPosition(position);
                    AlertDialog.Builder mibuild = new AlertDialog.Builder(getActivity());
                    final View mview = getLayoutInflater().inflate(R.layout.dialogo_reacomodo, null);
                    mibuild.setTitle("Nuevo acomodo");

                    final String rak = String.valueOf(valor1.getRack());
                    final String fil = String.valueOf(valor1.getFila());
                    final String col = String.valueOf(valor1.getColumna());
                    final String mp = valor1.getMateriaprima();
                    final String lmp = String.valueOf(valor1.getLotemp());
                    final Double primervalor = valor1.getCantidad();
                    final String per = valor1.getPersona();
                    final String cprod = "Reacomodo";
                    final String val3 = String.valueOf(primervalor);
                    final String valor = String.valueOf(primervalor);
                    final EditText op1 = (EditText) mview.findViewById(R.id.cantidad_reacomodo);
                    op1.setText(valor);
                    if (primervalor == 0) {
                        Toast.makeText(getActivity(), "Esta Operación se ha terminado", Toast.LENGTH_SHORT).show();
                    } else {
                        final AlertDialog.Builder builder = mibuild.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String op11 = op1.getText().toString();
                                op1.setText(op11);
                                EditText op2 = (EditText) mview.findViewById(R.id.ubicacion_reacomodo);
                                String ubibacion_nueva = op2.getText().toString();
                                String[] ubicacion = ubibacion_nueva.split("-");
                                String rack_nuevo = ubicacion[0];
                                String fila_nueva = ubicacion[1];
                                String columna_nueva = ubicacion[2];
                                Date fechahora = Calendar.getInstance().getTime();
                                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                                String dias = dateFormat.format(fechahora);
                                String val1 = op1.getText().toString();
                                Double valor_negativo = Double.parseDouble(val1)*-1;
                                String valor_neg_string = valor_negativo.toString();
                                Double val2 = Double.parseDouble(val1);
                                Double restar = primervalor - val2;
                                String total = String.valueOf(restar);
                                if (val2 == 0) {
                                    Toast.makeText(getActivity(), "Es un valor nulo", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                } else if (val2 <= primervalor) {
                                    String url = "https://appsionmovil.000webhostapp.com/AlmacenMP_reacomodo.php?Rack=" + rak +
                                            "&Fila=" + fil + "&Columna="+ col +"&MateriaPrima="+ mp.replaceAll(" ","%20")+
                                            "&LoteMP="+ lmp +"&Cantidad="+ valor_negativo +"&Persona="+ usuario.replaceAll(" ", "%20")+
                                            "&Observaciones="+ cprod.replaceAll(" ","%20")+
                                            "&FechayHora="+ dias.replaceAll(" ","%20")+"&RackNuevo="+rack_nuevo+"&FilaNueva="+fila_nueva+"&ColumnaNueva="+columna_nueva+"&CantidadNueva="+val1;
                                    cliente.post(url, new AsyncHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) { if (statusCode == 200) { } }
                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) { }
                                    });
                                    ObtenerAlmacen2();
                                    String signorestar = " - ";
                                    String signoigual = " = ";
                                    String url2 = "https://appsionmovil.000webhostapp.com/Almacendatoshistorico.php?MateriaPrima=" + mp +
                                            "&OperacionHistorico=" + val3 + signorestar.replaceAll(" ","%20") +
                                            val1 + signoigual.replaceAll(" ","%20") + total +
                                            "&FechayHora="+dias.replaceAll(" ","%20");
                                    cliente.post(url2, new AsyncHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) { if (statusCode == 200) {
                                            ObtenerAlmacen2();
                                        } }
                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                            Toast.makeText(getActivity(),"Error, no se guardaron los datos",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                } else {
                                    Toast.makeText(getActivity(), "Favor de Ingresar una cantidad menor", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        mibuild.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) { dialog.cancel(); }
                        });
                        mibuild.setView(mview);
                        AlertDialog dialog = mibuild.create();
                        dialog.show();
                    }
                }
            });
        }catch (Exception e1){
            e1.printStackTrace();
        }
    }
}