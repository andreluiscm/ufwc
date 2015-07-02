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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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


public class RateBathroomActivity extends ActionBarActivity {

    private int bathroomToRateId;

    private String loggedInUserEmail;

    private UFWCDAO ufwcDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_bathroom);

        ufwcDAO = new UFWCDAO(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Utilities.checkNetworkConnection(RateBathroomActivity.this);
        Utilities.checkGPSConnection(RateBathroomActivity.this);

        retrieveData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rate_bathroom, menu);
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

            loggedInUserEmail =  bundle.getString("loggedInUserEmail");
            bathroomToRateId = bundle.getInt("bathroomToRateId");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void confirmReview(View v) {

        User loggedInUser = ufwcDAO.retrieveUsuarioByEmail(loggedInUserEmail);

        RadioButton rbHygienizationGreat = (RadioButton) findViewById(R.id.radioButton5);
        RadioButton rbHygienizationGood = (RadioButton) findViewById(R.id.radioButton6);
        RadioButton rbHygienizationRegular = (RadioButton) findViewById(R.id.radioButton7);
        RadioButton rbHygienizationPoor = (RadioButton) findViewById(R.id.radioButton8);
        RadioButton rbHygienizationUnbearable = (RadioButton) findViewById(R.id.radioButton9);

        CheckBox cbHasDoor = (CheckBox) findViewById(R.id.checkBox);
        CheckBox cbHasMirror = (CheckBox) findViewById(R.id.checkBox2);
        CheckBox cbHasToilet = (CheckBox) findViewById(R.id.checkBox3);
        CheckBox cbHasPaper = (CheckBox) findViewById(R.id.checkBox4);
        CheckBox cbHasWashbasin = (CheckBox) findViewById(R.id.checkBox5);
        CheckBox cbHasWater = (CheckBox) findViewById(R.id.checkBox6);
        CheckBox cbHasSoap = (CheckBox) findViewById(R.id.checkBox7);
        CheckBox cbHasShower = (CheckBox) findViewById(R.id.checkBox8);
        CheckBox cbHasAccessibility = (CheckBox) findViewById(R.id.checkBox9);

        EditText etComment = (EditText) findViewById(R.id.editText9);

        if ( (!rbHygienizationGreat.isChecked() && !rbHygienizationGood.isChecked() && !rbHygienizationRegular.isChecked()
                && !rbHygienizationPoor.isChecked() && !rbHygienizationUnbearable.isChecked()) ) {

            Toast.makeText(RateBathroomActivity.this, "Choose ate least one item of the 'Hygienization' category.", Toast.LENGTH_LONG).show();

            RadioGroup rgHygienization = (RadioGroup) findViewById(R.id.radioGroup);
            rgHygienization.requestFocus();

        } else {

            double hygienizationGrade;

            if (rbHygienizationGreat.isChecked())
                hygienizationGrade = 10.0;

            else if (rbHygienizationGood.isChecked())
                hygienizationGrade = 7.5;

            else if (rbHygienizationRegular.isChecked())
                hygienizationGrade = 5.0;

            else if (rbHygienizationPoor.isChecked())
                hygienizationGrade = 2.5;

            else
                hygienizationGrade = 0.0;

            Review newReview = new Review();
            newReview.setUserId(loggedInUser.getId());
            newReview.setBathroomId(bathroomToRateId);
            newReview.setHygienizationGrade(hygienizationGrade);
            newReview.setHasDoor(cbHasDoor.isChecked());
            newReview.setHasMirror(cbHasMirror.isChecked());
            newReview.setHasToilet(cbHasToilet.isChecked());
            newReview.setHasPaper(cbHasPaper.isChecked());
            newReview.setHasWashbasin(cbHasWashbasin.isChecked());
            newReview.setHasWater(cbHasWater.isChecked());
            newReview.setHasSoap(cbHasSoap.isChecked());
            newReview.setHasShower(cbHasShower.isChecked());
            newReview.setHasAccessibility(cbHasAccessibility.isChecked());

            //ufwcDAO.createAvaliacao(newReview);

            new SendReviewTask(newReview).execute("http://ufwc-final.herokuapp.com/reviews.json");

            if (etComment.getText().length() > 0) {

                Comment newComment = new Comment();
                newComment.setUserId(loggedInUser.getId());
                newComment.setBathroomId(bathroomToRateId);
                newComment.setText(etComment.getText().toString());

                //ufwcDAO.createComentario(newComment);

                new SendCommentTask(newComment).execute("http://ufwc-final.herokuapp.com/comments.json");

            }

            Toast.makeText(RateBathroomActivity.this, "Review done.", Toast.LENGTH_LONG).show();

            Intent i = new Intent(RateBathroomActivity.this, MainActivity.class);
            i.putExtra("loggedInUserEmail", loggedInUserEmail);

            startActivity(i);

        }

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

    public static String POSTReview(String url, Review review) {

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
            jsonObject.accumulate("user_id", review.getUserId());
            jsonObject.accumulate("bathroom_id", review.getBathroomId());
            jsonObject.accumulate("hygienization", (int) Math.floor(review.getHygienizationGrade()));
            jsonObject.accumulate("has_door", review.isHasDoor());
            jsonObject.accumulate("has_mirror", review.isHasMirror());
            jsonObject.accumulate("has_toilet", review.isHasToilet());
            jsonObject.accumulate("has_papper", review.isHasPaper());
            jsonObject.accumulate("has_washbasin", review.isHasWashbasin());
            jsonObject.accumulate("has_water", review.isHasWater());
            jsonObject.accumulate("has_soap", review.isHasSoap());
            jsonObject.accumulate("has_shower", review.isHasShower());
            jsonObject.accumulate("has_accessibility", review.isHasAccessibility());

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

    public static String POSTComment(String url, Comment comment) {

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
            jsonObject.accumulate("user_id", comment.getUserId());
            jsonObject.accumulate("bathroom_id", comment.getBathroomId());
            jsonObject.accumulate("message", comment.getText());

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

    private class SendReviewTask extends AsyncTask<String, String, String> {

        Review review;

        ProgressDialog dialog;

        @Override
        protected String doInBackground(String... urls) {

            return POSTReview(urls[0], review);

        }

        public SendReviewTask(Review review) {

            this.review = review;

        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            if (dialog == null) {

                dialog = new ProgressDialog(RateBathroomActivity.this);
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                dialog.setMessage("Sending review...");

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

    private class SendCommentTask extends AsyncTask<String, String, String> {

        Comment comment;

        ProgressDialog dialog;

        public SendCommentTask(Comment comment) {

            this.comment = comment;

        }

        @Override
        protected String doInBackground(String... urls) {

            return POSTComment(urls[0], comment);

        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            if (dialog == null) {

                dialog = new ProgressDialog(RateBathroomActivity.this);
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                dialog.setMessage("Sending review...");

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
