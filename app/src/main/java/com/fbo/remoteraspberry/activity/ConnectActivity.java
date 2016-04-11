package com.fbo.remoteraspberry.activity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.fbo.remoteraspberry.R;
import com.fbo.remoteraspberry.data.LinkBundle;
import com.fbo.remoteraspberry.internet.Sustainable;
import com.fbo.remoteraspberry.util.ProgressUtils;
import com.fbo.remoteraspberry.util.UriUtils;


public class ConnectActivity extends Activity {

    private void connectRaspberryPi(String ipAdress, Sustainable fragment) {
        final String uri = UriUtils.getServiceUri(ipAdress);
        ServiceActivity.followToLinkList(uri, this, fragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.connect_container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_connect, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.test_connect_eth) {
            connectRaspberryPi("192.168.1.15", (Sustainable) getFragmentManager().findFragmentById(R.id.connect_container));
        } else if (id == R.id.test_connect_wlan) {
            connectRaspberryPi("192.168.1.16", (Sustainable) getFragmentManager().findFragmentById(R.id.connect_container));
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements Sustainable {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_connect, container, false);
        }

        @Override
        public void setSustained(boolean b) {
            ProgressUtils.showProgress(b, getActivity(), getActivity().findViewById(R.id.hello_text));
        }
    }


}
