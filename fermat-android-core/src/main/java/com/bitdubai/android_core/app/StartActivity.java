package com.bitdubai.android_core.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;
import com.bitdubai.android_core.layer._2_os.android.developer.bitdubai.version_1.AndroidOsDataBaseSystem;
import com.bitdubai.android_core.layer._2_os.android.developer.bitdubai.version_1.AndroidOsFileSystem;
import com.bitdubai.android_core.layer._2_os.android.developer.bitdubai.version_1.AndroidOsLocationSystem;
import com.bitdubai.fermat.R;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatWorkerCallBack;
import com.bitdubai.fermat_android_api.ui.util.FermatWorker;
import com.bitdubai.fermat_api.CantReportCriticalStartingProblemException;
import com.bitdubai.fermat_api.CantStartPlatformException;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.Service;
import com.bitdubai.fermat_api.layer.all_definition.enums.Addons;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import com.bitdubai.fermat_api.layer.all_definition.resources_structure.enums.ScreenSize;
import com.bitdubai.fermat_api.layer.all_definition.util.DeviceInfoUtils;
import com.bitdubai.fermat_api.layer.osa_android.LoggerSystemOs;
import com.bitdubai.fermat_core.CorePlatformContext;
import com.bitdubai.fermat_core.Platform;
import com.bitdubai.fermat_osa_addon.layer.android.logger.developer.bitdubai.version_1.LoggerAddonRoot;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.UnexpectedUIExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.platform_info.interfaces.PlatformInfo;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.platform_info.interfaces.PlatformInfoManager;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.platform_info.interfaces.exceptions.CantLoadPlatformInformationException;

import java.util.concurrent.Executors;


/**
 * Created by Matias Furszyfer
 */

/**
 * This Activity is used as a loader, to show something while the Fermat Platform is being initialized.
 * <p>
 * -- Luis.
 */

public class StartActivity extends FragmentActivity implements FermatWorkerCallBack{


    public static final String START_ACTIVITY_INIT = "Init";

    // Indicate if the app was loaded, for not load again the start activity.
    private static boolean WAS_START_ACTIVITY_LOADED = false;


    private AndroidOsFileSystem fileSystemOs;
    private CorePlatformContext platformContext;
    private AndroidOsDataBaseSystem databaseSystemOs;
    private AndroidOsLocationSystem locationSystemOs;
    private LoggerSystemOs loggerSystemOs;

    private Platform platform;


    private ProgressDialog mDialog;

    private ImageView imageView_fermat;


    Animation animation1;
    Animation animation2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            // Indicate if the app was loaded, for not load again the start activity.
            if (WAS_START_ACTIVITY_LOADED) {
                this.fermatInit ();
            }

