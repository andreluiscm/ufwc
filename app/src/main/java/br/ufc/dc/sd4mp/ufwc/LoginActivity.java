package br.ufc.dc.sd4mp.ufwc;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends ActionBarActivity {

    private UFWCDAO ufwcDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ufwcDAO = new UFWCDAO(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Utilities.checkNetworkConnection(LoginActivity.this);
        Utilities.checkGPSConnection(LoginActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void confirmLogin(View v) {

        EditText etEmail = (EditText) findViewById(R.id.editText7);
        EditText etPassword = (EditText) findViewById(R.id.editText8);

        if (etEmail.getText().length() == 0 || etPassword.getText().length() == 0)
            Toast.makeText(LoginActivity.this, "Fill all blank spaces.", Toast.LENGTH_LONG).show();

        else {

            if (etEmail.getText().toString().contains("@") && etEmail.getText().toString().contains(".")) {

                User userInDatabase = ufwcDAO.retrieveUsuarioByEmail(etEmail.getText().toString());

                if (userInDatabase != null) {

                    if (etPassword.getText().toString().equals(userInDatabase.getPassword())) {

                        Toast.makeText(LoginActivity.this, "You are now logged in.", Toast.LENGTH_LONG).show();

                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        i.putExtra("loggedInUserEmail", userInDatabase.getEmail());

                        startActivity(i);

                    } else
                        Toast.makeText(LoginActivity.this, "Incorrect password.", Toast.LENGTH_LONG).show();

                } else
                    Toast.makeText(LoginActivity.this, "Email not found in database.", Toast.LENGTH_LONG).show();

            } else
                Toast.makeText(LoginActivity.this, "Invalid email.", Toast.LENGTH_LONG).show();

        }

    }

    public void doRegister(View v) {

        Intent i = new Intent(LoginActivity.this, RegisterActivity.class);

        startActivity(i);

    }

}
