package matain.com.learningandroidopencv;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.security.PublicKey;
import java.security.SecureRandom;
import java.sql.BatchUpdateException;

public class HomeActivity extends AppCompatActivity {
    private final String LOG_TAG = HomeActivity.class.getSimpleName();
    private Button mAverageBlurButton;
    private Button mGaussBlurButton;
    private Button mMedianBlurButton;
    private Button mFilter2DButton;
    private Button mDoGButton;
    private Button mCannyButton;
    private Button mSobelButton;
    private Button mHarrisCorner;
    public static final String KEY_ACTION_MODE = "ACTION_MODE";
    public static final String KEY_ACTION_TITLE = "KEY_TITLE";
    private static final int MEAN_ACTION_BASE = 1;
    public static final int MEAN_AVERAGE_BLUR = MEAN_ACTION_BASE;
    public static final int MEAN_GAUSS_BLUR = MEAN_ACTION_BASE + 1;
    public static final int MEAN_MEDIA_BLUR = MEAN_ACTION_BASE + 2;
    public static final int MEAN_FILTER2D = MEAN_ACTION_BASE + 3;
    public static final int MEAN_DoG = MEAN_ACTION_BASE + 4;
    public static final int MEAN_CANNY = MEAN_ACTION_BASE + 5;
    public static final int MEAN_SOBLE = MEAN_ACTION_BASE + 6;
    public static final int MEAN_HARRIS_CORNER = MEAN_ACTION_BASE + 7;

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
                intent.putExtra(KEY_ACTION_TITLE,"Average Blur");
                startActivity(intent);
            }
        });

        mGaussBlurButton = findViewById(R.id.bMeanGauss);
        mGaussBlurButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(KEY_ACTION_MODE, MEAN_GAUSS_BLUR);
                intent.putExtra(KEY_ACTION_TITLE,"Gauss Blur");
                startActivity(intent);
            }
        });

        mMedianBlurButton = findViewById(R.id.bMeanMedian);
        mMedianBlurButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(KEY_ACTION_MODE, MEAN_MEDIA_BLUR);
                intent.putExtra(KEY_ACTION_TITLE,"Median Blur");
                startActivity(intent);
            }
        });

        mFilter2DButton = findViewById(R.id.bMeanFilter2D);
        mFilter2DButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(KEY_ACTION_MODE, MEAN_FILTER2D);
                intent.putExtra(KEY_ACTION_TITLE,"Filter2D");
                startActivity(intent);
            }
        });

        mDoGButton = findViewById(R.id.bDoG);
        mDoGButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(KEY_ACTION_MODE, MEAN_DoG);
                intent.putExtra(KEY_ACTION_TITLE,"Difference of Gauss");
                startActivity(intent);
            }
        });

        mCannyButton = findViewById(R.id.bCanny);
        mCannyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(KEY_ACTION_MODE, MEAN_CANNY);
                intent.putExtra(KEY_ACTION_TITLE,"Canny Edge");
                startActivity(intent);
            }
        });

        mSobelButton = findViewById(R.id.bSoble);
        mSobelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(KEY_ACTION_MODE, MEAN_SOBLE);
                intent.putExtra(KEY_ACTION_TITLE,"Sobel Edge");
                startActivity(intent);
            }
        });

        mHarrisCorner = findViewById(R.id.bHarrisCorner);
        mHarrisCorner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(KEY_ACTION_MODE, MEAN_HARRIS_CORNER);
                intent.putExtra(KEY_ACTION_TITLE,"Harris Corner");
                startActivity(intent);
            }
        });
    }
}
