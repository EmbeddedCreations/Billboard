package com.example.billboard;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    String address = "https://embeddedcreation.in/deeGIS/backend/login.php";
    InputStream is = null;
    String line;
    String result;

    String[] uname,pass;

    // Hardcoded username and password for demonstration purposes
    private static final String CORRECT_USERNAME = "admin";
    private static final String CORRECT_PASSWORD = "password";
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Initialize views
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);

        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        getData();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve input values
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                Log.d("uname",uname[1]);
                int index = -1;
                for (int i = 0; i < uname.length; i++) {
                    if (uname[i].equals(username)) {
                        index = i;
                        break;  // Found the target, exit the loop
                    }
                }
                // Validate input
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                } else {
                    // Check username and password
                    if ( password.equals(pass[index])) {
                        // Username and password are correct, proceed to the next activity
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Optional: Finish the login activity so the user can't go back to it
                        Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                    } else if (!password.equals(pass[index])) {
                        // Both username and password are incorrect
                        Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    } else if (index == -1) {
                        // Incorrect username
                        Toast.makeText(Login.this, "Invalid username", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private void getData(){
        try{
            URL url = new URL(address);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            is = new BufferedInputStream(con.getInputStream());
            //Read It Into a String
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            while((line = br.readLine())!= null){
                sb.append(line+"\n");
            }
            is.close();
            result = sb.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            JSONArray js =new JSONArray(result);
            JSONObject jo = null;
            uname = new String[js.length()];
            pass = new String[js.length()];
            for(int i=0;i<js.length();i++){
                jo = js.getJSONObject(i);
                uname[i] = jo.getString("UserName");
                pass[i] = jo.getString("Password");

            }
        }catch(Exception e){

        }
    }


}