            try {
                setContentView(R.layout.splash_screen);

                imageView_fermat = (ImageView) findViewById(R.id.imageView_fermat);

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Oooops! recovering from system error",
                        Toast.LENGTH_LONG).show();
            }

            int applicationState = ((ApplicationSession)getApplication()).getApplicationState();

            if(applicationState==ApplicationSession.STATE_NOT_CREATED) {
//                mDialog = new ProgressDialog(this);
//                mDialog.setMessage("Please wait...");
//                mDialog.setCancelable(false);
//////                    mDialog.show();
//                AlphaAnimation fadeOut = new AlphaAnimation( 1.0f , 0.0f ) ;
//                AlphaAnimation fadeIn = new AlphaAnimation(0.0f , 1.0f ) ;
//                imageView_fermat.startAnimation(fadeIn);
//                imageView_fermat.startAnimation(fadeOut);
//                imageView_fermat.getAnimation().setRepeatCount(Animation.INFINITE);
//                fadeIn.setDuration(1200);
//                fadeOut.setDuration(1200);
//                imageView_fermat.getAnimation().setDuration(1500);

//                Animation fadeIn = new AlphaAnimation(0, 1);
//                fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
//                fadeIn.setDuration(1000);
//
//                Animation fadeOut = new AlphaAnimation(1, 0);
//                fadeOut.setInterpolator(new AccelerateInterpolator()); // and this
//                //fadeOut.setStartOffset(fadeInDuration + timeBetween);
//                fadeOut.setDuration(1000);
//
//                AnimationSet animation = new AnimationSet(false); // change to false
//                animation.addAnimation(fadeIn);
//                animation.addAnimation(fadeOut);
//                animation.setRepeatCount(Animation.INFINITE);
//                imageView_fermat.setAnimation(animation);
//                imageView_fermat.animate();
//                imageView_fermat.getAnimation().start();
//                imageView_fermat.getAnimation().setDuration(Animation.INFINITE);
                //fadeOut.setStartOffset(1200+fadeIn.getStartOffset()+1200);
//                measurement_index = AppSettings.getMeasureUnit(this);


                animation1 = new AlphaAnimation(0.0f, 1.0f);
                animation1.setDuration(1000);
                //animation1.setStartOffset(5000);

                //animation1 AnimationListener
                animation1.setAnimationListener(new Animation.AnimationListener(){

                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        // start animation2 when animation1 ends (continue)
                        imageView_fermat.startAnimation(animation2);
                    }

                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationStart(Animation arg0) {
                        // TODO Auto-generated method stub

                    }

                });

                animation2 = new AlphaAnimation(1.0f, 0.0f);
                animation2.setDuration(1000);
                //animation2.setStartOffset(5000);

                //animation2 AnimationListener
                animation2.setAnimationListener(new Animation.AnimationListener(){

                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        // start animation1 when animation2 ends (repeat)
                        imageView_fermat.startAnimation(animation1);
                    }

                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationStart(Animation arg0) {
                        // TODO Auto-generated method stub

                    }

                });

                imageView_fermat.startAnimation(animation1);



                GetTask getTask = new GetTask(this,this);
                getTask.setCallBack(this);
                getTask.execute();
            }else if (applicationState == ApplicationSession.STATE_STARTED ){
                fermatInit();
            }


    }

    public void handleTouch(View view) {
        fermatInit();
    }

    private boolean fermatInit() {
        Intent intent = new Intent(this, SubAppActivity.class);
        intent.putExtra(START_ACTIVITY_INIT, "init");
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        return true;
    }

    /**
     * implement this function to handle the result object through dynamic array
     *
     * @param result array of native object (handle result field with result[0], result[1],... result[n]
     */
    @Override
    public void onPostExecute(Object... result) {
        PlatformInfoManager platformInfoManager = (PlatformInfoManager) platform.getCorePlatformContext().getAddon(Addons.PLATFORM_INFO);
        setPlatformDeviceInfo(platformInfoManager);
        //mDialog.dismiss();

        imageView_fermat.clearAnimation();

        // Indicate that app was loaded.
        WAS_START_ACTIVITY_LOADED = true;
        fermatInit();
    }

    /**
     * Implement this function to handle errors during the execution of any fermat worker instance
     *
     * @param ex Throwable object
     */
    @Override
    public void onErrorOccurred(Exception ex) {
        //mDialog.dismiss();
        ex.printStackTrace();
        Toast.makeText(getApplicationContext(), "Application crash, re open the app please",
                Toast.LENGTH_LONG).show();
    }

    class GetTask extends FermatWorker{


        public GetTask(Activity activity,FermatWorkerCallBack fermatWorkerCallBack){
            super(activity,fermatWorkerCallBack);
        }


        /**
         * This function is used for the run method of the fermat background worker
         *
         * @throws Exception any type of exception
         */
        @Override
        protected Object doInBackground() throws Exception {


                Context context = getApplicationContext();

                platform = ((ApplicationSession)getApplication()).getFermatPlatform();


                //set Os Addons in platform
                fileSystemOs = new AndroidOsFileSystem(context.getFilesDir().getPath());

                platform.setFileSystemOs(fileSystemOs);

                databaseSystemOs = new AndroidOsDataBaseSystem(context.getFilesDir().getPath());
                platform.setDataBaseSystemOs(databaseSystemOs);

           locationSystemOs = new AndroidOsLocationSystem();
                   locationSystemOs.setContext(context);
                    platform.setLocationSystemOs(locationSystemOs);


                loggerSystemOs = new LoggerAddonRoot();
                try {
                    ((Service) loggerSystemOs).start();
                    platform.setLoggerSystemOs(loggerSystemOs);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Oooops! recovering from system error", Toast.LENGTH_SHORT).show();
                }

                //execute start platform
                try {

                    platform.start();

                } catch (CantStartPlatformException | CantReportCriticalStartingProblemException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Oooops! recovering from system error", Toast.LENGTH_SHORT).show();
                }


                /**
                 * get platform object
                 */

                platformContext = platform.getCorePlatformContext();




                return true;
        }
    }

    private void setPlatformDeviceInfo(PlatformInfoManager platformInfoManager){
        try {
            PlatformInfo platformInfo = platformInfoManager.getPlatformInfo();
            platformInfo.setScreenSize(getScreenSize());
            platformInfoManager.setPlatformInfo(platformInfo);
        } catch (CantLoadPlatformInformationException e) {
            e.printStackTrace();
        }
    }

    private ScreenSize getScreenSize(){

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return DeviceInfoUtils.toScreenSize(dpHeight,dpWidth);

    }
}
