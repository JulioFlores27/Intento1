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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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

public class ConteoFragment extends Fragment {

        Main2Activity adaptadores;
        SwipeRefreshLayout swipere;
        ListView lista2;
        AsyncHttpClient cliente;
        Button boton1, boton2, btn_checar_ubicacion;
        EditText matepri2;
        EditText ubicacion;
        EditText lote, cantidad;
        String usuario;

        Handler customHandler = new Handler();
        public class contar extends CountDownTimer {
            public contar(long milienfuturo, long countdowninterval){
                super(milienfuturo,countdowninterval);
            }
            @Override
            public void onTick(long millisUntilFinished) {
                if(matepri2.getText().toString().isEmpty()){
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
                contar tiempo = new contar(5000, 5000);
                if(matepri2.getText().toString().isEmpty()) {
                    tiempo.start();
                    customHandler.postDelayed(this, 1000);
                }else{
                    tiempo.cancel();
                }
            }
        };

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View conteo = inflater.inflate(R.layout.fragment_conteo, container, false);
            lista2 = (ListView) conteo.findViewById(R.id.listaconteo);
            boton1 = (Button) conteo.findViewById(R.id.b12conteo);
            boton2 = (Button) conteo.findViewById(R.id.b22conteo);
            customHandler.postDelayed(actualizartimer, 1000);
            //TODO decidir si se queda o se va el botón de checar
            btn_checar_ubicacion = (Button) conteo.findViewById(R.id.btn_checar_ubicacion);
            btn_checar_ubicacion.setVisibility(View.INVISIBLE);

            matepri2 = (EditText) conteo.findViewById(R.id.matpri3);
            ubicacion = (EditText) conteo.findViewById(R.id.ubicacion_conteo);
            lote = (EditText) conteo.findViewById(R.id.lote3);
            cantidad = (EditText) conteo.findViewById(R.id.cantidad_conteo);
            swipere = conteo.findViewById(R.id.swiperefrescar);
            cliente = new AsyncHttpClient();
            //usuario = "Javier Belausteguigoitia";
            //usuario = "Danya López";
            //usuario = "Tablet";
            //usuario = "Edgar Cruz";
            usuario = "Juan Antonio Muñoz";
            //usuario = "Edgar Gallardo";
            ObtenerAlmacen();
            return conteo;
        }

