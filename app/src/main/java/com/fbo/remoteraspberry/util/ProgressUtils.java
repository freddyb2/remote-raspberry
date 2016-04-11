package com.fbo.remoteraspberry.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.Build;
import android.view.View;

import com.fbo.remoteraspberry.R;

/**
 * Created by Fred on 11/11/2014.
 */
public class ProgressUtils {

    public static void showProgress(final boolean show, final Activity activity, final View formView) {
        showProgress(show, activity, formView, activity.findViewById(R.id.progress_view));
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private static void showProgress(final boolean show, final Activity activity, final View formView, final View progressView) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = activity.getResources().getInteger(android.R.integer.config_shortAnimTime);

            if (formView != null) {
                formView.setVisibility(show ? View.GONE : View.VISIBLE);
                formView.animate().setDuration(shortAnimTime).alpha(
                        show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        formView.setVisibility(show ? View.GONE : View.VISIBLE);
                    }
                });
            }

            if (progressView != null){
                progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                progressView.animate().setDuration(shortAnimTime).alpha(
                        show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                    }
                });
            }
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            if (formView != null) {
                formView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
            if (progressView != null){
                progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }

        }
    }
}
