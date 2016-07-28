package connect.dbase.rock.dbasem;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class MainScreenActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    EditText txt_search_mobile, txt_search_lastname;
    EditText txt_first_name, txt_last_name, txt_address1, txt_address2, txt_suburb, txt_post_code, txt_email_address, txt_phone_number, txt_work, txt_mobile_number, txt_birthday, txt_refer_by;
    CheckBox m_chk_sms_appointment, m_chk_sms_marketing, m_chk_mail;
    ConnectionClass connectionClass;
    Spinner m_spinner_gender, m_spinner_state, m_spinner_title, m_spinner_clients;
    ArrayList<UserModel> m_users_search_result;
    SearchResultAdapter m_search_adapter;
    UserModel m_current_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        m_users_search_result = new ArrayList<>();
        setInitUI();
    }
    private  void setInitUI(){
        Button m_bt_search = (Button)findViewById(R.id.bt_search);
        m_bt_search.setOnClickListener(this);
        Button m_bt_setting = (Button)findViewById(R.id.bt_setting);
        m_bt_setting.setOnClickListener(this);
        Button m_bt_clear = (Button)findViewById(R.id.bt_clear);
        m_bt_clear.setOnClickListener(this);
        Button m_bt_submit = (Button)findViewById(R.id.bt_submit);
        m_bt_submit.setOnClickListener(this);
        txt_search_lastname = (EditText)findViewById(R.id.txt_search_lastname);
        txt_search_mobile = (EditText)findViewById(R.id.txt_search_mobile);
        m_spinner_gender = (Spinner)findViewById(R.id.spin_gender);
        ArrayAdapter<CharSequence> adapter_gender = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        m_spinner_gender.setAdapter(adapter_gender);
        m_spinner_state = (Spinner)findViewById(R.id.spin_state);
        ArrayAdapter<CharSequence> adapter_state = ArrayAdapter.createFromResource(this,
                R.array.state_array, android.R.layout.simple_spinner_item);
        adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        m_spinner_state.setAdapter(adapter_state);
        m_spinner_title = (Spinner)findViewById(R.id.spin_title);
        ArrayAdapter<CharSequence> adapter_title = ArrayAdapter.createFromResource(this,
                R.array.title_array, android.R.layout.simple_spinner_item);
        adapter_title.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        m_spinner_title.setAdapter(adapter_title);
        m_spinner_clients = (Spinner)findViewById(R.id.spin_search_result);
        m_spinner_clients.setVisibility(View.GONE);
        m_search_adapter = new SearchResultAdapter(this);
        m_spinner_clients.setAdapter(m_search_adapter);
        m_spinner_clients.setOnItemSelectedListener(this);
        txt_address1 = (EditText)findViewById(R.id.txt_address_1);
        txt_address2 = (EditText)findViewById(R.id.txt_address_2);
        txt_birthday = (EditText)findViewById(R.id.txt_birth_day);
        txt_email_address = (EditText)findViewById(R.id.txt_email);
        txt_first_name = (EditText)findViewById(R.id.txt_first_name);
        txt_last_name = (EditText)findViewById(R.id.txt_last_name);
        txt_mobile_number = (EditText)findViewById(R.id.txt_mobile);
        txt_phone_number = (EditText)findViewById(R.id.txt_phone);
        txt_post_code = (EditText)findViewById(R.id.txt_postcode);
        txt_refer_by = (EditText)findViewById(R.id.txt_refer);
        txt_suburb = (EditText)findViewById(R.id.txt_suburb);
        txt_work = (EditText)findViewById(R.id.txt_work);
        m_chk_mail = (CheckBox)findViewById(R.id.chk_mail);
        m_chk_sms_appointment = (CheckBox)findViewById(R.id.chk_sms_appoint);
        m_chk_sms_marketing = (CheckBox)findViewById(R.id.chk_sms_marketing);
        prepareConnection();
//        InputMethodManager im = (InputMethodManager) this.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        im.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        m_current_user =(UserModel) m_search_adapter.getItem(i);
        setTitleSpinnerBasedSearchResult(m_current_user.getTitle());
        setStateBasedSearchResult(m_current_user.getState());
        setGenderBasedSearchResult(m_current_user.getGender());
        if(m_current_user.getSMS_Appointment_Reminders() == 1){
            m_chk_sms_appointment.setChecked(true);
        }
        else{
            m_chk_sms_appointment.setChecked(false);
        }

        if(m_current_user.getSMS_Marketing() == 1){
            m_chk_sms_marketing.setChecked(true);
        }
        else{
            m_chk_sms_marketing.setChecked(false);
        }

        if(m_current_user.getMail() == 1){
            m_chk_mail.setChecked(true);
        }
        else{
            m_chk_mail.setChecked(false);
        }
        txt_first_name.setText(m_current_user.getFirst_Name());
        txt_last_name.setText(m_current_user.getLast_Name());
        txt_address1.setText(m_current_user.getAddress());
        txt_address2.setText(m_current_user.getAddress_2());
        txt_suburb.setText(m_current_user.getSuburb());
        txt_post_code.setText(m_current_user.getPost_Code());
        txt_email_address.setText(m_current_user.getEmail());
        txt_phone_number.setText(m_current_user.getPhone());
        txt_work.setText(m_current_user.getWork());
        txt_mobile_number.setText(m_current_user.getMobile());
        txt_birthday.setText(m_current_user.getBirthday());
        txt_refer_by.setText(m_current_user.getReferred_By());
    }
    public void setCurrentUserModelFromUI(){
        m_current_user.setFirst_Name(txt_first_name.getText().toString());
        m_current_user.setLast_Name(txt_last_name.getText().toString());
        m_current_user.setAddress(txt_address1.getText().toString());
        m_current_user.setAddress_2(txt_address2.getText().toString());
        m_current_user.setSuburb(txt_suburb.getText().toString());
        m_current_user.setPost_Code(txt_post_code.getText().toString());
        m_current_user.setEmail(txt_email_address.getText().toString());
        m_current_user.setPhone(txt_phone_number.getText().toString());
        m_current_user.setWork(txt_work.getText().toString());
        m_current_user.setMobile(txt_mobile_number.getText().toString());
        m_current_user.setBirthday(txt_birthday.getText().toString());
        m_current_user.setReferred_By(txt_refer_by.getText().toString());
        m_current_user.setState(m_spinner_state.getSelectedItem().toString());
        m_current_user.setGender(m_spinner_gender.getSelectedItem().toString());
        m_current_user.setTitle(m_spinner_title.getSelectedItem().toString());
        if(m_chk_sms_appointment.isChecked()){
            m_current_user.setSMS_Appointment_Reminders(1);
        }
        else{
            m_current_user.setSMS_Appointment_Reminders(0);
        }
        if(m_chk_sms_marketing.isChecked()){
            m_current_user.setSMS_Marketing(1);
        }
        else{
            m_current_user.setSMS_Marketing(0);
        }
        if(m_chk_mail.isChecked()) {
            m_current_user.setMail(1);
        }
        else{
            m_current_user.setMail(0);
        }
    }
    public void setCurrentUserModelFromUI(int current_userId, int current_shopId){
        m_current_user = new UserModel();
        m_current_user.setClishop(current_shopId);
        m_current_user.setCliid(current_userId);
        m_current_user.setFirst_Name(txt_first_name.getText().toString());
        m_current_user.setLast_Name(txt_last_name.getText().toString());
        m_current_user.setAddress(txt_address1.getText().toString());
        m_current_user.setAddress_2(txt_address2.getText().toString());
        m_current_user.setSuburb(txt_suburb.getText().toString());
        m_current_user.setPost_Code(txt_post_code.getText().toString());
        m_current_user.setEmail(txt_email_address.getText().toString());
        m_current_user.setPhone(txt_phone_number.getText().toString());
        m_current_user.setWork(txt_work.getText().toString());
        m_current_user.setMobile(txt_mobile_number.getText().toString());
        m_current_user.setBirthday(txt_birthday.getText().toString());
        m_current_user.setReferred_By(txt_refer_by.getText().toString());
        m_current_user.setState(m_spinner_state.getSelectedItem().toString());
        m_current_user.setGender(m_spinner_gender.getSelectedItem().toString());
        m_current_user.setTitle(m_spinner_title.getSelectedItem().toString());
        if(m_chk_sms_appointment.isChecked()){
            m_current_user.setSMS_Appointment_Reminders(1);
        }
        else{
            m_current_user.setSMS_Appointment_Reminders(0);
        }
        if(m_chk_sms_marketing.isChecked()){
            m_current_user.setSMS_Marketing(1);
        }
        else{
            m_current_user.setSMS_Marketing(0);
        }
        if(m_chk_mail.isChecked()) {
            m_current_user.setMail(1);
        }
        else{
            m_current_user.setMail(0);
        }
    }
    private void setTitleSpinnerBasedSearchResult(String value){
        for (int i = 0 ; i < m_spinner_title.getAdapter().getCount(); i++)
        {
            if(m_spinner_title.getItemAtPosition(i).toString().equals(value)) {
                m_spinner_title.setSelection(i);
                break;
            }
        }
    }
    private void setStateBasedSearchResult(String value){
        for (int i = 0 ; i < m_spinner_state.getAdapter().getCount(); i++)
        {
            if(m_spinner_state.getItemAtPosition(i).toString().equals(value)) {
                m_spinner_state.setSelection(i);
                break;
            }
        }
    }
    private void setGenderBasedSearchResult(String value){
        if(value.equals("M")){
            m_spinner_gender.setSelection(0);
        }
        else{
            m_spinner_gender.setSelection(1);
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    private void prepareConnection(){
        connectionClass = new ConnectionClass();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String m_db_password = sp.getString("password_original", "");
        String m_server_ip = sp.getString("server_ip","");
        if(m_db_password.trim().isEmpty() && m_server_ip.trim().isEmpty())
        {
            Toast.makeText(MainScreenActivity.this, "Set server ip and password, please", Toast.LENGTH_SHORT).show();
        }
        else
        {
            connectionClass.setDBPassword(m_db_password);
            connectionClass.setIPAddress(m_server_ip);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.bt_search:
                onClickSearch();
                break;
            case R.id.bt_setting:
                onClickSetting();
                break;
            case R.id.bt_clear:
                onClickClear();
                break;
            case R.id.bt_submit:
                onClickSubmit();
                break;
            default:

        }
    }
    private void onClickSubmit() {
        if(txt_first_name.getText().toString().trim().isEmpty() || txt_last_name.getText().toString().trim().isEmpty()
                || txt_address1.getText().toString().trim().isEmpty() || txt_suburb.getText().toString().trim().isEmpty() || txt_post_code.getText().toString().trim().isEmpty()
                || txt_mobile_number.getText().toString().trim().isEmpty()){
            Toast.makeText(MainScreenActivity.this, "Input all data fields with asteria, please.", Toast.LENGTH_SHORT).show();
        }
        else{
            if(txt_search_lastname.getText().toString().trim().isEmpty() && txt_search_mobile.getText().toString().trim().isEmpty()){
                  DoInsert m_insert_engine = new DoInsert();
                  m_insert_engine.execute();
            }
            else{
                setCurrentUserModelFromUI();
                DoUpdate m_update_engine = new DoUpdate();
                m_update_engine.execute();
            }
        }
    }
    private void onClickClear() {
//        String m_tmp = txt_search_lastname.getText().toString();
//        txt_search_lastname.setText(convertStringToWordArray(m_tmp));
        OnupdateResult();
    }
    private void onClickSetting() {
        Dialog m_dialog = new ConfirmPasswordDialog(this, CommonDatas.SETTING_PASSWORD);
        m_dialog.show();
    }
    private void onClickSearch() {
        if(txt_search_mobile.getText().toString().trim().isEmpty() && txt_search_lastname.getText().toString().trim().isEmpty()){
            Toast.makeText(MainScreenActivity.this, "Fill out the search field", Toast.LENGTH_SHORT).show();
        }
        else {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String m_search_password = sp.getString("searchpassword", "");
            if (m_search_password.trim().isEmpty() == false) {
                Dialog m_dialog = new ConfirmPasswordDialog(this, CommonDatas.SEARCH_PASSWORD);
                m_dialog.show();
            }
            else{
                setResultDialogForSearch(CommonDatas.PASSWORD_RESULT_OK);
            }
        }
    }
    public void setResultDialogForSearch(int result_value){
        if(result_value == CommonDatas.PASSWORD_RESULT_NO){
            Toast.makeText(MainScreenActivity.this, "Search Password is invalid", Toast.LENGTH_SHORT).show();
        }
        else if(result_value == CommonDatas.PASSWORD_RESULT_OK){
            m_users_search_result.clear();
            DoSearch m_search_engine = new DoSearch();
            m_search_engine.execute();

        }
    }
    public void setResultDialogForSetting(int result_value){
        if(result_value == CommonDatas.PASSWORD_RESULT_NO){
            Toast.makeText(MainScreenActivity.this, "Setting Password is invalid", Toast.LENGTH_SHORT).show();
        }
        else if(result_value == CommonDatas.PASSWORD_RESULT_OK){
            Intent i  = new Intent(MainScreenActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }
    public void OnsearchResult(){
        m_spinner_clients.setAdapter(m_search_adapter);
        m_search_adapter.notifyDataSetChanged();
        m_spinner_clients.setVisibility(View.VISIBLE);
    }
    public void OnupdateResult(){
        m_users_search_result = new ArrayList<UserModel>();
        m_search_adapter.notifyDataSetChanged();
        m_spinner_clients.setVisibility(View.GONE);
        m_current_user = new UserModel();
        txt_search_mobile.setText("");
        txt_search_lastname.setText("");
        
        txt_first_name.setText("");
        txt_last_name.setText("");
        txt_address1.setText("");
        txt_address2.setText("");
        txt_suburb.setText("");

        txt_post_code.setText("");
        txt_email_address.setText("");
        txt_phone_number.setText("");
        txt_work.setText("");
        txt_mobile_number.setText("");

        txt_birthday.setText("");
        txt_refer_by.setText("");
        m_spinner_state.setSelection(0);
        m_spinner_gender.setSelection(0);
        m_spinner_title.setSelection(0);

        m_chk_sms_appointment.setChecked(false);
        m_chk_sms_marketing.setChecked(false);
        m_chk_mail.setChecked(false);

    }
    public class DoSearch extends AsyncTask<String,String,String> {
        String z = "";
        Boolean isSuccess = false;
        String search_mobile = txt_search_mobile.getText().toString().trim();
        String search_last = txt_search_lastname.getText().toString().trim();
        @Override
        protected void onPreExecute() {
            Log.d("pre-excute","pre-execute");
        }
        @Override
        protected void onPostExecute(String r) {
            if(isSuccess) {
                        OnsearchResult();
            }
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    String query = "SELECT * FROM CLIENT_TBL";
                    if(search_mobile.isEmpty() == false){
                        query += " WHERE MOBILE = '" + search_mobile + "'";
                        if(search_last.isEmpty() == false){
                            query += " and LAST = '" +search_last+"'";
                        }
                    }
                    else{
                        if(search_last.isEmpty() == false){
                            query += " WHERE LAST = '" + search_last+"'";
                        }
                    }
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if(rs == null) {
                        isSuccess =false;
                    }
                    else{
                        isSuccess = true;
                    }
                    while(rs.next()){
                        UserModel m_model = new UserModel();
                        m_model.setTitle(rs.getString("TITLE"));
                        m_model.setFirst_Name(rs.getString("FIRST"));
                        m_model.setLast_Name(rs.getString("LAST"));
                        m_model.setAddress(rs.getString("ADDRESS1"));
                        m_model.setAddress_2(rs.getString("ADDRESS2"));
                        m_model.setSuburb(rs.getString("SUBURB"));
                        m_model.setState(rs.getString("STATE"));
                        m_model.setPost_Code(rs.getString("PCODE"));
                        m_model.setEmail(rs.getString("E_MAIL"));
                        m_model.setPhone(rs.getString("PHONEH"));
                        m_model.setWork(rs.getString("PHONEW"));
                        m_model.setMobile(rs.getString("MOBILE"));
                        m_model.setGender(rs.getString("GENDER"));
                        m_model.setBirthday(rs.getString("BIRTHDAY"));
                        m_model.setReferred_By(rs.getString("REFERRED_BY"));
                        m_model.setSMS_Appointment_Reminders(rs.getInt("NO_AUTO_SMS"));
                        m_model.setSMS_Marketing(rs.getInt("NO_SMS"));
                        m_model.setMail(rs.getInt("NO_MAILINGS"));
                        m_model.setCliid(rs.getInt("CLI_ID"));
                        m_model.setClishop(rs.getInt("CLI_SHOP"));
                        m_users_search_result.add(m_model);
                    }
                    rs.close();
                    stmt.close();
                    con.close();
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
    public class DoUpdate extends AsyncTask<String,String,String> {
        String z = "";
        Boolean isSuccess = false;
        @Override
        protected void onPreExecute() {
            Log.d("pre-excute","pre-execute");
        }
        @Override
        protected void onPostExecute(String r) {
            if(isSuccess) {
                OnupdateResult();
                Toast.makeText(MainScreenActivity.this, "Your details have been updated successfully", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    String sql_query = "UPDATE CLIENT_TBL SET TITLE = '"+m_current_user.getTitle().toString()+"', ";
                    sql_query += "FIRST = '"+m_current_user.getFirst_Name().toString()+"', ";
                    sql_query += "LAST = '"+m_current_user.getLast_Name().toString()+"', ";
                    sql_query += "ADDRESS1 = '"+m_current_user.getAddress().toString()+"', ";
                    sql_query += " ADDRESS2 = '"+m_current_user.getAddress_2().toString()+"', ";
                    sql_query += " SUBURB = '"+m_current_user.getSuburb().toString()+"', ";
                    sql_query += " STATE = '"+m_current_user.getState().toString()+"', ";
                    sql_query += " PCODE = '"+m_current_user.getPost_Code().toString()+"', ";
                    sql_query += " E_MAIL = '"+m_current_user.getEmail().toString()+"', ";
                    sql_query += " PHONEH = '"+m_current_user.getPhone().toString()+"', ";
                    sql_query += " PHONEW = '"+m_current_user.getWork().toString()+"', ";
                    sql_query += " MOBILE = '"+m_current_user.getMobile().toString()+"', ";
                    sql_query += " GENDER = '"+m_current_user.getGender().toString()+"', ";
                    if(m_current_user.getBirthday().toString().isEmpty())
                        sql_query += "BIRTHDAY = NULL,";
                    else
                        sql_query += " BIRTHDAY = '"+m_current_user.getBirthday().toString()+"', ";

                    sql_query += " REFERRED_BY = '"+m_current_user.getReferred_By().toString()+"', ";
                    sql_query += " NO_AUTO_SMS = "+m_current_user.getSMS_Appointment_Reminders()+", ";
                    sql_query += " NO_SMS = "+m_current_user.getSMS_Marketing()+", ";
                    sql_query += " NO_MAILINGS = "+m_current_user.getMail()+" ";
                    sql_query += "WHERE CLI_ID = "+m_current_user.getCliid()+" and ";
                    sql_query += "CLI_SHOP = " + m_current_user.getClishop() + ";";
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(sql_query);
                    z = "success";
                    isSuccess = true;
                    stmt.close();
                    con.close();
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
    public class DoInsert extends AsyncTask<String,String,String> {
        String z = "";
        Boolean isSuccess = false;
        @Override
        protected void onPreExecute() {
            Log.d("pre-excute","pre-execute");
        }
        @Override
        protected void onPostExecute(String r) {
            if(isSuccess) {
                OnupdateResult();
                Toast.makeText(MainScreenActivity.this, "Your details have been updated successfully", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    int max_cli_id, max_shop_id;
                    String sql_get_max_query = "select max(CLI_ID) as maxcli_id, max(CLI_SHOP) as cli_shop_id from CLIENT_TBL";
                    Statement statement = con.createStatement();
                    ResultSet rs_set = statement.executeQuery(sql_get_max_query);
                    max_cli_id = 0;
                    max_shop_id = 1;
                    while(rs_set.next()){
                        max_cli_id = rs_set.getInt("maxcli_id");
                        max_shop_id = rs_set.getInt("cli_shop_id");
                    }
                    rs_set.close();
                    statement.close();
                    setCurrentUserModelFromUI((max_cli_id + 1),max_shop_id);
                    Statement statement1 = con.createStatement();
                    String query_insert = "INSERT INTO CLIENT_TBL(CLI_SHOP,CLI_ID,TITLE,FIRST,LAST,ADDRESS1,ADDRESS2,SUBURB,STATE,PCODE,PHONEH,PHONEW,MOBILE,E_MAIL," +
                            "BIRTHDAY,GENDER,CLUB_PTS, NO_MAILINGS, REFERRED_BY,NO_SMS, NO_AUTO_SMS) VALUES("+m_current_user.getClishop()+",";
                    query_insert +=  m_current_user.getCliid()+", '";
                    query_insert +=  m_current_user.getTitle() + "','";
                    query_insert +=  m_current_user.getFirst_Name().toString()+"', '";
                    query_insert +=  m_current_user.getLast_Name().toString()+"', '";
                    query_insert +=  m_current_user.getAddress().toString()+"', '";
                    query_insert +=  m_current_user.getAddress_2().toString()+"', '";
                    query_insert +=  m_current_user.getSuburb().toString()+"', '";
                    query_insert +=  m_current_user.getState().toString()+"', '";
                    query_insert +=  m_current_user.getPost_Code().toString()+"', '";
                    query_insert +=  m_current_user.getPhone().toString()+"', '";
                    query_insert +=  m_current_user.getWork().toString()+"', '";
                    query_insert +=  m_current_user.getMobile().toString()+"', '";
                    query_insert +=  m_current_user.getEmail().toString()+"', ";
                    if(m_current_user.getBirthday().toString().isEmpty())
                        query_insert += "NULL,'";
                    else
                        query_insert += ("'" + m_current_user.getBirthday().toString()+"', '");
                    query_insert +=  m_current_user.getGender().toString()+"', 0,";
                    query_insert +=  m_current_user.getMail() +",'";
                    query_insert +=  m_current_user.getReferred_By().toString()+"', ";
                    query_insert +=  m_current_user.getSMS_Marketing()+", ";
                    query_insert +=  m_current_user.getSMS_Appointment_Reminders()+")";

                    statement1.executeUpdate(query_insert);
                    z = "Your details have been updated successfully ";
                    isSuccess = true;
                    statement1.close();
                    con.close();
                }
            }
            catch (Exception ex) {
                isSuccess = false;
                z = "Exceptions";
            }
            return z;
        }
    }
    private class SearchResultAdapter extends BaseAdapter implements SpinnerAdapter {
        Context _this;
        SearchResultAdapter(Context context){_this = context;}

        @Override
        public int getCount() {
            return m_users_search_result.size();
        }

        @Override
        public UserModel getItem(int position) {
            return m_users_search_result.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            TextView text = new TextView(_this);
            text.setText(m_users_search_result.get(position).getFirst_Name() +"    "+ m_users_search_result.get(position).getLast_Name());
            text.setTextSize(20);
            return text;
        }
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView v = new TextView(_this);
            v.setText(getItem(position).getFirst_Name() + "  " + getItem(position).getLast_Name());
            v.setTextSize(20);
            v.setHeight(70);
            return v;
        }


    }
}
