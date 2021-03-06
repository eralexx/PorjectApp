package app.movie.tutorial.com.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import app.movie.tutorial.com.R;
import app.movie.tutorial.com.rest.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    private EditText mUser;
    private EditText mPassWord;
    private EditText mEmail;
    private static Retrofit retrofit = null;
    public static final String BASE_URL = "http://2.152.165.114:80/rest/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void ExecuteRegister(View view) {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        ApiService ApiService = retrofit.create(ApiService.class);

        mUser = (EditText) findViewById(R.id.user_text);
        mPassWord = (EditText) findViewById(R.id.password_text);
        mEmail =  (EditText) findViewById(R.id.email_text) ;

        if (!mEmail.getText().toString().equals("") || !mUser.getText().toString().equals("") || mPassWord.getText().toString().equals("") ) {
            Call<Integer> call = ApiService.Register(mEmail.getText().toString(),mUser.getText().toString(), mPassWord.getText().toString());

            call.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    try {

                        int registerCheck = response.body() != null ? response.body().intValue() : -1;

                        if (registerCheck == -1) {
                            showAlertDialog("Warning", "User not found");
                        }
                        else {
                            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                            intent.putExtra("userEmail", mUser.getText().toString());
                            startActivity(intent);
                        }
                        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(intent);
                    } catch (Exception ex) {
                        Log.d("1",ex.getMessage());
                        showAlertDialog("Warning", "Login error.");
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable throwable) {
                    Log.d("1",throwable.getMessage());
                    Log.d("2", call.toString());
                    showAlertDialog("Warning", "Conectivity error.");

                }
            });
        } else {
            showAlertDialog("Warning", "Please fill the form.");
        }
    }
    public void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
