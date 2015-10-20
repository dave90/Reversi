package it.unical.mat.reversi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Spinner;

import it.unical.mat.reversi.ai.AIType;
import it.unical.mat.reversi.core.GameManager;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();

        String extraModeWhite = intent.getStringExtra(ReversiActivity.EXTRA_MODE_WHITE);
        String extraModeBlack = intent.getStringExtra(ReversiActivity.EXTRA_MODE_BLACK);

        Log.i("extraModeWhite", extraModeWhite);
        Log.i("extraModeBlack", extraModeBlack);

        // FIXME not the best way to do it
        final PlayerType playerTypeWhite = extraModeWhite.contains("Human") ? PlayerType.HUMAN : PlayerType.COMPUTER;
        final PlayerType playerTypeBlack = extraModeBlack.contains("Human") ? PlayerType.HUMAN : PlayerType.COMPUTER;

        GameManager gameManager = new GameManager();

        if (extraModeWhite.contains("Computer")) {
            gameManager.setAI(getAIType(extraModeWhite.substring(extraModeWhite.indexOf("(") + 1,extraModeWhite.indexOf(")"))));
        }
        if (playerTypeBlack == PlayerType.COMPUTER) {
            gameManager.setAI(getAIType(extraModeBlack.substring(extraModeBlack.indexOf("(") + 1,extraModeBlack.indexOf(")"))));
        }

    }

    private AIType getAIType(String extraMode) {

        for (AIType t : AIType.values())
        if (t.name().contains(extraMode))
            return t;

      throw new IllegalStateException("Illegal value for extraMode: " + extraMode);

    }

}
