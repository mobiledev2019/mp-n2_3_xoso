package com.android.xoso;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.security.PrivateKey;

public class RegisterActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;
    private EditText mConfirmPassword;

    private View mRegisterFormView;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmail = findViewById(R.id.register_email);
        mPassword = findViewById(R.id.register_password);
        mConfirmPassword = findViewById(R.id.register_confirm_password);

        Button mRegisterButton = findViewById(R.id.email_register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });

        mRegisterFormView = findViewById(R.id.email_register_form);
        mProgressView = findViewById(R.id.register_progress);
    }


    public void attemptRegister() {

        mEmail.setError(null);
        mPassword.setError(null);
        mConfirmPassword.setError(null);

        View focusView = null;

        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        String confirm_password = mConfirmPassword.getText().toString();


        if (TextUtils.isEmpty(email)) {
            mEmail.setError("Email không được để trống!");
            mEmail.requestFocus();
            return;
        } else if (!isValidEmail(email)) {
            mEmail.setError("Email không hợp lệ");
            mEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            mPassword.setError("Password không được để trống");
            mPassword.requestFocus();
            return;
        } else if (!isValidPassword(password)) {
            mPassword.setError("Password phải lớn hơn 5 ký tự");
            mPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(confirm_password)) {
            mConfirmPassword.setError("Confirm password không được để trống");
            mConfirmPassword.requestFocus();
            return;
        } else if (!isValidPassword(confirm_password)) {
            mConfirmPassword.setError("Confirm password phải lớn hơn 5 ký tự");
            mConfirmPassword.requestFocus();
            return;

        } else if (!isMatchPassword(password, confirm_password)) {
            mConfirmPassword.setError("Confirm Password không giống password");
            mConfirmPassword.requestFocus();
            return;
        }

        showProgress(true);
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);


    }

    public boolean isValidEmail(String email) {
        return email.contains("@");
    }

    public boolean isValidPassword(String password) {
        return password.length() > 6;
    }

    public boolean isMatchPassword(String password, String confirm_password) {
        return confirm_password.equals(password);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


}