        private void ObtenerAlmacen(){
            if (matepri2.getText().toString().isEmpty()) {
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
                String buscar1 = matepri2.getText().getFilters().toString();
                //Toast.makeText(getActivity(), buscar, Toast.LENGTH_SHORT).show();
                String url = "https://appsionmovil.000webhostapp.com/AlmacenMP_consultar.php?MateriaPrima="+ buscar1;
                cliente.post(url, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        if (statusCode == 200) { listaralmacen(new String(responseBody));}
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) { }
                });
            }
        }

        private void ObtenerAlmacen3(){

            String buscar2 = matepri2.getText().toString();
            String ubicacion_array [] = ubicacion.getText().toString().split("-");
            String rack = ubicacion_array [0];
            String fila = ubicacion_array [1];
            String columna = ubicacion_array [2];

            //Toast.makeText(getActivity(), buscar, Toast.LENGTH_SHORT).show();
            String url = "https://appsionmovil.000webhostapp.com/AlmacenMP_consultar3.php?MateriaPrima="+ buscar2+"&rack="+rack+"&fila="+fila +"&columna="+columna;
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

        private void ObtenerAlmacenes(){
            String obs = " ";
            Date fechahora = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            String dias = dateFormat.format(fechahora);
            String currentString = ubicacion.getText().toString();
            String[] separated = currentString.split("-");
            String a = separated[0];
            String b = separated[1];
            String c = separated[2];
            String url = "https://appsionmovil.000webhostapp.com/AlmacenMP_insertar.php?Rack="+ a.replaceAll(" ", "%20") +
                    "&Fila="+ b.replaceAll(" ", "%20") +
                    "&Columna="+ c.replaceAll(" ", "%20") +
                    "&MateriaPrima="+ matepri2.getText().toString().replaceAll(" ", "%20") +
                    "&LoteMP="+ lote.getText().toString().replaceAll(" ", "%20") +
                    "&Cantidad="+ cantidad.getText().toString().replaceAll(" ", "%20") +
                    "&Persona=" + usuario.replaceAll(" ", "%20") +
                    "&Observaciones=" + obs.replaceAll(" ", "%20") +
                    "&FechayHora=" + dias.replaceAll(" ", "%20");
            Toast.makeText(getActivity(), "Sus datos se han guardado",Toast.LENGTH_SHORT).show();
            cliente.post(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) { if(statusCode == 200){ } }
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
                matepri2.addTextChangedListener(new TextWatcher() {
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
                        if (ubicacion.getText().toString().isEmpty()) {
                            ObtenerAlmacen();
                            swipere.setRefreshing(false);
                        } else {
                            if (ubicacion.getText().toString().matches(".-.-.") || ubicacion.getText().toString().matches("..-.-.") || ubicacion.getText().toString().matches(".-.-..")|| ubicacion.getText().toString().matches("..-.-..")) {
                                ObtenerAlmacen3();
                                ConnectivityManager conectividad1 = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo lanets = conectividad1.getActiveNetworkInfo();
                                if (lanets != null && lanets.isConnected()) {
                                    matepri2.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                        }
                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                            adaptadores.getFilter().filter(s);
                                        }
                                        @Override
                                        public void afterTextChanged(Editable s) {
                                        }
                                    });

                                    boton1.setVisibility(View.VISIBLE);
                                    boton2.setVisibility(View.VISIBLE);
                                    swipere.setRefreshing(false);
                                } else {
                                    Toast.makeText(getActivity(), "No hay Internet, intentarlo más tarde o verifica su conexión", Toast.LENGTH_SHORT).show();
                                    boton1.setVisibility(View.INVISIBLE);
                                    boton2.setVisibility(View.INVISIBLE);
                                    swipere.setRefreshing(false);
                                }
                                swipere.setRefreshing(false);
                            } else {
                                Toast.makeText(getActivity(), "Revisar ubicación", Toast.LENGTH_LONG).show();
                                swipere.setRefreshing(false);
                            }
                        }
                        swipere.setRefreshing(false);
                    }
                });

                boton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(matepri2.getText().toString().isEmpty() || lote.getText().toString().isEmpty() || cantidad.getText().toString().isEmpty() || ubicacion.getText().toString().isEmpty()){
                            Toast.makeText(getActivity(), "Favor de ingresar datos",Toast.LENGTH_SHORT).show();
                        }

                        if (matepri2.getText().toString().length() <5 || matepri2.getText().toString().length() > 25){
                            Toast.makeText(getActivity(), "Revisar MP",Toast.LENGTH_SHORT).show();

                        }

                        if (lote.getText().toString().length() <5 || lote.getText().toString().length() > 10){
                            Toast.makeText(getActivity(), "Revisar lote",Toast.LENGTH_SHORT).show();

                        }



                        if ((lote.getText().toString().length() == 10 || lote.getText().toString().length() == 5) && (matepri2.getText().toString().length() ==5 || matepri2.getText().toString().length() >21 ) && (ubicacion.getText().toString().matches(".-.-.")||ubicacion.getText().toString().matches("..-.-.")||ubicacion.getText().toString().matches(".-.-..")||ubicacion.getText().toString().matches("..-.-.."))){

                            ObtenerAlmacenes();
                            matepri2.setText("");
                            lote.setText("");
                            cantidad.setText("");
                            ObtenerAlmacen3();

                        } else {
                            Toast.makeText(getActivity(), "Revisar ubicación",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                ubicacion.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {

                        if (keyCode == KeyEvent.KEYCODE_ENTER){

                            if (ubicacion.getText().toString().isEmpty()) {
                                ObtenerAlmacen();
                                swipere.setRefreshing(false);
                            } else {

                                if (ubicacion.getText().toString().matches(".-.-.") || ubicacion.getText().toString().matches("..-.-.") || ubicacion.getText().toString().matches(".-.-..")|| ubicacion.getText().toString().matches("..-.-..")) {

                                    ObtenerAlmacen3();
                                    ConnectivityManager conectividad1 = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                                    NetworkInfo lanets = conectividad1.getActiveNetworkInfo();
                                    if (lanets != null && lanets.isConnected()) {
                                        matepri2.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                            }

                                            @Override
                                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                adaptadores.getFilter().filter(s);
                                            }

                                            @Override
                                            public void afterTextChanged(Editable s) {
                                            }
                                        });

                                        boton1.setVisibility(View.VISIBLE);
                                        boton2.setVisibility(View.VISIBLE);
                                        swipere.setRefreshing(false);
                                    } else {
                                        Toast.makeText(getActivity(), "No hay Internet, intentarlo más tarde o verifica su conexión", Toast.LENGTH_SHORT).show();
                                        boton1.setVisibility(View.INVISIBLE);
                                        boton2.setVisibility(View.INVISIBLE);
                                        swipere.setRefreshing(false);
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Revisar ubicación", Toast.LENGTH_LONG).show();
                                    swipere.setRefreshing(false);
                                }
                            }

                        }

                        return false;
                    }
                });


                btn_checar_ubicacion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (ubicacion.getText().toString().isEmpty()) {
                            ObtenerAlmacen();
                            swipere.setRefreshing(false);
                        } else {

                            if (ubicacion.getText().toString().matches(".-.-.") || ubicacion.getText().toString().matches("..-.-.") || ubicacion.getText().toString().matches(".-.-..")|| ubicacion.getText().toString().matches("..-.-..")) {

                                ObtenerAlmacen3();
                                ConnectivityManager conectividad1 = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo lanets = conectividad1.getActiveNetworkInfo();
                                if (lanets != null && lanets.isConnected()) {
                                    matepri2.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                        }

                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                            adaptadores.getFilter().filter(s);
                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {
                                        }
                                    });

                                    boton1.setVisibility(View.VISIBLE);
                                    boton2.setVisibility(View.VISIBLE);
                                    swipere.setRefreshing(false);
                                } else {
                                    Toast.makeText(getActivity(), "No hay Internet, intentarlo más tarde o verifica su conexión", Toast.LENGTH_SHORT).show();
                                    boton1.setVisibility(View.INVISIBLE);
                                    boton2.setVisibility(View.INVISIBLE);
                                    swipere.setRefreshing(false);
                                }
                            } else {
                                Toast.makeText(getActivity(), "Revisar ubicación", Toast.LENGTH_LONG).show();
                                swipere.setRefreshing(false);
                            }
                        }


                    }
                });

                lista2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final Almacen valor1 = (Almacen) lista2.getItemAtPosition(position);
                        AlertDialog.Builder mibuild = new AlertDialog.Builder(getActivity());
                        final View mview = getLayoutInflater().inflate(R.layout.operacion, null);
                        mibuild.setTitle("Cantidades Restantes");
                        mibuild.setMessage("Surtido:");
                        final String rak = String.valueOf(valor1.getRack());
                        final String fil = String.valueOf(valor1.getFila());
                        final String col = String.valueOf(valor1.getColumna());
                        final String mp = valor1.getMateriaprima();
                        final String lmp = String.valueOf(valor1.getLotemp());
                        final Double primervalor = valor1.getCantidad();
                        final String per = valor1.getPersona();
                        final String cprod = ubicacion.getText().toString();
                        final String val3 = String.valueOf(primervalor);
                        if (primervalor == 0) {
                            Toast.makeText(getActivity(), "Esta Operación se ha terminado", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            final AlertDialog.Builder builder = mibuild.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    EditText op1 = (EditText) mview.findViewById(R.id.operacion1);
                                    Date fechahora = Calendar.getInstance().getTime();
                                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                                    String dias = dateFormat.format(fechahora);
                                    String val1 = op1.getText().toString();
                                    Double val2 = Double.parseDouble(val1);
                                    Double restar = primervalor - val2;
                                    String total = String.valueOf(restar);
                                    if (val2 == 0) {
                                        Toast.makeText(getActivity(), "Es un valor nulo", Toast.LENGTH_SHORT).show();
                                        dialog.cancel();
                                    } else if (val2 <= primervalor) {
                                        String url = "https://appsionmovil.000webhostapp.com/AlmacenMP_CantidadOperacion.php?Rack=" + rak +
                                                "&Fila=" + fil + "&Columna="+ col +"&MateriaPrima="+ mp.replaceAll(" ","%20")+
                                                "&LoteMP="+ lmp +"&Cantidad=-"+ val1 +"&Persona="+ usuario.replaceAll(" ", "%20")+
                                                "&Observaciones="+ cprod.replaceAll(" ","%20")+
                                                "&FechayHora="+ dias.replaceAll(" ","%20");
                                        cliente.post(url, new AsyncHttpResponseHandler() {
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) { if (statusCode == 200) { } }
                                            @Override
                                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) { }
                                        });
                                        ObtenerAlmacen3();
                                        String signorestar = " - ";
                                        String signoigual = " = ";
                                        String url2 = "https://appsionmovil.000webhostapp.com/Almacendatoshistorico.php?MateriaPrima=" + mp +
                                                "&OperacionHistorico=" + val3 + signorestar.replaceAll(" ","%20") +
                                                val1 + signoigual.replaceAll(" ","%20") + total +
                                                "&FechayHora="+dias.replaceAll(" ","%20");
                                        cliente.post(url2, new AsyncHttpResponseHandler() {
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) { if (statusCode == 200) {
                                                ObtenerAlmacen3();
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
