package com.fbo.remoteraspberry.internet;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.fbo.remoteraspberry.activity.ConnectActivity;
import com.fbo.remoteraspberry.ui.AlertDialogUtils;

/**
 * Created by Fred on 28/06/2015.
 */
public abstract class GenericWebAsyncTask<I, O> {

    private AsyncTask<Void, Void, O> asyncTask;

    protected abstract O remoteServiceAccess(final I input);

    protected abstract void treatResult(final O output);

    private final Sustainable sustainable;
    private final Context context;
    private final I inputObject;
    private boolean hasBeenAlreadyLaunched = false;
    private boolean throwedAnException = false;
    private boolean mustTreatResult = false;


    protected GenericWebAsyncTask(Sustainable sustainable, Context context, I inputObject) {
        this.sustainable = sustainable;
        this.context = context;
        this.inputObject = inputObject;
    }


    public final void execute() {
        if (!hasBeenAlreadyLaunched) {
            if (checkInternetConnectivity()) {
                sustainable.setSustained(true);
                this.asyncTask = new AsyncTask<Void, Void, O>() {
                    @Override
                    protected O doInBackground(Void... voids) {
                        try {
                            mustTreatResult = true;
                            return remoteServiceAccess(inputObject);
                        } catch (Exception e) {
                            throwedAnException = true;
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(O o) {
                        if (throwedAnException) {
                            AlertDialogUtils.showAlertDialog(context, "Serveur inaccessible",
                                    "Le serveur est temporairement inaccessible.\n"
                                            + "Merci de bien vouloir signaler ce problème à :\n"
                                            + "frederic.boisguerin@gmail.com");
                        } else {
                            try {
                                if (mustTreatResult) {
                                    treatResult(o);
                                }
                            } catch (Exception e) {
                                AlertDialogUtils.showAlertDialog(context, "Erreur inconnue");
                                e.printStackTrace();
                            } finally {
                                sustainable.setSustained(false);
                            }
                        }
                        sustainable.setSustained(false);
                    }
                };
                this.asyncTask.execute();
            }
        }
        hasBeenAlreadyLaunched = true;
    }

    /*
    INTERNET CONNECTIVITY
     */

    private boolean checkInternetConnectivity() {
        if (isConnectedToInternet()) {
            return true;
        } else {
            AlertDialogUtils.showAlertDialog(context, "Aucune connectivité Internet", "Activez votre réseau de données (mobile ou Wifi) avant de continuer");
            return false;
        }
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
