package dogd.lunch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Admin on 2017-06-07.
 */

public class supportActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);


        // 이메일 보내기 버튼
        Button supportSendEmail = (Button) findViewById(R.id.bt_support_sendMail);
        supportSendEmail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_SEND);

                // 제목/내용의 EditText 값 저장
                EditText title = (EditText) findViewById(R.id.et_support_title);
                EditText sub = (EditText) findViewById(R.id.et_support_sub);

                // EditText의 내용을 String 으로 변환
                String titleStr = title.getText().toString();
                String subStr   = sub.getText().toString();

                // 메일주소
                String[] mailaddr = {"syama70@naver.com"};

                sendIntent.setType("plaine/text");
                sendIntent.putExtra(Intent.EXTRA_EMAIL, mailaddr); // 받는사람
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, titleStr); // 제목
                sendIntent.putExtra(Intent.EXTRA_TEXT, "\n\n" + subStr); // 첨부내용

                startActivity(sendIntent);

            }
        });

        // 건의사항 액티비티 종료 버튼
        Button supportClose = (Button) findViewById(R.id.bt_support_close);
        supportClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

    }

}
