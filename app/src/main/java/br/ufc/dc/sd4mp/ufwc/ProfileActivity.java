package br.ufc.dc.sd4mp.ufwc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
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


public class ProfileActivity extends ActionBarActivity {

    private String loggedInUserEmail;

    private UFWCDAO ufwcDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        loggedInUserEmail = "";

        ufwcDAO = new UFWCDAO(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Utilities.checkNetworkConnection(ProfileActivity.this);
        Utilities.checkGPSConnection(ProfileActivity.this);

        retrieveData();
        fillBlankSpaces();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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

    private void retrieveData() {

        Bundle bundle = getIntent().getExtras();

        try {

            loggedInUserEmail = bundle.getString("loggedInUserEmail");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void fillBlankSpaces() {

        User loggedInUser = ufwcDAO.retrieveUsuarioByEmail(loggedInUserEmail);

        String passwordInDots = "";

        EditText etEmail = (EditText) findViewById(R.id.editText4);
        EditText etPassword = (EditText) findViewById(R.id.editText5);
        EditText etConfirmPassword = (EditText) findViewById(R.id.editText6);
        RadioButton rbFemale = (RadioButton) findViewById(R.id.radioButton3);
        RadioButton rbMale = (RadioButton) findViewById(R.id.radioButton4);

        etEmail.setText(loggedInUser.getEmail());

        for (int i = 0; i < loggedInUser.getPassword().length(); i++)
            passwordInDots += "*";

        etPassword.setText(passwordInDots);
        etConfirmPassword.setText(passwordInDots);

        if (loggedInUser.getGender().equals("Feminino"))
            rbFemale.setChecked(true);
        else
            rbMale.setChecked(true);

    }

    public void editProfile(View v) {

        User loggedInUser = ufwcDAO.retrieveUsuarioByEmail(loggedInUserEmail);

        TextView tvEmail = (TextView) findViewById(R.id.textView7);
        EditText etEmail = (EditText) findViewById(R.id.editText4);
        EditText etPassword = (EditText) findViewById(R.id.editText5);
        EditText etConfirmPassword = (EditText) findViewById(R.id.editText6);
        RadioButton rbFemale = (RadioButton) findViewById(R.id.radioButton3);
        RadioButton rbMale = (RadioButton) findViewById(R.id.radioButton4);
        Button btEdit = (Button) findViewById(R.id.button4);
        Button btSave = (Button) findViewById(R.id.button5);

        tvEmail.setText("Email: (Not editable)");

        etEmail.setText(loggedInUser.getEmail());
        etEmail.setEnabled(false);

        etPassword.setText("");
        etPassword.setEnabled(true);

        etConfirmPassword.setText("");
        etConfirmPassword.setEnabled(true);

        if (loggedInUser.getGender().equals("Feminino"))
            rbFemale.setChecked(true);

        else
            rbMale.setChecked(true);

        rbFemale.setEnabled(true);
        rbMale.setEnabled(true);

        btEdit.setAlpha(0.25f);
        btEdit.setClickable(false);

        btSave.setAlpha(1.0f);
        btSave.setClickable(true);

    }

    public void saveProfile(View v) {

        User loggedInUser = ufwcDAO.retrieveUsuarioByEmail(loggedInUserEmail);

        TextView tvEmail = (TextView) findViewById(R.id.textView7);
        EditText etEmail = (EditText) findViewById(R.id.editText4);
        EditText etPassword = (EditText) findViewById(R.id.editText5);
        EditText etConfirmPassword = (EditText) findViewById(R.id.editText6);
        RadioButton rbFemale = (RadioButton) findViewById(R.id.radioButton3);
        RadioButton rbMale = (RadioButton) findViewById(R.id.radioButton4);
        Button btEdit = (Button) findViewById(R.id.button4);
        Button btSave = (Button) findViewById(R.id.button5);

        if ( (etPassword.getText().length() > 0 && etConfirmPassword.getText().length() > 0) && (rbMale.isChecked() || rbFemale.isChecked()) ) {

            if (etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {

                tvEmail.setText("Email:");

                etEmail.setEnabled(false);
                etPassword.setEnabled(false);
                etConfirmPassword.setEnabled(false);

                rbFemale.setEnabled(false);
                rbMale.setEnabled(false);

                btEdit.setAlpha(1.0f);
                btEdit.setClickable(true);

                btSave.setAlpha(0.25f);
                btSave.setClickable(false);

                loggedInUser.setPassword(etPassword.getText().toString());

                if (rbFemale.isChecked())
                    loggedInUser.setGender("Feminino");
                else
                    loggedInUser.setGender("Masculino");

                //ufwcDAO.updateUsuario(loggedInUser);

                new SendUserTask(loggedInUser).execute("http://ufwc-final.herokuapp.com/users.json");

                Toast.makeText(ProfileActivity.this, "Your profile was changed successfully.", Toast.LENGTH_LONG).show();

                Intent i = new Intent(ProfileActivity.this, MainActivity.class);
                i.putExtra("loggedInUserEmail", loggedInUser.getEmail());

                startActivity(i);

            } else {

                Toast.makeText(ProfileActivity.this, "Password does not match the password confirmation.", Toast.LENGTH_LONG).show();

                etPassword.requestFocus();

            }

        } else
            Toast.makeText(ProfileActivity.this, "Fill all blank spaces.", Toast.LENGTH_LONG).show();

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

                dialog = new ProgressDialog(ProfileActivity.this);
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                dialog.setMessage("Updating data...");

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
