package androsoft.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;


import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.facebook.ads.AdSettings;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements MaxAdListener, MaxAdViewAdListener {
    Button btn1;
    private MaxInterstitialAd interstitialAd;
    private MaxAdView adView;
    private int retryAttempt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ads start
        // Please make sure to set the mediation provider value to "max" to ensure proper functionality
        AppLovinSdk.getInstance( this ).setMediationProvider( "max" );
        AppLovinSdk.initializeSdk( this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration)
            {
                // AppLovin SDK is initialized, start loading ads
            }
        } );


        AdSettings.setDataProcessingOptions( new String[] {} );
        AdSettings.setDataProcessingOptions( new String[] {"LDU"}, 1, 1000 );
        ///
        adView = new MaxAdView( (getString(R.string.banner_home_footer)), this );
        adView.setListener( this );
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int heightPx = getResources().getDimensionPixelSize( R.dimen.banner_height );
        adView.setLayoutParams( new FrameLayout.LayoutParams( width, heightPx ,Gravity.BOTTOM) );
        ViewGroup rootView = findViewById( android.R.id.content );
        rootView.addView( adView );
        adView.loadAd();
        //
        AppLovinSdk.getInstance( this ).showMediationDebugger();

        interstitialAd = new MaxInterstitialAd( (getString(R.string.interstitial_full_screen)), this );
        interstitialAd.setListener( this );
        // Load the first ad
        interstitialAd.loadAd();
        ///end




        btn1 = (Button) findViewById(R.id.button);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, buttnpage.class);
                startActivity(intent);
                if ( interstitialAd.isReady() )
                {
                    interstitialAd.showAd();
                }

            }
        });

    }
//banner



    

    // MAX Ad Listener 2nd
    @Override
    public void onAdLoaded(final MaxAd maxAd)
    {
        retryAttempt = 0;
    }
    @Override
    public void onAdLoadFailed(final String adUnitId, final MaxError error)
    {
        retryAttempt++;
        long delayMillis = TimeUnit.SECONDS.toMillis( (long) Math.pow( 2, Math.min( 6, retryAttempt ) ) );

        new Handler().postDelayed( new Runnable()
        {
            @Override
            public void run()
            {
                interstitialAd.loadAd();
            }
        }, delayMillis );
    }
    @Override
    public void onAdDisplayFailed(final MaxAd maxAd, final MaxError error)
    {
        interstitialAd.loadAd();
    }
    @Override
    public void onAdDisplayed(final MaxAd maxAd) {}
    @Override
    public void onAdClicked(final MaxAd maxAd) {}
    @Override
    public void onAdHidden(final MaxAd maxAd)
    {
        interstitialAd.loadAd();
    }


    @Override
    public void onAdExpanded(MaxAd ad) {

    }
    @Override
    public void onAdCollapsed(MaxAd ad) {
    }
}