package br.ufc.dc.sd4mp.ufwc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class RegisterActivity extends ActionBarActivity {

    private UFWCDAO ufwcDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ufwcDAO = new UFWCDAO(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        Utilities.checkNetworkConnection(RegisterActivity.this);
        Utilities.checkGPSConnection(RegisterActivity.this);
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

    public void confirmRegistration(View v) {

        EditText etEmail = (EditText) findViewById(R.id.editText);
        EditText etPassword = (EditText) findViewById(R.id.editText2);
        EditText etConfirmPassword = (EditText) findViewById(R.id.editText3);

        RadioButton rbFemale = (RadioButton) findViewById(R.id.radioButton);
        RadioButton rbMale = (RadioButton) findViewById(R.id.radioButton2);

        if ( (etEmail.getText().length() > 0 && etPassword.getText().length() > 0 && etConfirmPassword.getText().length() > 0)  &&
        (rbFemale.isChecked() || rbMale.isChecked()) ) {

            if (etEmail.getText().toString().contains("@") && etEmail.getText().toString().contains(".")) {

                if (etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {

                    if (etPassword.getText().length() <= 3) {

                        Toast.makeText(RegisterActivity.this, "Your password must contains at least 4 characters.", Toast.LENGTH_LONG).show();

                        etPassword.requestFocus();

                    } else {

                        if (ufwcDAO.retrieveUsuarioByEmail(etEmail.getText().toString()) != null) {

                            Toast.makeText(RegisterActivity.this, "This email is already used.", Toast.LENGTH_LONG).show();

                            etEmail.requestFocus();

                        } else {

                            String newUserGender;

                            if (rbFemale.isChecked())
                                newUserGender = "Feminino";

                            else
                                newUserGender = "Masculino";

                            User newUser = new User(ufwcDAO.listUsuarios().size() + 1, etEmail.getText().toString(),
                                    etPassword.getText().toString(), newUserGender);

                            //ufwcDAO.createUsuario(newUser);

                            new SendUserTask(newUser).execute("http://ufwc-final.herokuapp.com/users.json");

                            Toast.makeText(RegisterActivity.this, "You are now registered and logged in.", Toast.LENGTH_LONG).show();

                            Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                            i.putExtra("loggedInUserEmail", newUser.getEmail());

                            startActivity(i);

                        }

                    }

                } else {

                    Toast.makeText(RegisterActivity.this, "Password does not match the password confirmation.", Toast.LENGTH_LONG).show();

                    etPassword.requestFocus();

                }

            } else
                Toast.makeText(RegisterActivity.this, "Invalid email.", Toast.LENGTH_LONG).show();

        } else
            Toast.makeText(RegisterActivity.this, "Fill all blank spaces.", Toast.LENGTH_LONG).show();

    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";

        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();

        return result;

    }

    public static String POSTUser(String url, User user) {

        InputStream inputStream = null;
        String result = "";

        try {

            // 1. create HttpClient
            HttpClient httpClient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("email", user.getEmail());
            jsonObject.accumulate("password", user.getPassword());
            jsonObject.accumulate("gender", user.getGender());

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpClient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

            Log.v("Server", result);

        } catch (Exception e) {

            Log.d("InputStream", e.getLocalizedMessage());

        }

        // 11. return result
        return result;

    }

    private class SendUserTask extends AsyncTask<String, String, String> {

        User user;

        ProgressDialog dialog;

        public SendUserTask(User user) {

            this.user = user;

        }

        @Override
        protected String doInBackground(String... urls) {

            return POSTUser(urls[0], user);

        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            if (dialog == null) {

                dialog = new ProgressDialog(RegisterActivity.this);
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                dialog.setMessage("Sending data...");

            }

            dialog.show();

        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            if (dialog != null)
                dialog.dismiss();

            dialog = null;

        }

    }

}
