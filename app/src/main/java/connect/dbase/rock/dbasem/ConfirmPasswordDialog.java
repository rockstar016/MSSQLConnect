package connect.dbase.rock.dbasem;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

/**
 * Created by RockStar-0116 on 2016.07.17.
 */
public class ConfirmPasswordDialog extends Dialog implements View.OnClickListener{

    Button m_bt_yes, m_bt_no;
    Activity m_parent;
    int type;
    EditText m_txt_password;
    public ConfirmPasswordDialog(Activity context, int type) {
        super(context);
        m_parent = context;
        this.type = type;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirm_password);
        m_bt_yes = (Button)findViewById(R.id.bt_dialog_yes);
        m_bt_no = (Button)findViewById(R.id.bt_dialog_no);
        m_txt_password = (EditText)findViewById(R.id.txt_dialog_password);
        m_bt_yes.setOnClickListener(this);
        m_bt_no.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.bt_dialog_yes)
        {
            if(type == CommonDatas.SEARCH_PASSWORD){
                //search를 누른 경우, 여기에 넘어올때는 분명하게 파스워드가 채워져 있는
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(m_parent.getApplicationContext());
                String m_search_password = sp.getString("searchpassword","");
                if(m_txt_password.getText().toString().trim().equals(m_search_password))
                {
                    ((MainScreenActivity)m_parent).setResultDialogForSearch(CommonDatas.PASSWORD_RESULT_OK);
                }else{
                    ((MainScreenActivity)m_parent).setResultDialogForSearch(CommonDatas.PASSWORD_RESULT_NO);
                }
            }
            else{
                if(m_txt_password.getText().toString().trim().equals("hwbw")){
                    ((MainScreenActivity)m_parent).setResultDialogForSetting(CommonDatas.PASSWORD_RESULT_OK);
                }
                else
                {
                    ((MainScreenActivity)m_parent).setResultDialogForSetting(CommonDatas.PASSWORD_RESULT_NO);
                }
            }
            dismiss();
        }
        else{
            //no button
            dismiss();
        }
    }
}
