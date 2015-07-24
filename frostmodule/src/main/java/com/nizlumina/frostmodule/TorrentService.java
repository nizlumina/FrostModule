package com.nizlumina.frostmodule;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;

import com.frostwire.bittorrent.BTContext;
import com.frostwire.bittorrent.BTEngine;
import com.frostwire.jlibtorrent.DHT;

import java.io.File;

public class TorrentService extends Service
{
    public TorrentService()
    {
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void initBTEngine()
    {
        BTEngine.ctx = new BTContext();
        BTEngine btEngine = BTEngine.getInstance();
        btEngine.reloadBTContext(new File(getFilesDir(), "metafile"), Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), new File(getFilesDir(), "libtorrent"), 0, 0, "0.0.0.0", false, false);

        BTEngine.ctx.optimizeMemory = true;
        btEngine.start();
        DHT dht = new DHT(btEngine.getSession());
        dht.start();
    }
}
