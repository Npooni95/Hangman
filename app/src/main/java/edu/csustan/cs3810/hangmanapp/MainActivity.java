package edu.csustan.cs3810.hangmanapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Hangman hangman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (hangman == null) {
            hangman = new Hangman(Hangman.DEFAULT_GUESSES);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(R.id.layoutGameState) == null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            GameStateFragment fragment = new GameStateFragment();
            transaction.add(R.id.layoutGameState, fragment);
            transaction.commit();
        }

        if (fragmentManager.findFragmentById(R.id.layoutGameResult) == null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            GameResultFragment fragment = new GameResultFragment();
            transaction.add(R.id.layoutGameResult, fragment);
            transaction.commit();
        }

        if (fragmentManager.findFragmentByTag("background") == null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            BackgroundFragment fragment = new BackgroundFragment();
            transaction.add(fragment, "background");
            transaction.commit();
        }
    }

    public void play(View v) {
        Log.i("MainActivity", "play");
        EditText input = findViewById(R.id.inputLetter);
        Editable userText = input.getText();

        if (userText != null && userText.length() > 0) {
            char letter = userText.charAt(0);
            getGame().guess(letter);
            TextView lblNumOfGuessesLeft = findViewById(R.id.lblNumOfGuessesLeft);
            lblNumOfGuessesLeft.setText("" + hangman.getGuessesLeft());

            FragmentManager fragmentManager = getSupportFragmentManager();
            GameStateFragment gameStateFragment =
                    (GameStateFragment)fragmentManager.findFragmentById(R.id.layoutGameState);
            View container = gameStateFragment.getView();
            TextView lblGameState = container.findViewById(R.id.lblStateOfGame);
            lblGameState.setText(hangman.currentIncompleteWord());

            input.setText("");

            int result = hangman.gameOver();
            if (result != 0) {
                GameResultFragment gameResultFragment =
                        (GameResultFragment)fragmentManager.findFragmentById(R.id.layoutGameResult);

                if (result == 1){
                    gameResultFragment.setResult("YOU WON");
                    return;
                }

                else if (result == -1) {
                    gameResultFragment.setResult("You Lost");
                }

                input.setHint("");
            }

            if (hangman.getGuessesLeft() == 1) {
                BackgroundFragment backgroundFragment =
                        (BackgroundFragment)fragmentManager.findFragmentByTag("background");
                GameResultFragment gameResultFragment =
                        (GameResultFragment)fragmentManager.findFragmentById(R.id.layoutGameResult);
                gameResultFragment.setResult(backgroundFragment.warning());
            }
        }
    }

    public int getGuessesLeft() {
        return hangman.getGuessesLeft();
    }

    public Hangman getGame() {
        return hangman;
    }
}