package cn.weipan.fb.act;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;
import cn.weipan.fb.R;

/**
 * 新消息通知
 * Created by cc on 2016/7/22.
 * 邮箱：904359289@QQ.com.
 */
public class NewMessage extends BaseActivity implements View.OnClickListener {
    private Switch tbNews;
    private Switch tbVoice;
    private Switch tbMove;
    private SharedPreferences sp = null;
    private String[] voice;
    private SharedPreferences.Editor editor;
    private boolean ismove = true;
    private boolean isvoice = true;
    private boolean isreceivemessge = true;
    private String voicer;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
        initView();

    }

    //初始化控件
    private void initView() {
        LinearLayout fanhui = (LinearLayout) findViewById(R.id.ll_fanhui);
        fanhui.setOnClickListener(this);
        TextView header = (TextView) findViewById(R.id.head_view_title);
        header.setText("通知消息设置");

        tbNews = (Switch) findViewById(R.id.tb_news);
        tbNews.setOnClickListener(this);
        tbVoice = (Switch) findViewById(R.id.tb_voice);
        tbVoice.setOnClickListener(this);
        tbMove = (Switch) findViewById(R.id.tb_move);
        tbMove.setOnClickListener(this);
        textView = (TextView) findViewById(R.id.tv_text);
        RelativeLayout rl_voice = (RelativeLayout) findViewById(R.id.rl_voice);
        rl_voice.setOnClickListener(this);
        sp = getSharedPreferences("userInfo", 0);
        editor = sp.edit();

        voice = new String[]{"小燕", "小宇", "小研", "小琪", "小峰", "小梅（粤语）", "小莉（台湾）"};


        textView.setText(sp.getString("voice", "小燕"));
        isreceivemessge = sp.getBoolean("isreceivemessge", true);
        ismove = sp.getBoolean("ismove", true);
        isvoice = sp.getBoolean("isvoice", true);

        if (isreceivemessge) {
            tbNews.setChecked(true);
        } else {
            tbNews.setChecked(false);
        }
        if (ismove) {
            tbMove.setChecked(true);
        } else {
            tbVoice.setChecked(false);
        }
        if (isvoice) {
            tbVoice.setChecked(true);
        } else {
            tbVoice.setChecked(false);
        }
    }

    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.ll_fanhui:
                finish();
                break;
            //接收消息
            case R.id.tb_news:
                if (tbNews.isChecked()) {
                    editor.putBoolean("isreceivemessge", true);
                    Toast.makeText(NewMessage.this, "开", Toast.LENGTH_SHORT).show();
                    JPushInterface.resumePush(getApplicationContext());
                } else {
                    editor.putBoolean("isreceivemessge", false);
                    Toast.makeText(NewMessage.this, "关", Toast.LENGTH_SHORT).show();
                    JPushInterface.stopPush(getApplicationContext());
                }
                editor.commit();
                break;
            //声音
            case R.id.tb_voice:
                if (tbVoice.isChecked()) {
                    editor.putBoolean("isvoice", true);
                    Toast.makeText(NewMessage.this, "声音开", Toast.LENGTH_SHORT).show();
                } else {
                    editor.putBoolean("isvoice", false);
                    Toast.makeText(NewMessage.this, "声音关", Toast.LENGTH_SHORT).show();
                }
                editor.commit();
                break;
            //震动
            case R.id.tb_move:
                if (tbMove.isChecked()) {
                    editor.putBoolean("ismove", true);
                    Toast.makeText(NewMessage.this, "震动开", Toast.LENGTH_SHORT).show();
                } else {
                    editor.putBoolean("ismove", false);
                    Toast.makeText(NewMessage.this, "震动关", Toast.LENGTH_SHORT).show();
                }
                editor.commit();
                break;
            case R.id.rl_voice:
                Dialog alertDialog = new AlertDialog.Builder(this).
                        setTitle("请选择发音人")
                        .setItems(voice, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(NewMessage.this, voice[which], Toast.LENGTH_SHORT).show();
                                voicer = voice[which];
                                textView.setText(voice[which]);
                                editor.putString("voice", voice[which]);
                                editor.commit();
                                switch (voice[which]) {
                                    case "小燕":
                                        voicer = "xioayan";
                                        editor.putString("voicer", voicer);
                                        editor.commit();
                                        break;
                                    case "小宇":
                                        voicer = "xiaoyu";
                                        editor.putString("voicer", voicer);
                                        editor.commit();
                                        break;
                                    case "小研":
                                        voicer = "vixy";
                                        editor.putString("voicer", voicer);
                                        editor.commit();
                                        break;
                                    case "小琪":
                                        voicer = "vixq";
                                        editor.putString("voicer", voicer);
                                        editor.commit();
                                        break;
                                    case "小峰":
                                        voicer = "vixf";
                                        editor.putString("voicer", voicer);
                                        editor.commit();
                                        break;
                                    case "小梅（粤语）":
                                        voicer = "vixm";
                                        editor.putString("voicer", voicer);
                                        editor.commit();
                                        break;
                                    case "小莉（台湾）":
                                        voicer = "vixl";
                                        editor.putString("voicer", voicer);
                                        editor.commit();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        }).create();
                alertDialog.show();
                break;
            default:
                break;
        }

    }
}
