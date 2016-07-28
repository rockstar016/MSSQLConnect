package connect.dbase.rock.dbasem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ConnectionClass connectionClass;
    Button save_setting, test_setting;
    EditText m_txtServerIP, m_txtSearchPassword;
    Spinner m_password;
    HashMap<String, String> passwordmap;
    String[] password_array, password_array_original;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        save_setting = (Button)findViewById(R.id.button_set_save);
        test_setting = (Button)findViewById(R.id.button_set_test);
        save_setting.setOnClickListener(this);
        test_setting.setOnClickListener(this);
        password_array = getResources().getStringArray(R.array.password_array);
        password_array_original = getResources().getStringArray(R.array.password_array_original);


        m_txtServerIP = (EditText)findViewById(R.id.server_ip);
        m_password = (Spinner)findViewById(R.id.password);
        ArrayAdapter<CharSequence> adapter_state = ArrayAdapter.createFromResource(this,
                R.array.password_array, android.R.layout.simple_spinner_item);
        adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        m_password.setAdapter(adapter_state);

        m_txtSearchPassword = (EditText)findViewById(R.id.search_password);
        settingvalue_load();
        setting_actionbar();
    }
    public void setting_actionbar(){
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Setting");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            Intent i =new Intent(this, MainScreenActivity.class);
            startActivity(i);
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,MainScreenActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_set_save)
        {
            settingvalue_save();
        }
        else if(view.getId() == R.id.button_set_test)
        {
            settingvalue_test();
        }
    }
    private void settingvalue_save() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor Ed = sp.edit();
        Ed.putString("server_ip",m_txtServerIP.getText().toString().trim());
        Ed.putString("password",m_password.getSelectedItem().toString().trim());
        Ed.putString("searchpassword",m_txtSearchPassword.getText().toString().trim());
        Ed.putString("password_original",password_array_original[m_password.getSelectedItemPosition()]);
        Ed.commit();
        Toast.makeText(MainActivity.this, "Success to save", Toast.LENGTH_SHORT).show();
    }
    private void settingvalue_load(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        m_txtServerIP.setText(sp.getString("server_ip",""));
        String password = sp.getString("password","");
        for (int i = 0 ; i < m_password.getAdapter().getCount(); i++)
        {
            if(m_password.getItemAtPosition(i).toString().equals(password)) {
                m_password.setSelection(i);
                break;
            }
        }
        m_txtSearchPassword.setText(sp.getString("searchpassword",""));
    }
    private void settingvalue_test() {
        if(m_txtServerIP.getText().toString().trim().isEmpty() == false)
        {
            connectionClass = new ConnectionClass();
            connectionClass.setIPAddress(m_txtServerIP.getText().toString().trim());
            connectionClass.setDBPassword(password_array_original[m_password.getSelectedItemPosition()]);
            DoLogin doLogin = new DoLogin();
            doLogin.execute("");
        }
        else{
            Toast.makeText(MainActivity.this, "Input ServerIP", Toast.LENGTH_SHORT).show();
        }
    }
    public class DoLogin extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
        @Override
        protected void onPreExecute() {
            Log.d("pre-excute","pre-execute");
        }
        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(MainActivity.this,r,Toast.LENGTH_SHORT).show();
            if(isSuccess) {
                Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();                
            }
        }
        @Override
        protected String doInBackground(String... params) {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {
                        String query = "SELECT * FROM CLIENT_TBL";//='" + userid + "' and password='" + password + "'";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        if(rs != null)//if(rs.next())
                        {
                            z = "Login successfull";
                            isSuccess=true;
                        }
                        else
                        {
                            z = "Invalid Credentials";
                            isSuccess = false;
                        }
                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    z = "Exceptions";
                }
            return z;
        }
    }
}
