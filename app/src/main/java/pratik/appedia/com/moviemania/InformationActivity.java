package pratik.appedia.com.moviemania;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class InformationActivity extends AppCompatActivity {

    private TextView messageTextView;
    private TextView informationTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        final Typeface galada_regular = Typeface.createFromAsset(getAssets(),"fonts/Galada-Regular.ttf");
        final Typeface roboto_regular = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");

        informationTextview = findViewById(R.id.informationTextview);
        informationTextview.setTypeface(galada_regular);
        informationTextview.setText(getString(R.string.information));

        messageTextView = findViewById(R.id.messageTextview);
        messageTextView.setTypeface(roboto_regular);
        messageTextView.setText(getString(R.string.info_screen_message));

    }
}
