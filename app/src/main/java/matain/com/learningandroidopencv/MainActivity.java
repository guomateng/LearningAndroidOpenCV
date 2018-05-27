package matain.com.learningandroidopencv;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private final int SELECT_PHOTO = 1;
    private ImageView mSrcImageView;
    private ImageView mDstImageView;
    private SeekBar mProgress;
    private Bitmap mSelectedImage;
    private TextView mTextView;
    private Mat mSrc, mDst;
    private int mThreshold = 100;
    private static int ACTION_MODE = 1;
    private static String ACTION_TITLE = "";
    private myHandler mHandler = new myHandler();
    private static final int EVENT_BASE = 100;
    private static final int EVENT_SHOW_RESULT = EVENT_BASE;

    private BaseLoaderCallback mOpenCVCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            super.onManagerConnected(status);
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    Log.d(LOG_TAG, "openCV lib load complete ....");
                    break;
                default:
                    Log.d(LOG_TAG, "openCV lib load fail .. error = " + status);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        if (intent.hasExtra(HomeActivity.KEY_ACTION_MODE)) {
            ACTION_MODE = intent.getIntExtra(HomeActivity.KEY_ACTION_MODE, 1);
        }
        if(intent.hasExtra(HomeActivity.KEY_ACTION_TITLE)){
            ACTION_TITLE = intent.getStringExtra(HomeActivity.KEY_ACTION_TITLE);
            setTitle(ACTION_TITLE);
        }
        mSrcImageView = findViewById(R.id.src_image_view);
        mDstImageView = findViewById(R.id.dest_image_view);
        mProgress = findViewById(R.id.progress);
        mTextView = findViewById(R.id.SeekBarValue);
        if(mProgress != null && mTextView != null){
            dismissProgressBar();
            mProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    if (mTextView != null){
                        mTextView.setText("Value:" + i);
                    }
                    mThreshold = i;
                    processImageAndShowResult();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
        if (!OpenCVLoader.initDebug()) {
            Log.d(LOG_TAG, "external openCV lib found using it .....");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mOpenCVCallBack);
        } else {
            Log.d(LOG_TAG, "internal openCV lib found using it .....");
            mOpenCVCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }
    private void dismissProgressBar(){
        if(mProgress != null && mTextView != null) {
            mProgress.setVisibility(View.GONE);
            mTextView.setVisibility(View.GONE);
        }
    }

    private void displayProgressBar(){
        if(mProgress != null && mTextView != null) {
            mProgress.setVisibility(View.VISIBLE);
            mTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_load_image) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class myHandler extends Handler{
        @Override
        public void handleMessage(Message msg){
            Log.d(LOG_TAG, "myHandler recv event " + msg.what);
            switch (msg.what){
                case EVENT_SHOW_RESULT:
                    showResult();
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        mSelectedImage = BitmapFactory.decodeStream(imageStream);
                        mSrc = new Mat(mSelectedImage.getWidth(), mSelectedImage.getHeight(), CvType.CV_8UC3);
                        mDst = new Mat(mSelectedImage.getWidth(), mSelectedImage.getHeight(), CvType.CV_8UC3);
                        Utils.bitmapToMat(mSelectedImage, mSrc);
                    } catch (FileNotFoundException e) {
                        Log.d(LOG_TAG, "FileNotFoundException E:" + e);
                    }
                }
                processImageAndShowResult();
                break;
            default:
                Log.d(LOG_TAG, "can not handle the case "+ ACTION_MODE);
                break;
        }
    }
    private void processImageAndShowResult(){
        switch (ACTION_MODE) {
            case HomeActivity.MEAN_AVERAGE_BLUR:
                Log.d(LOG_TAG, "do average blur ..");
                Imgproc.blur(mSrc, mDst, new Size(3, 3));
                break;
            case HomeActivity.MEAN_GAUSS_BLUR:
                Log.d(LOG_TAG, "do gaussian blur ....");
                Imgproc.GaussianBlur(mSrc, mDst, new Size(3, 3), 0);
                break;

            case HomeActivity.MEAN_MEDIA_BLUR:
                Log.d(LOG_TAG, "do median blur ....");
                Imgproc.medianBlur(mSrc, mDst, 7);
                break;

            case HomeActivity.MEAN_FILTER2D:
                Log.d(LOG_TAG, "do filter2d  ....");
                Imgproc.cvtColor(mSrc, mSrc, Imgproc.COLOR_BGR2GRAY);
                Mat kernel = new Mat(3, 3, CvType.CV_8SC1);
                kernel.put(3, 3, 0, -1, 0, -1, 5, -1, 0, -1, 0);
                Imgproc.filter2D(mSrc, mDst, mSrc.depth(), kernel);
                break;
            case HomeActivity.MEAN_DoG:
                doDogEdge();
                break;
            case HomeActivity.MEAN_CANNY:
                doCannyEdge();
                displayProgressBar();
                break;
            case HomeActivity.MEAN_SOBLE:
                displayProgressBar();
                doSobelEdge();
            case HomeActivity.MEAN_HARRIS_CORNER:
                displayProgressBar();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        doHarrisCorner();
                    }
                }).start();
                return;
            default:
                Log.d(LOG_TAG, "can not handle the case "+ ACTION_MODE);
                break;
        }
        showResult();
    }
    private void doDogEdge(){
        Mat srcGray = new Mat();
        Mat gBlur1 = new Mat();
        Mat gBlur2 = new Mat();
                            /*step1: convert BGR to Gray*/
        Imgproc.cvtColor(mSrc, srcGray, Imgproc.COLOR_BGR2GRAY);
                            /*step2: make tow radius gaussian blur for gray mat*/
        Imgproc.GaussianBlur(srcGray, gBlur1, new Size(3, 3), 0);
        Imgproc.GaussianBlur(srcGray, gBlur2, new Size(5, 5), 0);
                            /*do difference of gaussian process*/
        Core.absdiff(gBlur1, gBlur2, mDst);
                            /*threshold the mDst*/
        Imgproc.threshold(mDst, mDst, 1, 255, Imgproc.THRESH_BINARY);
    }

    private void doCannyEdge() {
        Mat srcGray = new Mat();
        Imgproc.cvtColor(mSrc, srcGray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.Canny(srcGray, mDst, mThreshold, 2*mThreshold);
    }

    private void doSobelEdge(){
        Mat srcGray = new Mat();
        Mat sobel = new Mat();
        Imgproc.cvtColor(mSrc, srcGray, Imgproc.COLOR_BGR2GRAY);

        /*calculate X direction gray grad*/
        Mat x_grad = new Mat();
        Mat x_abs_grad = new Mat();
        Imgproc.Sobel(srcGray, x_grad, CvType.CV_16S,1,0,3,mThreshold,0);
        Core.convertScaleAbs(x_grad,x_abs_grad);

        /*calculate Y direction gray grad*/
        Mat y_grad = new Mat();
        Mat y_abs_grad = new Mat();
        Imgproc.Sobel(srcGray, y_grad, CvType.CV_16S,0,1,3,mThreshold,0);
        Core.convertScaleAbs(y_grad,y_abs_grad);

        Core.addWeighted(x_abs_grad, 0.5, y_abs_grad, 0.5, 1, sobel);
        sobel.copyTo(mDst);
    }

    private void doHarrisCorner(){
        Mat srcGray = new Mat();
        Mat corners = new Mat();
        Imgproc.cvtColor(mSrc, srcGray, Imgproc.COLOR_BGR2GRAY);

        Mat tempCorners = new Mat();
        Log.d(LOG_TAG, "doHarrisCorner step 1");
        Imgproc.cornerHarris(srcGray,tempCorners,2,3,0.04);

        Core.normalize(tempCorners, tempCorners,0,255,Core.NORM_MINMAX);
        Core.convertScaleAbs(tempCorners,tempCorners);
        Log.d(LOG_TAG, "doHarrisCorner step 2");
        Random random = new Random();
        Log.d(LOG_TAG, "tempCorners cols = " + tempCorners.cols() + " tempCorners rows = " + tempCorners.rows());
        for (int i = 0; i < tempCorners.cols(); i++){
            for (int j = 0; j < tempCorners.rows(); j++){
                double[] value = tempCorners.get(i,j);
                if(value != null && value[0] > 254){
                    Log.d(LOG_TAG, "position(" + i +", " + j + ") value is " + value[0]);
                    Imgproc.circle(corners, new Point(i,j), 5, new Scalar(random.nextInt(255)), 2);
                }
            }
        }
        Log.d(LOG_TAG, "doHarrisCorner step 3");
        corners.copyTo(mDst);
        Message msg = mHandler.obtainMessage(EVENT_SHOW_RESULT);
        msg.sendToTarget();
    }

    private void showResult() {
        Bitmap srcImage = Bitmap.createBitmap(mSrc.cols(), mSrc.rows(), Bitmap.Config.RGB_565);
        Bitmap dstImage = Bitmap.createBitmap(mDst.cols(), mDst.rows(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(mSrc, srcImage);
        Utils.matToBitmap(mDst, dstImage);
        mSrcImageView.setImageBitmap(srcImage);
        mDstImageView.setImageBitmap(dstImage);
    }
}
