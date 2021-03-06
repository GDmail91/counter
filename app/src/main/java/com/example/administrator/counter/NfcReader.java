package com.example.administrator.counter;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by YS on 2015-12-11.
 */
public class NfcReader extends Activity {
    TextView mTextView;
    Button tempButton;
    EditText tempTextedit;

    NfcAdapter mNfcAdapter; // NFC 어댑터
    PendingIntent mPendingIntent; // 수신받은 데이터가 저장된 인텐트
    IntentFilter[] mIntentFilters; // 인텐트 필터
    String[][] mNFCTechLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfc_reader);

        // TODO 임시로 다음스탭으로 넘어가는 버튼
        tempButton = (Button)findViewById(R.id.temp_button);
        tempTextedit = (EditText)findViewById(R.id.temp_textedit);


        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HttpHandler().regBtn("{\"mac_addr\":\"" + tempTextedit.getText().toString() + "\"}", new MyCallback() {
                    @Override
                    public void httpProcessing(JSONObject result) {
                        // TODO if result의 status가 true 일경우만 다음 실행
                        Toast toast = Toast.makeText(getApplicationContext(), "버튼등록 완료", Toast.LENGTH_LONG);
                        toast.show();

                        Intent intent = new Intent(NfcReader.this, ButtonRegPage.class);
                        intent.putExtra("mac_addr", tempTextedit.getText().toString());
                        startActivity(intent);
                    }
                });
            }
        });

        mTextView = (TextView)findViewById(R.id.textMessage);
        // NFC 어댑터를 구한다
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        // NFC 어댑터가 null 이라면 칩이 존재하지 않는 것으로 간주
        if( mNfcAdapter == null ) {
            mTextView.setText("This phone is not NFC enable.");
            return;
        }

        mTextView.setText("Scan a NFC tag");

        // NFC 데이터 활성화에 필요한 인텐트를 생성
        Intent intent = new Intent(this, getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mPendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        // NFC 데이터 활성화에 필요한 인텐트 필터를 생성
        IntentFilter iFilter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            iFilter.addDataType("*/*");
            mIntentFilters = new IntentFilter[] { iFilter };
        } catch (Exception e) {
            mTextView.setText("Make IntentFilter error");
        }
        mNFCTechLists = new String[][] { new String[] { NfcF.class.getName() } };
    }

    public void tempFunc(View v) {
        try {
            Thread.sleep(3000);

            Intent intent = new Intent(NfcReader.this, ButtonRegPage.class);
            intent.putExtra("mac_addr", "TESTMACADDR010");
            startActivity(intent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void onResume() {
        super.onResume();
        // 앱이 실행될때 NFC 어댑터를 활성화 한다
        if( mNfcAdapter != null )
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, mNFCTechLists);

        // NFC 태그 스캔으로 앱이 자동 실행되었을때
        if( NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction()) )
            // 인텐트에 포함된 정보를 분석해서 화면에 표시
            onNewIntent(getIntent());
    }

    public void onPause() {
        super.onPause();
        // 앱이 종료될때 NFC 어댑터를 비활성화 한다
        if( mNfcAdapter != null )
            mNfcAdapter.disableForegroundDispatch(this);
    }

    public void onClick(View v) {
        switch( v.getId() ) {
            case R.id.buttonSetup : {
                if (mNfcAdapter == null) return;
                // NFC 환경설정 화면 호출
                Intent intent = new Intent( Settings.ACTION_NFC_SETTINGS );
                startActivity(intent);
                break;
            }
        }
    }

    // NFC 태그 정보 수신 함수. 인텐트에 포함된 정보를 분석해서 화면에 표시
    @Override
    public void onNewIntent(Intent intent) {
        // 인텐트에서 액션을 추출
        String action = intent.getAction();
        // 인텐트에서 태그 정보 추출
        String tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG).toString();
        String strMsg = action + "\n\n" + tag;
        // 액션 정보와 태그 정보를 화면에 출력
//        mTextView.setText(strMsg);

        Log.d("NFC", "NFC msg: " + action +", "+tag);
        // 인텐트에서 NDEF 메시지 배열을 구한다
        Parcelable[] messages = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        if(messages == null) return;

        for(int i=0; i < messages.length; i++)
            // NDEF 메시지를 화면에 출력
            showMsg((NdefMessage)messages[i]);
    }

    // NDEF 메시지를 화면에 출력
    public void showMsg(NdefMessage mMessage) {
        String strMsg = "", strRec="";
        // NDEF 메시지에서 NDEF 레코드 배열을 구한다
        NdefRecord[] recs = mMessage.getRecords();
        for (int i = 0; i < recs.length; i++) {
            // 개별 레코드 데이터를 구한다
            NdefRecord record = recs[i];
            byte[] payload = record.getPayload();
            // 레코드 데이터 종류가 텍스트 일때
            if( Arrays.equals(record.getType(), NdefRecord.RTD_TEXT) ) {
                // 버퍼 데이터를 인코딩 변환
                /**************여기서 데이터 받음 **/
                strRec = byteDecoding(payload);
            }
            // 레코드 데이터 종류가 URI 일때
            else if( Arrays.equals(record.getType(), NdefRecord.RTD_URI) ) {
                strRec = new String(payload, 0, payload.length);
                strRec = "URI: " + strRec;
            }
            strMsg += ("\n\nNdefRecord[" + i + "]:\n" + strRec);
        }

//        mTextView.append(strMsg);
        Log.d("NFC", "NFC msg: " + strRec);
        final String mac_addr = strRec;
        new HttpHandler().regBtn("{\"mac_addr\":\"" + mac_addr + "\"}", new MyCallback() {
            @Override
            public void httpProcessing(JSONObject result) {
                // TODO if result의 status가 true 일경우만 다음 실행
                try {
                    if (result.getBoolean("status")) {
                        Toast toast = Toast.makeText(getApplicationContext(), "버튼등록 완료", Toast.LENGTH_LONG);
                        toast.show();

                        Intent intent = new Intent(NfcReader.this, ButtonRegPage.class);
                        intent.putExtra("mac_addr", mac_addr);
                        startActivity(intent);
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "버튼등록 실패", Toast.LENGTH_LONG);
                        toast.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // 버퍼 데이터를 디코딩해서 String 으로 변환
    public String byteDecoding(byte[] buf) {
        String strText="";
        String textEncoding;
        if ((buf[0] & 0200) == 0) textEncoding = "UTF-8";
        else textEncoding = "UTF-16";
        int langCodeLen = buf[0] & 0077;

        try {
            strText = new String(buf, langCodeLen + 1,
                    buf.length - langCodeLen - 1, textEncoding);
        } catch(Exception e) {
            Log.d("tag1", e.toString());
        }
        return strText;
    }
}
