package com.mobimation.storymaker;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * DeliveryService manages the transport of user created media artifacts
 * to a central repository. It works on a backlog of not yet transferred
 * material where transfers that become interrupted by power issues or
 * connectivity loss are retried until successful whenever the DeliveryService
 * is up and running. It is intended that a backlog editor be implemented that
 * allow user clearing of artifacts, remote or local that the user decide not to transfer.
 */
public class DeliveryService extends Service {
    public DeliveryService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
