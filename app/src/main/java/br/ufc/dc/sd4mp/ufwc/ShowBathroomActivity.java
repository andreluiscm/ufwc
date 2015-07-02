package br.ufc.dc.sd4mp.ufwc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class ShowBathroomActivity extends ActionBarActivity {

    private int bathroomToShowId;

    private String loggedInUserEmail;

    private UFWCDAO ufwcDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_bathroom);

        ufwcDAO = new UFWCDAO(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Utilities.checkNetworkConnection(ShowBathroomActivity.this);
        Utilities.checkGPSConnection(ShowBathroomActivity.this);

        retrieveData();
        fillBlankSpaces();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_bathroom, menu);
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

            bathroomToShowId = bundle.getInt("bathroomToShowId");
            loggedInUserEmail =  bundle.getString("loggedInUserEmail");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void fillBlankSpaces() {

        List<Review> alReviews = ufwcDAO.listAvaliacoesByIdBanheiro(bathroomToShowId);
        List<Comment> alComments = ufwcDAO.listCommentsByIdBanheiro(bathroomToShowId);

        Bathroom bathroomToShow = ufwcDAO.retrieveBanheiro(bathroomToShowId);
        bathroomToShow.setBathroomHelper(new BathroomHelper());

        for (Review r : alReviews) {

            bathroomToShow.getBathroomHelper().setReviewCount(bathroomToShow.getBathroomHelper().getReviewCount() + 1);

            if (r.getHygienizationGrade() > 9.0)
                bathroomToShow.getBathroomHelper().setHygienizationGreatCount(bathroomToShow.getBathroomHelper().getHygienizationGreatCount() + 1);

            if (r.getHygienizationGrade() > 7.0 && r.getHygienizationGrade() < 8.0)
                bathroomToShow.getBathroomHelper().setHygienizationGoodCount(bathroomToShow.getBathroomHelper().getHygienizationGoodCount() + 1);

            if (r.getHygienizationGrade() > 4.0 && r.getHygienizationGrade() < 6.0)
                bathroomToShow.getBathroomHelper().setHygienizationRegularCount(bathroomToShow.getBathroomHelper().getHygienizationRegularCount() + 1);

            if (r.getHygienizationGrade() > 2.0 && r.getHygienizationGrade() < 3.0)
                bathroomToShow.getBathroomHelper().setHygienizationPoorCount(bathroomToShow.getBathroomHelper().getHygienizationPoorCount() + 1);

            if (r.getHygienizationGrade() < 1.0)
                bathroomToShow.getBathroomHelper().setHygienizationUnbearableCount(bathroomToShow.getBathroomHelper().getHygienizationUnbearableCount() + 1);

            if (r.isHasDoor())
                bathroomToShow.getBathroomHelper().setHasDoorCount(bathroomToShow.getBathroomHelper().getHasDoorCount() + 1);

            if (r.isHasMirror())
                bathroomToShow.getBathroomHelper().setHasMirrorCount(bathroomToShow.getBathroomHelper().getHasMirrorCount() + 1);

            if (r.isHasToilet())
                bathroomToShow.getBathroomHelper().setHasToiletCount(bathroomToShow.getBathroomHelper().getHasToiletCount() + 1);

            if (r.isHasPaper())
                bathroomToShow.getBathroomHelper().setHasPaperCount(bathroomToShow.getBathroomHelper().getHasPaperCount() + 1);

            if (r.isHasWashbasin())
                bathroomToShow.getBathroomHelper().setHasWashbasinCount(bathroomToShow.getBathroomHelper().getHasWashbasinCount() + 1);

            if (r.isHasWater())
                bathroomToShow.getBathroomHelper().setHasWaterCount(bathroomToShow.getBathroomHelper().getHasWaterCount() + 1);

            if (r.isHasSoap())
                bathroomToShow.getBathroomHelper().setHasSoapCount(bathroomToShow.getBathroomHelper().getHasSoapCount() + 1);

            if (r.isHasShower())
                bathroomToShow.getBathroomHelper().setHasShowerCount(bathroomToShow.getBathroomHelper().getHasShowerCount() + 1);

            if (r.isHasAccessibility())
                bathroomToShow.getBathroomHelper().setHasAccessibilityCount(bathroomToShow.getBathroomHelper().getHasAccessibilityCount() + 1);

        }

        TextView tvDescription = (TextView) findViewById(R.id.textView14);
        TextView tvGender = (TextView) findViewById(R.id.textView16);
        TextView tvGrade = (TextView) findViewById(R.id.textView18);
        TextView tvNumberOfReviews = (TextView) findViewById(R.id.textView20);
        TextView tvHygienizationGreat = (TextView) findViewById(R.id.textView23);
        TextView tvHygienizationGood = (TextView) findViewById(R.id.textView25);
        TextView tvHygienizationRegular = (TextView) findViewById(R.id.textView27);
        TextView tvHygienizationPoor = (TextView) findViewById(R.id.textView29);
        TextView tvHygienizationUnbearable = (TextView) findViewById(R.id.textView31);
        TextView tvHasDoor = (TextView) findViewById(R.id.textView34);
        TextView tvHasMirror = (TextView) findViewById(R.id.textView36);
        TextView tvHasToilet = (TextView) findViewById(R.id.textView38);
        TextView tvHasPaper = (TextView) findViewById(R.id.textView40);
        TextView tvHasWashbasin = (TextView) findViewById(R.id.textView42);
        TextView tvHasWater = (TextView) findViewById(R.id.textView44);
        TextView tvHasSoap = (TextView) findViewById(R.id.textView46);
        TextView tvHasShower = (TextView) findViewById(R.id.textView48);
        TextView tvHasAccessibility = (TextView) findViewById(R.id.textView50);
        TextView tvComment = (TextView) findViewById(R.id.textView57);

        tvDescription.setText(bathroomToShow.getDescription());
        tvGender.setText(bathroomToShow.getGender());
        tvGrade.setText(String.valueOf(bathroomToShow.calculateGrade()));
        tvNumberOfReviews.setText(String.valueOf(bathroomToShow.getBathroomHelper().getReviewCount()));
        tvHygienizationGreat.setText(String.valueOf(bathroomToShow.getBathroomHelper().getHygienizationGreatCount()));
        tvHygienizationGood.setText(String.valueOf(bathroomToShow.getBathroomHelper().getHygienizationGoodCount()));
        tvHygienizationRegular.setText(String.valueOf(bathroomToShow.getBathroomHelper().getHygienizationRegularCount()));
        tvHygienizationPoor.setText(String.valueOf(bathroomToShow.getBathroomHelper().getHygienizationPoorCount()));
        tvHygienizationUnbearable.setText(String.valueOf(bathroomToShow.getBathroomHelper().getHygienizationUnbearableCount()));
        tvHasDoor.setText(String.valueOf(bathroomToShow.getBathroomHelper().getHasDoorCount()));
        tvHasMirror.setText(String.valueOf(bathroomToShow.getBathroomHelper().getHasMirrorCount()));
        tvHasToilet.setText(String.valueOf(bathroomToShow.getBathroomHelper().getHasToiletCount()));
        tvHasPaper.setText(String.valueOf(bathroomToShow.getBathroomHelper().getHasPaperCount()));
        tvHasWashbasin.setText(String.valueOf(bathroomToShow.getBathroomHelper().getHasWashbasinCount()));
        tvHasWater.setText(String.valueOf(bathroomToShow.getBathroomHelper().getHasWaterCount()));
        tvHasSoap.setText(String.valueOf(bathroomToShow.getBathroomHelper().getHasSoapCount()));
        tvHasShower.setText(String.valueOf(bathroomToShow.getBathroomHelper().getHasShowerCount()));
        tvHasAccessibility.setText(String.valueOf(bathroomToShow.getBathroomHelper().getHasAccessibilityCount()));
        tvComment.setText(String.valueOf(alComments.size()));

        if (alComments.size() > 0) {

            int commentLayoutSize = 0;

            if (alComments.size() == 1)
                commentLayoutSize = 100;

            else if (alComments.size() == 2)
                commentLayoutSize = 200;

            else
                commentLayoutSize = 300;

            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

            LayoutParams layoutParams = (LayoutParams) linearLayout.getLayoutParams();
            layoutParams.height = commentLayoutSize;

            linearLayout.setLayoutParams(layoutParams);

        }

        ListView listView = (ListView) findViewById(R.id.listView);
        CommentAdapter adapter = new CommentAdapter(alComments, ShowBathroomActivity.this);

        listView.setAdapter(adapter);

    }

    public void rateBathroom(View v) {

        if (loggedInUserEmail.length() > 0) {

            User loggedInUser = ufwcDAO.retrieveUsuarioByEmail(loggedInUserEmail);
            Bathroom bathroomToRate = ufwcDAO.retrieveBanheiro(bathroomToShowId);

            if (loggedInUser.getGender().equals(bathroomToRate.getGender())) {

                Intent i = new Intent(ShowBathroomActivity.this, RateBathroomActivity.class);
                i.putExtra("loggedInUserEmail", loggedInUserEmail);
                i.putExtra("bathroomToRateId", bathroomToShowId);

                startActivity(i);

            } else
                Toast.makeText(ShowBathroomActivity.this, "Your gender have to match the bathroom gender.", Toast.LENGTH_LONG).show();

        } else {

            AlertDialog.Builder alBuilder = new AlertDialog.Builder(ShowBathroomActivity.this);

            alBuilder.setTitle("Login Alert");
            alBuilder.setMessage("You must be logged in to access your profile.");
            alBuilder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent i = new Intent(ShowBathroomActivity.this, LoginActivity.class);

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

}
