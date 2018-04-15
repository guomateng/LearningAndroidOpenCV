package matain.com.learningandroidopencv;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.security.PublicKey;

public class HomeActivity extends AppCompatActivity {
    private final String LOG_TAG = HomeActivity.class.getSimpleName();
    private Button mAverageBlurButton;
    private Button mGaussBlurButton;
    private Button mMedianBlurButton;
    private Button mFilter2DButton;
    public static final String KEY_ACTION_MODE = "ACTION_MODE";
    public static final int MEAN_AVERAGE_BLUR = 1;
    public static final int MEAN_GAUSS_BLUR = 2;
    public static final int MEAN_MEDIA_BLUR = 3;
    public static final int MEAN_FILTER2D = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAverageBlurButton = findViewById(R.id.bMeanAverage);
        mAverageBlurButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(KEY_ACTION_MODE, MEAN_AVERAGE_BLUR);
                startActivity(intent);
            }
        });

        mGaussBlurButton = findViewById(R.id.bMeanGauss);
        mGaussBlurButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(KEY_ACTION_MODE, MEAN_GAUSS_BLUR);
                startActivity(intent);
            }
        });

        mMedianBlurButton = findViewById(R.id.bMeanMedian);
        mMedianBlurButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(KEY_ACTION_MODE, MEAN_MEDIA_BLUR);
                startActivity(intent);
            }
        });

        mFilter2DButton = findViewById(R.id.bMeanFilter2D);
        mFilter2DButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(KEY_ACTION_MODE, MEAN_FILTER2D);
                startActivity(intent);
            }
        });
    }
}
