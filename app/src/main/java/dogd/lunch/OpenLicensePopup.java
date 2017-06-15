package dogd.lunch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Admin on 2017-05-31.
 */

public class OpenLicensePopup extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_activity);

        // UI 객체 생성
        TextView txtText = (TextView) findViewById(R.id.txtText);

        // 데이터 가져오기
        String data = openLicenseList();
        txtText.setText(data);

        Linkify.TransformFilter mTransform = new Linkify.TransformFilter() {
            @Override
            // return 값에 "" 공백을 입력.. 아니면 주소 뒤에 붙어서 이상한 링크로 이동하게 됨.
            public String transformUrl(Matcher match, String url) {
                return "";
            }
        };

        // 각 링크를 연결하는 패턴 적용
        Pattern pattern1 = Pattern.compile("jxl");

        // 해당 텍스트에 아래와 같이 링크를 연결
        Linkify.addLinks(txtText, pattern1, "https://sourceforge.net/projects/jexcelapi/files/jexcelapi/", null, mTransform);

    }

    // 확인 버튼 클릭
    public void mOnClose(View v) {
        // 데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close popup");
        setResult(RESULT_OK, intent);

        // 액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 바깥 레이어 클릭시 안닫히게
        if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        // 안드로이드 백버튼 막기
        return ;
    }

    // 오픈 라이선스 List
    private String openLicenseList() {
        String olscStr = "";
        olscStr += "jxl(2.6.12) \n";
        olscStr += "";
        return olscStr;
    }
}