package it.unical.mat.reversi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ReversiActivity extends AppCompatActivity {

    public final static String EXTRA_MODE_WHITE = "it.unical.mat.reversi.MODE_WHITE";
    public final static String EXTRA_MODE_BLACK = "it.unical.mat.reversi.MODE_BLACK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reversi);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.player_mode, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        Spinner spinnerWhite = (Spinner) findViewById(R.id.spinnerPlayerWhite);
        spinnerWhite.setAdapter(adapter);
        Spinner spinnerBlack = (Spinner) findViewById(R.id.spinnerPlayerBlack);
        spinnerBlack.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reversi, menu);
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

    public void startNewGame(View v) {

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(EXTRA_MODE_WHITE, (CharSequence) ((Spinner) findViewById(R.id.spinnerPlayerWhite)).getSelectedItem());
        intent.putExtra(EXTRA_MODE_BLACK, (CharSequence) ((Spinner) findViewById(R.id.spinnerPlayerBlack)).getSelectedItem());
        startActivity(intent);


    }

}
