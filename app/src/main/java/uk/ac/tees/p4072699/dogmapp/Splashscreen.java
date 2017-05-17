package uk.ac.tees.p4072699.dogmapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class Splashscreen extends Activity {
    private ProgressBar spinner;
    private ImageView iv;
    private LinearLayout l;
    private Animation anim;

    /*Gets the window, sets the format, created the Thread, and
    * starts the animations for the Splash images, the progress
    * bar is displayed here.*/
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    /**
     * Called when the activity is first created.
     */
    Thread splashTread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        StartAnimations();
        spinner = (ProgressBar) findViewById(R.id.progressBar);
    }

    private void StartAnimations() {
        anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        l = (LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        iv = (ImageView) findViewById(R.id.splash);
        iv.clearAnimation();
        iv.startAnimation(anim);

        /*Logic for the thread start for Splash, if the wait time has been less than 3500ms,
        * the sleep value is 100 and waited is increased by 100, as well as the progress
        * bar value.*/
        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    int progress = 0;
                    // Splash screen pause time
                    while (waited < 3500) {

                        sleep(100);
                        spinner.setProgress(progress);
                        waited += 100;
                        progress += 3;
                    }
                    Intent intent = new Intent(Splashscreen.this,
                            Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    Splashscreen.this.finish();
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    Splashscreen.this.finish();
                }
            }
        };
        splashTread.start();
    }
}