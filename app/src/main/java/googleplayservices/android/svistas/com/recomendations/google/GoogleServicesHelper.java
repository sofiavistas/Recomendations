package googleplayservices.android.svistas.com.recomendations.google;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by svistas on 19/12/16.
 */

public class GoogleServicesHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public interface GoogleServicesListener{
        public void onConnected();
        public void onDisconnected();
    }

    private static final int REQUEST_CODE_RESOLUTION = -100;
    private static final int REQUEST_CODE_AVAILABILITY = -101;

    private Activity activity;
    private GoogleServicesListener listener;
    private GoogleApiClient apiClient;

    public GoogleServicesHelper(Activity activity, GoogleServicesListener listener){
        this.activity = activity;
        this.listener = listener;

        this.apiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public void connect(){
        apiClient.connect();
    }

    public void disconnect(){
        apiClient.disconnect();
    }

    public boolean isGooglePlayServicesAvailable(){
        int availability = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);

        switch(availability){
            case ConnectionResult.SUCCESS:
                return true;
            case ConnectionResult.SERVICE_DISABLED:
            case ConnectionResult.SERVICE_INVALID:
            case ConnectionResult.SERVICE_MISSING:
                GooglePlayServicesUtil.getErrorDialog(availability,activity,REQUEST_CODE_AVAILABILITY).show();
                return false;
            default:
                return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}



