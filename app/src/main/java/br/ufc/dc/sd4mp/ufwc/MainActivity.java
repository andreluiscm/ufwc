package br.ufc.dc.sd4mp.ufwc;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private ScrollView scrollView;

    private GoogleMap googleMap;

    private String loggedInUserEmail;

    private UFWCDAO ufwcDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loggedInUserEmail = "";

        ufwcDAO = new UFWCDAO(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Utilities.checkNetworkConnection(MainActivity.this);
        Utilities.checkGPSConnection(MainActivity.this);

        retrieveData();
        checkIfLoggedIn();

        initMap();
        //moveToDC();
        moveToCurrentPosition();
        setMapMovable();
        initMapListeners();
        //initDataTest();

        new RetrieveDataTask("users").execute("http://ufwc-final.herokuapp.com/users.json");
        new RetrieveDataTask("bathrooms").execute("http://ufwc-final.herokuapp.com/bathrooms.json");
        new RetrieveDataTask("reviews").execute("http://ufwc-final.herokuapp.com/reviews.json");
        new RetrieveDataTask("comments").execute("http://ufwc-final.herokuapp.com/comments.json");

        showDataOnMap();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initMap() {

        if (googleMap == null) {

            googleMap = ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

            if (googleMap == null)
                Toast.makeText(MainActivity.this, "Sorry! Unable to create map.", Toast.LENGTH_SHORT).show();
            else
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        }

    }

    private void moveToDC() {

        // Coordinates of DC - UFC - Brazil
        double longitude = -38.5742;
        double latitude = -3.745962;

        MarkerOptions marker = new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title("Department of Computer Science")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(18).build();

        //googleMap.addMarker(marker);
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    private void moveToCurrentPosition() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            moveToDC();

        }

        // Checar se isso esta correto
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 2, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                //MarkerOptions marker = new MarkerOptions()
                        //.position(new LatLng(location.getLatitude(), location.getLongitude()))
                        //.title("You are here!")
                        //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));

                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(18).build();

                //googleMap.addMarker(marker);
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

                ;

            }

            @Override
            public void onProviderEnabled(String provider) {

                ;

            }

            @Override
            public void onProviderDisabled(String provider) {

                ;

            }
        });

    }

    // Used from http://www.londatiga.net/it/programming/android/how-to-make-android-map-scrollable-inside-a-scrollview-layout/
    private void setMapMovable() {

        scrollView = (ScrollView) findViewById(R.id.scrollView);

        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).setListener(new WorkaroundMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                scrollView.requestDisallowInterceptTouchEvent(true);
            }
        });

    }

    private void initMapListeners() {

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                Bathroom bathroomToShow = ufwcDAO.retrieveBanheiroByDescricaoAndGenero(marker.getTitle(), marker.getRotation());

                int bathroomToShowId = bathroomToShow.getId();

                Intent i = new Intent(MainActivity.this, ShowBathroomActivity.class);
                i.putExtra("bathroomToShowId", bathroomToShowId);
                i.putExtra("loggedInUserEmail", loggedInUserEmail);

                startActivity(i);

            }
        });

    }

    private void initDataTest() {

        Bathroom b1 = new Bathroom(1, -38.5739596, -3.7467913, "Banheiro Teste 1 Feminino", "Feminino", new BathroomHelper());
        Bathroom b2 = new Bathroom(2, -38.5739596, -3.7467913, "Banheiro Teste 1 Masculino", "Masculino", new BathroomHelper());

        ufwcDAO.createBanheiro(b1);
        ufwcDAO.createBanheiro(b2);

        User u1 = new User(1, "user1@ufwc.com", "1234", "Feminino");
        User u2 = new User(2, "user2@ufwc.com", "1234", "Masculino");

        ufwcDAO.createUsuario(u1);
        ufwcDAO.createUsuario(u1);

    }

    private void retrieveData() {

        Bundle bundle = getIntent().getExtras();

        try {

            loggedInUserEmail = bundle.getString("loggedInUserEmail");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showDataOnMap() {

        List<Bathroom> alBathrooms = ufwcDAO.listBanheiros();

        for (Bathroom b : alBathrooms) {

            if (b.getGender().equals("Feminino"))
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(b.getLatitude(), b.getLongitude()))
                        .title(b.getDescription())
                        .rotation(-45.0f)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));

            else
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(b.getLatitude(), b.getLongitude()))
                        .title(b.getDescription())
                        .rotation(45.0f)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        }

    }

    private void checkIfLoggedIn() {

        TextView tvLogin = (TextView) findViewById(R.id.textView);
        TextView tvLogout = (TextView) findViewById(R.id.textView2);

        if (loggedInUserEmail.length() > 0) {

            tvLogin.setVisibility(View.INVISIBLE);
            tvLogin.setClickable(false);

            tvLogout.setVisibility(View.VISIBLE);
            tvLogout.setClickable(true);

        }

    }

    public void doLogin(View v) {

        Intent i = new Intent(MainActivity.this, LoginActivity.class);

        startActivity(i);

    }

    public void doLogout(View v) {

        TextView tvLogin = (TextView) findViewById(R.id.textView);
        TextView tvLogout = (TextView) findViewById(R.id.textView2);

        tvLogin.setVisibility(View.VISIBLE);
        tvLogin.setClickable(true);

        tvLogout.setVisibility(View.INVISIBLE);
        tvLogout.setClickable(false);

        loggedInUserEmail = "";

        Toast.makeText(MainActivity.this, "You have successfully logged out.", Toast.LENGTH_LONG).show();

    }

    public void lookProfile(View v) {

        if (loggedInUserEmail.length() > 0) {

            Intent i = new Intent(MainActivity.this, ProfileActivity.class);
            i.putExtra("loggedInUserEmail", loggedInUserEmail);

            startActivity(i);

        } else {

            AlertDialog.Builder alBuilder = new AlertDialog.Builder(MainActivity.this);

            alBuilder.setTitle("Login Alert");
            alBuilder.setMessage("You must be logged in to access your profile.");
            alBuilder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent i = new Intent(MainActivity.this, LoginActivity.class);

                    startActivity(i);

                    }

            });

            alBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();

                }
            });

            alBuilder.show();

        }

    }

    public void contactUs(View v) {

        Intent i = new Intent(Intent.ACTION_SEND);
        i.putExtra(Intent.EXTRA_EMAIL, Utilities.CONTACT_EMAIL);
        i.setType("plain/text");

        startActivity(Intent.createChooser(i, "Sending Email..."));

    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";

        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();

        return result;

    }

    public static String GET(String url) {

        InputStream inputStream = null;
        String result = "";

        try {

            // create HttpClient
            HttpClient httpClient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpClient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);

            else
                result = "Did not work!";

        } catch (Exception e) {

            Log.d("InputStream", e.getLocalizedMessage());

        }

        return result;

    }

    private class RetrieveDataTask extends AsyncTask<String, String, String> {

        String table;

        ProgressDialog dialog;

        public RetrieveDataTask(String table) {

            this.table = table;

        }

        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);

        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            if (dialog == null) {

                dialog = new ProgressDialog(MainActivity.this);
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                dialog.setMessage("Receiving data...");

            }

            dialog.show();

        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            JSONArray jsonArray;

            try {

                jsonArray = new JSONArray(result);

                if (table.equals("users")) {

                    ufwcDAO.deleteAllFromUsuario();

                    HashMap<String, User> hmUsers = new HashMap<String, User>();

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        User user = new User(jsonObject.getInt("id"), jsonObject.getString("email"), jsonObject.getString("password"), jsonObject.getString("gender"));

                        hmUsers.put(user.getEmail(), user);

                    }

                    for (String key : hmUsers.keySet())
                        ufwcDAO.createUsuario(hmUsers.get(key));

                } else if (table.equals("bathrooms")) {

                    ufwcDAO.deleteAllFromBanheiro();

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        Bathroom bathroom = new Bathroom(jsonObject.getInt("id"), jsonObject.getDouble("longitude"),
                                jsonObject.getDouble("latitude"), jsonObject.getString("description"),
                                jsonObject.getString("gender"), new BathroomHelper());

                        ufwcDAO.createBanheiro(bathroom);

                    }

                } else if (table.equals("reviews")) {

                    ufwcDAO.deleteAllFromAvaliacao();

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        double hygienizationGrade = 0d;

                        if (jsonObject.getInt("hygienization") == 10)
                            hygienizationGrade = 10d;

                        else if (jsonObject.getInt("hygienization") == 7)
                            hygienizationGrade = 7.5d;

                        else if (jsonObject.getInt("hygienization") == 5)
                            hygienizationGrade = 5d;

                        else if (jsonObject.getInt("hygienization") == 2)
                            hygienizationGrade = 2.5d;

                        else if (jsonObject.getInt("hygienization") == 0)
                            hygienizationGrade = 0d;

                        Review review = new Review(jsonObject.getInt("user_id"), jsonObject.getInt("bathroom_id"),
                                hygienizationGrade, jsonObject.getBoolean("has_door"),
                                jsonObject.getBoolean("has_mirror"), jsonObject.getBoolean("has_toilet"),
                                jsonObject.getBoolean("has_papper"), jsonObject.getBoolean("has_washbasin"),
                                jsonObject.getBoolean("has_water"), jsonObject.getBoolean("has_soap"),
                                jsonObject.getBoolean("has_shower"), jsonObject.getBoolean("has_accessibility"));

                        ufwcDAO.createAvaliacao(review);

                    }

                } else if (table.equals("comments")) {

                    ufwcDAO.deleteAllFromComentario();

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        Comment comment = new Comment(jsonObject.getInt("user_id"), jsonObject.getInt("bathroom_id"), jsonObject.getString("message"));

                        ufwcDAO.createComentario(comment);

                    }

                }

            } catch (JSONException e) {

                e.printStackTrace();

            }

            if (dialog != null)
                dialog.dismiss();

            dialog = null;

        }

    }

}
