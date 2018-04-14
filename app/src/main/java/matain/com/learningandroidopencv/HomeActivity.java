package matain.com.learningandroidopencv;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.security.PublicKey;

public class HomeActivity extends AppCompatActivity {
    private final String LOG_TAG = HomeActivity.class.getSimpleName();
    private Button mBlurButton;
    public static final String KEY_ACTION_MODE = "ACTION_MODE";
    public static final int MEAN_BLUR = 1;
    public static final int MEAN_GAUSS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mBlurButton = findViewById(R.id.bMean);
        mBlurButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(KEY_ACTION_MODE, MEAN_BLUR);
                startActivity(intent);
            }
        });
    }
}
