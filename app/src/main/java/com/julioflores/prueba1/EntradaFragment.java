package com.julioflores.prueba1;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import cz.msebera.android.httpclient.Header;

public class EntradaFragment extends Fragment {

    View v;
    EditText mp1, nl1, c1, u1;
    Button bu1, scanner;
    AsyncHttpClient cliente;
    String usuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_entrada, container, false);
        mp1 = v.findViewById(R.id.matpri1);
        nl1 = v.findViewById(R.id.lote1);
        c1 = v.findViewById(R.id.cantidad1);
        u1 = v.findViewById(R.id.ubicacion1);
        bu1 = v.findViewById(R.id.b1);
        scanner = v.findViewById(R.id.boton_scanner);
        cliente = new AsyncHttpClient();

        //usuario = "Javier Belausteguigoitia";
        //usuario = "Danya López";
        //usuario = "Tablet";
        //usuario = "Edgar Cruz";
        usuario = "Juan Antonio Muñoz";
        //usuario = "Edgar Gallardo";
        final Estructura_BBDD admin = new Estructura_BBDD(getActivity(), "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        ConnectivityManager conectividad = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo lanet = conectividad.getActiveNetworkInfo();
        if(lanet != null && lanet.isConnected()){
            ArrayList<String> ranking = new ArrayList<>();
            Cursor fila = bd.rawQuery("select *  from almacensqlite", null);
            if(fila.moveToFirst()) {
                do {
                    ranking.add(fila.getString(0) + " - " + fila.getString(1) + " - " +
                            fila.getString(2) + " - " + fila.getString(3)+ " - " + fila.getString(4) + " - "
                            + fila.getString(5) + " - " + fila.getString(6) + " - " + fila.getString(7) +
                            " - " + fila.getString(8));
                    String mpa1 = fila.getString(1);
                    String nla1 = fila.getString(2);
                    String ca1 = fila.getString(3);
                    String ra1 = fila.getString(4);
                    String fi1 = fila.getString(5);
                    String co1 = fila.getString(6);
                    String fechora1 = fila.getString(7);
                    String usras1 = fila.getString(8);
                    String ots = " ";
                    final String url = "https://appsionmovil.000webhostapp.com/AlmacenMP_insertar.php?Rack=" + ra1.replaceAll(" ", "%20") +
                            "&Fila=" + fi1.replaceAll(" ", "%20") +
                            "&Columna=" + co1.replaceAll(" ", "%20") +
                            "&MateriaPrima=" + mpa1.replaceAll(" ", "%20") +
                            "&LoteMP=" + nla1.replaceAll(" ", "%20") +
                            "&Envase=" + ots.replaceAll(" ", "%20") +
                            "&Cantidad=" + ca1.replaceAll(" ", "%20") +
                            "&Persona=" + usras1.replaceAll(" ", "%20") +
                            "&Observaciones=" + ots.replaceAll(" ", "%20") +
                            "&FechayHora=" + fechora1.replaceAll(" ", "%20");
                    //Toast.makeText(getActivity(), url,Toast.LENGTH_LONG).show();
                    cliente.post(url, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) { if (statusCode == 200) { } }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) { }
                    });
                    String dnis = fila.getString(0);
                    //Toast.makeText(getActivity(), dnis, Toast.LENGTH_LONG).show();
                    bd.delete("almacensqlite", "id=" + dnis, null);
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                } while (fila.moveToNext());
            }else{}
        }else{}

        bu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp1.getText().toString().isEmpty() || nl1.getText().toString().isEmpty() || c1.getText().toString().isEmpty() || u1.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Favor de ingresar datos",Toast.LENGTH_SHORT).show();
                }else if (mp1.getText().toString().length() <5 || mp1.getText().toString().length() > 25){
                    Toast.makeText(getActivity(), "Revisar MP",Toast.LENGTH_SHORT).show();
                }else if (nl1.getText().toString().length() <5 || nl1.getText().toString().length() > 10){
                    Toast.makeText(getActivity(), "Revisar lote",Toast.LENGTH_SHORT).show();
                }
                else {
                    ObtenerAlmacenes();
                }
            }
        });

        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mibuild = new AlertDialog.Builder(getActivity());
                final View mview = getLayoutInflater().inflate(R.layout.dialogo_escaneo, null);
                final EditText codigo = (EditText) mview.findViewById(R.id.codigo_escaneado);
                mibuild.setTitle("Escanea el codigo");
                mibuild.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String escaneo = codigo.getText().toString();
                        if (escaneo.length() > 23) {
                            String[] partes_codigo = codigo.getText().toString().split("-");
                            String mp_codigo = partes_codigo[0];
                            String lote_codigo = partes_codigo[3].trim();
                            mp1.setText(mp_codigo);
                            nl1.setText(lote_codigo);
                            c1.requestFocus();
                        }
                        else {
                            Toast.makeText(getActivity(),"Error en el código",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                mibuild.setView(mview);
                AlertDialog dialogo = mibuild.create();
                dialogo.show();
            }
        });
        return v;
    }

    private void ObtenerAlmacenes(){
        String obs = " ";
        Date fechahora = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        final String dias = dateFormat.format(fechahora);
        String currentString = u1.getText().toString();
        String[] separated = currentString.split("-");
        final String a = separated[0];
        final String b = separated[1];
        final String c = separated[2];
            final String url = "https://appsionmovil.000webhostapp.com/AlmacenMP_insertar.php?Rack=" + a.replaceAll(" ", "%20") +
                    "&Fila=" + b.replaceAll(" ", "%20") +
                    "&Columna=" + c.replaceAll(" ", "%20") +
                    "&MateriaPrima=" + mp1.getText().toString().replaceAll(" ", "%20") +
                    "&LoteMP=" + nl1.getText().toString().replaceAll(" ", "%20") +
                    "&Envase=" + obs.replaceAll(" ", "%20") +
                    "&Cantidad=" + c1.getText().toString().replaceAll(" ", "%20") +
                    "&Persona=" + usuario.replaceAll(" ", "%20") +
                    "&Observaciones=" + obs.replaceAll(" ", "%20") +
                    "&FechayHora=" + dias.replaceAll(" ", "%20");
            cliente.post(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (statusCode == 200) {
                        //Toast.makeText(getActivity(), url, Toast.LENGTH_SHORT).show();
                        mp1.setText("");
                        nl1.setText("");
                        c1.setText("");
                        u1.setText("");
                    } }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Estructura_BBDD admin = new Estructura_BBDD(getActivity(), "administracion", null, 1);
                    SQLiteDatabase bd = admin.getWritableDatabase();
                    Toast.makeText(getActivity(), "Se cargaron los datos temporalmente de la persona en BD Interno",Toast.LENGTH_LONG).show();
                        String map1 = mp1.getText().toString();
                        String nla1 = nl1.getText().toString();
                        String cana1 = c1.getText().toString();
                        String usuari1 = usuario;
                        ContentValues registro = new ContentValues();
                        registro.put("matpria1", map1);
                        registro.put("nolotea1", nla1);
                        registro.put("cantia1", cana1);
                        registro.put("racka1", a);
                        registro.put("fila1", b);
                        registro.put("cola1", c);
                        registro.put("fechahora1", dias);
                        registro.put("usuara1", usuari1);
                        bd.insert("almacensqlite", null, registro);
                    //Toast.makeText(getActivity(), map1+" "+nla1+" "+cana1+" "+a+" "+b+" "+c+" "+dias,Toast.LENGTH_LONG).show();
                    bd.close();
                        mp1.setText("");
                        nl1.setText("");
                        c1.setText("");
                        u1.setText("");
                }
            });
    }
}