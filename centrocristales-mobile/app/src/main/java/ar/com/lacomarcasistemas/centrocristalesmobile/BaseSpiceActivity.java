package ar.com.lacomarcasistemas.centrocristalesmobile;

import android.support.v7.app.AppCompatActivity;

import com.octo.android.robospice.SpiceManager;

import ar.com.lacomarcasistemas.centrocristalesmobile.network.CentroCristalesTurnosWSService;
import ar.com.lacomarcasistemas.centrocristalesmobile.network.WarnesWSRestService;

/**
 * Created by Test on 06/06/2016.
 */
public abstract class BaseSpiceActivity extends AppCompatActivity {

    private SpiceManager crmWSManager = new SpiceManager(WarnesWSRestService.class);
    private SpiceManager centroCristalesTurnosWSManager = new SpiceManager(CentroCristalesTurnosWSService.class);

    @Override
    protected void onStart() {
        crmWSManager.start(this);
        centroCristalesTurnosWSManager.start(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        crmWSManager.shouldStop();
        centroCristalesTurnosWSManager.shouldStop();
        super.onStop();
    }

    protected SpiceManager getCrmWSManager() {
        return crmWSManager;
    }

    protected SpiceManager getCentroCristalesTurnosWSManager() {
        return centroCristalesTurnosWSManager;
    }
}
