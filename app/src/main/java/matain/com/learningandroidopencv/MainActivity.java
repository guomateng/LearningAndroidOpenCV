package matain.com.learningandroidopencv;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private final int SELECT_PHOTO = 1;
    private ImageView mSrcImageView;
    private ImageView mDstImageView;
    private Bitmap mSelectedImage;
    Mat src, dst;
    private static int ACTION_MODE = 1;

    private BaseLoaderCallback mOpenCVCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            super.onManagerConnected(status);
            switch (status){
                case LoaderCallbackInterface.SUCCESS:
                    Log.d(LOG_TAG, "openCV lib load complete ....");
                    break;
                default:
                    Log.d(LOG_TAG,"openCV lib load fail .. error = " + status);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        if(intent.hasExtra(HomeActivity.KEY_ACTION_MODE)){
            ACTION_MODE = intent.getIntExtra(HomeActivity.KEY_ACTION_MODE, 1);
        }
        mSrcImageView = findViewById(R.id.src_image_view);
        mDstImageView = findViewById(R.id.dest_image_view);
        if(!OpenCVLoader.initDebug()) {
            Log.d(LOG_TAG,"external openCV lib found using it .....");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mOpenCVCallBack);
        }else {
            Log.d(LOG_TAG, "internal openCV lib found using it .....");
            mOpenCVCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.action_load_image){
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        mSelectedImage = BitmapFactory.decodeStream(imageStream);
                        src = new Mat();
                        dst = new Mat();
                        Utils.bitmapToMat(mSelectedImage, src,true);
                    }catch (FileNotFoundException e){
                        Log.d(LOG_TAG, "FileNotFoundException E:" + e);
                    }
                    switch (ACTION_MODE){
                        case HomeActivity.MEAN_AVERAGE_BLUR:
                            Log.d(LOG_TAG, "do average blur ..");
                            Imgproc.blur(src, dst, new Size(3,3));
                            break;
                        case HomeActivity.MEAN_GAUSS_BLUR:
                            Log.d(LOG_TAG, "do gaussian blur ....");
                            Imgproc.GaussianBlur(src, dst, new Size(3,3),0);
                            break;

                        case HomeActivity.MEAN_MEDIA_BLUR:
                            Log.d(LOG_TAG, "do median blur ....");
                            Imgproc.medianBlur(src,dst,7);
                            break;

                        case HomeActivity.MEAN_FILTER2D:
                            Log.d(LOG_TAG, "do filter2d  ....");
                            dst = new Mat();
                            Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2GRAY);
                            Mat kernel = new Mat(3,3,CvType.CV_8SC1);
                            kernel.put(3,3,0,-1,0,-1,5,-1,0,-1,0);
                            Imgproc.filter2D(src, dst, src.depth(),kernel);
                            break;
                    }
                }
                showResult();
                break;
        }
    }

    private void showResult(){
        Bitmap srcImage = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
        Bitmap dstImage = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(src, srcImage);
        Utils.matToBitmap(dst, dstImage);
        mSrcImageView.setImageBitmap(srcImage);
        mDstImageView.setImageBitmap(dstImage);
    }
}
