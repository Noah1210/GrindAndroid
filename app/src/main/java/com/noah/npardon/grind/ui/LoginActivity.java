package com.noah.npardon.grind.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.noah.npardon.grind.R;
import com.noah.npardon.grind.beans.User;
import com.noah.npardon.grind.daos.DaoUser;
import com.noah.npardon.grind.daos.DelegateAsyncTask;
import com.noah.npardon.grind.net.TokenManager;

public class LoginActivity extends AppCompatActivity {
    private TextView inputLogin, inputPassword;
    public static User userCurrent = null;
    private ProgressBar progressBar;
    private Button buttonLogin;
    private static Activity activity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inputLogin = findViewById(R.id.txLogin);
        inputPassword = findViewById(R.id.txPassword);
        buttonLogin = findViewById(R.id.btSignIn);
        Button buttonRegister = findViewById(R.id.btLoginSignup);
        progressBar = findViewById(R.id.progBarLogin);
        activity = this;

        TokenManager tokenManager = new TokenManager(this);
        String authToken = tokenManager.getAuthToken();

        if (authToken != null) {
            progressBar.setVisibility(View.VISIBLE);
                DaoUser.getInstance().getLoginWithToken(authToken, new DelegateAsyncTask() {
                    @Override
                    public void whenWSIsTerminated(Object result) {
                        progressBar.setVisibility(View.GONE);
                        User us = (User) result;
                        if (us != null) {
                            Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            finish();
                            LoginActivity.this.startActivity(intent);
                        }
                    }
                });
        }
            buttonRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            buttonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.setVisibility(View.VISIBLE);
                    String login = String.valueOf(inputLogin.getText());
                    String password = String.valueOf(inputPassword.getText());

                    DaoUser.getInstance().getLogin(login, password, new DelegateAsyncTask() {
                        @Override
                        public void whenWSIsTerminated(Object result) {
                            User us = (User) result;
                            if (us != null) {
                                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                userCurrent = us;
                                tokenManager.saveAuthToken(us.getToken());
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                DialogFragment newFragmet = new StartGameDialogFragment();
                                newFragmet.show(getSupportFragmentManager(), "error");
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            });
            inputChange();


    }

    private void inputChange() {
        inputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (inputPassword.getText().length() >= 2 && inputLogin.getText().length() != 0) {
                    buttonLogin.setEnabled(true);
                } else {
                    buttonLogin.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public static class StartGameDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_dialog_layout_login, null);

            Button positiveButton = dialogView.findViewById(R.id.positive_button);
            Button negativeButton = dialogView.findViewById(R.id.negative_button);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(),RegistrationActivity.class);
                    startActivity(intent);
                    dismiss();
                    activity.finish();

                }
            });
            builder.setView(dialogView);

            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}