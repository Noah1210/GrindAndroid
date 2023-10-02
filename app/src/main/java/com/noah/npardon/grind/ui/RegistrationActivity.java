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
import android.util.Log;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {
    private TextView inputLogin, inputEmail, inputPassword, tvEmailError, tvLoginError, tvPasswordError;
    public static User userCurrent = null;
    private ProgressBar progressBar;
    private Button buttonRegister;
    private static Activity activity = null;
    private boolean isAtLeast2 = false, hasUpperCase = false, hasNumber = false, isRegistrationClickable = false;
    private String login, email;
    private Matcher matcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        inputLogin = findViewById(R.id.txRegisterLogin);
        inputPassword = findViewById(R.id.txRegisterPassword);
        inputEmail = findViewById(R.id.txRegisterEmail);
        buttonRegister = findViewById(R.id.btRegister);
        Button buttonLogin = findViewById(R.id.btRegisterLogin);
        progressBar = findViewById(R.id.progBarLogin);
        tvEmailError = findViewById(R.id.tvEmailError);
        tvLoginError = findViewById(R.id.tvLoginError);
        tvPasswordError = findViewById(R.id.tvPasswordError);
        activity = this;
        TokenManager tokenManager = new TokenManager(this);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String login = String.valueOf(inputLogin.getText());
                String password = String.valueOf(inputPassword.getText());
                String email = String.valueOf(inputEmail.getText());
                if (login.length() > 0 && password.length() > 0 && email.length() > 0) {
                    if (isRegistrationClickable) {
                        DaoUser.getInstance().getRegister(login, password, email, new DelegateAsyncTask() {
                            @Override
                            public void whenWSIsTerminated(Object result) {
                                User us = (User) result;
                                if (us != null) {
                                    Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                    tokenManager.saveAuthToken(us.getToken());
                                    userCurrent = us;
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    DialogFragment newFragmet = new RegistrationActivity.StartGameDialogFragment();
                                    newFragmet.show(getSupportFragmentManager(), "error");
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        if (login.length() == 0) {
                            tvLoginError.setVisibility(View.VISIBLE);
                            tvLoginError.setText("Please enter a username");
                        }
                        if (password.length() == 0) {
                            tvPasswordError.setVisibility(View.VISIBLE);
                            tvPasswordError.setText("Please enter a password");
                        }
                        if (email.length() == 0) {
                            tvEmailError.setVisibility(View.VISIBLE);
                            tvEmailError.setText("Please enter a mail");
                        }

                    }
                }


            }
        });
        inputChange();
    }

    private void checkPassword() {
        String password = String.valueOf(inputPassword.getText());
        if (password.length() >= 2) {
            isAtLeast2 = true;
        } else {
            isAtLeast2 = false;
            tvPasswordError.setVisibility(View.VISIBLE);
            tvPasswordError.setText("The password has to be at least 2 characters");
        }
        if (password.matches("(.*[A-Z].*)")) {
            hasUpperCase = true;
        } else {
            hasUpperCase = false;
            tvPasswordError.setVisibility(View.VISIBLE);
            tvPasswordError.setText("The password has to contain at least one uppercase");
        }
        if (password.matches((".*[0-9].*"))) {
            hasNumber = true;
        } else {
            hasNumber = false;
            tvPasswordError.setVisibility(View.VISIBLE);
            tvPasswordError.setText("The password has to contain at least one number");
        }
        if(isAtLeast2 & hasNumber && hasUpperCase){
            tvPasswordError.setVisibility(View.GONE);
        }

    }

    private void checkAllData(String email, String login) {
        if (isAtLeast2 && hasNumber && hasUpperCase && inputEmail.length() > 0 && inputLogin.length() > 5 && matcher.matches()) {
            Log.d("TAG", "checkAllData: Shit goin crazy");
            isRegistrationClickable = true;
            buttonRegister.setEnabled(true);
        } else {
            isRegistrationClickable = false;
            buttonRegister.setEnabled(false);
        }
    }

    private void inputChange() {
        inputLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                login = String.valueOf(inputLogin.getText());
                if (login.length() > 0) {
                    tvLoginError.setVisibility(View.GONE);
                }else {
                    tvLoginError.setVisibility(View.VISIBLE);
                    tvLoginError.setText("Please enter a username");
                }
                checkAllData(email, login);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkPassword();
                checkAllData(email, login);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                email = String.valueOf(inputEmail.getText());
                String regexPattern = "^(.+)@(\\S+)$";
                Pattern pattern = Pattern.compile(regexPattern);
                matcher = pattern.matcher(email);
                if (email.length() > 0 && matcher.matches()) {
                    tvEmailError.setVisibility(View.GONE);
                }else {
                    tvEmailError.setVisibility(View.VISIBLE);
                    tvEmailError.setText("Please enter a valid email");
                }
                checkAllData(email, login);
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
            View dialogView = inflater.inflate(R.layout.custom_dialog_layout_registration, null);

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
                    Intent intent = new Intent(getContext(),LoginActivity.class);
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