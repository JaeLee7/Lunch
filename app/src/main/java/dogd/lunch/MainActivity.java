package dogd.lunch;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;


public class MainActivity extends Activity {

    private NotesDbAdapter dbAdapter;
    private dayCalc dayCalc;
    private static final String TAG = "NotesDbAdapter";

    // 날짜를 이동할 때 쓰는 값
    long transToday;
    // 총 열 개수 값 설정 (56개 열이 존재)
    int rowEndIndex = 56;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "DB test :: onCreate()");
        this.dbAdapter = new NotesDbAdapter(this);
        dayCalc        = new dayCalc();


        // 오늘 날짜와 현재 시간 불러오기
        final long today    = dayCalc.whatDay();
        // final long nowClock = dayCalc.whatTime();

        copyExcelDataToDatabase();  // Excel -> DB로 자료 복사
        printDay(today);            // DB에 있는 자료를 오늘 날짜에 맞게 뿌려준다.

        // 이동하는 날짜 값
        transToday  = dayCalc.whatDay();

        // 전날 메뉴 보기
        ImageButton prevDay = (ImageButton) findViewById(R.id.bt_left);
        prevDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 저장 날짜로부터 1씩 감소
                long prevDate = dayCalc.prevDay(transToday);
                // 화면에 출력
                printDay(prevDate);
                // transToday 값에 1 감소
                transToday -= 1;
            }
        });

        // 다음날 메뉴 보기
        ImageButton nextDay = (ImageButton) findViewById(R.id.bt_right);
        nextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 저장 날짜로부터 1씩 증가
                long nextDate = dayCalc.nextDay(transToday);
                // 화면에 출력
                printDay(nextDate);
                // transToday 값에 1 증가
                transToday += 1;
            }
        });

        // DB초기화하여 뿌려주기
        ImageButton reset = (ImageButton) findViewById(R.id.bt_reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DB open() -> delete entire DB -> DB close()
                dbAdapter.open();
                dbAdapter.allDelNote();
                dbAdapter.close();

                // DB삭제 로그와 메시지 띄워주기
                Log.d("DB", "삭제 완료");
                Toast.makeText(getApplicationContext(), "DB 삭제", Toast.LENGTH_SHORT).show();

                // 엑셀 -> DB 저장 로그와 메시지 띄우기
                copyExcelDataToDatabase();
                Log.d("excel->DB", "정상 동작");
                Toast.makeText(getApplicationContext(), "Excel->DB 동작", Toast.LENGTH_SHORT).show();

                // DB에 저장되어 있는 자료 뿌리기
                printDay(today);
                transToday = today;
                Log.d("printDay, transToday", "초기화 및 현재 날짜로 덮어씌우기 완료");

                // 초기화 완료 메시지 로그 띄우기
                Log.d("DB", "초기화 성공");
                Toast.makeText(getApplicationContext(), "초기화 종료!", Toast.LENGTH_SHORT).show();
            }
        });

        // 오픈소스 라이선스 버튼
        Button oslc = (Button) findViewById(R.id.bt_openLicense);
        oslc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 데이터 담아서 팝업(액티비티) 호출
                Intent intent = new Intent(getApplicationContext(), OpenLicensePopup.class);
                startActivity(intent);
            }
        });

        Button support = (Button) findViewById(R.id.bt_supportEmail);
        support.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 건의사항 액티비티로 이동
                Intent intent = new Intent(getApplicationContext(), supportActivity.class);
                startActivity(intent);
            }
        });
    }

    // Excel -> DB로 저장
    private void copyExcelDataToDatabase() {
        Log.w("ExcelToDatabase", "copyExcelDataToDatabase()");

        Workbook workbook = null;
        Sheet sheet = null;

        try {
            InputStream is = getBaseContext().getResources().getAssets().open("notes.xls");
            workbook = workbook.getWorkbook(is);

            if(workbook != null) {
                // 엑셀의 첫번째 sheet 인식
                sheet = workbook.getSheet(0);


                // Row(열), Column(행)
                if(sheet != null) {
                    // int nMaxColumn = 4;
                    int nRowStartIndex = 0;
                    // 엑셀에있는 열 개수
                    int nRowEndIndex = rowEndIndex;
                    int nColumnStartIndex = 0;
                    // DB에 데이터가 있으면 추가로 저장하지 않게 하기위해 중복값을 찾기위해 변수 선언
                    boolean overlapFind = false;

                    dbAdapter.open();
                    Cursor result = dbAdapter.fetchAllNotes();

                    while(result.moveToNext()) {
                        String strTitle = result.getString(1);
                        for(int nRow = nRowStartIndex; nRow < nRowEndIndex; nRow++) {
                            String title = sheet.getCell(nRow, nColumnStartIndex).getContents();
                            if(strTitle.equals(title)) {
                                overlapFind = true;
                            }
                        }

                    }
                    if(overlapFind == true) {
                        Log.d("DB", "저장되어 있는 값입니다.");
                    }
                    else {
                        for(int nRow = nRowStartIndex; nRow < nRowEndIndex; nRow++) {
                            String title = sheet.getCell(nRow, nColumnStartIndex).getContents();
                            String body1 = sheet.getCell(nRow, nColumnStartIndex + 1).getContents();
                            String body2 = sheet.getCell(nRow, nColumnStartIndex + 2).getContents();
                            String body3 = sheet.getCell(nRow, nColumnStartIndex + 3).getContents();
                            dbAdapter.createNote(title, body1, body2, body3);
                        }
                    }

                    dbAdapter.close();
                } else {
                    System.out.println("Sheet is null!");
                }
            } else {
                System.out.println("workBook is null!!");
            }
    } catch (Exception e) {
            e.printStackTrace();
    } finally {
            if(workbook != null) {
                workbook.close();
            }
        }
    }

    // 날짜를 레이아웃에 출력, Ldate 값에 해당 날짜를 입력하면 그에 알맞은 값을 맞춰준다.
    private void printDay(long Ldate) {
        dbAdapter.open();
        Cursor result = dbAdapter.fetchAllNotes();

        // 레이아웃에 뿌려줄 문자열을 저장할 변수들
        String dateStr = "";
        String breakfastStr = "";
        String lunchStr = "";
        String dinnerStr = "";

        // TextView 불러옴
        TextView date = (TextView) findViewById(R.id.tv_date);
        TextView breakfast = (TextView) findViewById(R.id.tv_breakfast);
        TextView lunch = (TextView) findViewById(R.id.tv_lunch);
        TextView dinner = (TextView) findViewById(R.id.tv_dinner);

        // DB에 있는 자료값 검색
        while(result.moveToNext()) {
            // String 변수에 DB안에 들어있는 값 저장
            String title = result.getString(1);
            long dayCheck = Long.parseLong(title);
            String body1 = result.getString(2);
            String body2 = result.getString(3);
            String body3 = result.getString(4);

            // DB에 가져온 dayCheck 날짜와 Ldate(today or transDate and etc...)  값이 맞다면
            if(dayCheck == Ldate) {
                String[] titleStr = new String[3];
                titleStr[0] = title.substring(0,4);
                titleStr[1] = title.substring(4,6);
                titleStr[2] = title.substring(6);
                // 출력 값에 "yyyy년 MM월 dd일" 형식으로 저장
                dateStr += titleStr[0] + "년 " + titleStr[1] + "월 " + titleStr[2] + "일";
                breakfastStr += body1;   // 아침 메뉴 저장
                lunchStr += body2;       // 점심 메뉴 저장
                dinnerStr += body3;      // 저녁 메뉴 저장
            }
        }
        // TextView에 Scroll 생기게 만들기
        breakfast.setMovementMethod(new ScrollingMovementMethod());
        lunch.setMovementMethod(new ScrollingMovementMethod());
        dinner.setMovementMethod(new ScrollingMovementMethod());

        if(dateStr == "") { // 만약 DB에 불러오는 값이 공백이라면
            // 텍스트 색상을 변경해줌
            dinner.setTextColor(Color.DKGRAY);
            breakfast.setTextColor(Color.DKGRAY);
            lunch.setTextColor(Color.DKGRAY);

            // 각 값에 DB에 자료가 없다는 출력 해주기~
            date.setText("메뉴가 등록안되었소!");
            breakfast.setText("메뉴가 홈페이지 혹은 \n DB에 등록되지 않았습니다.");
            lunch.setText("홈페이지에 등록되었다면 \n 개발자에게 연락해주어유~");
            dinner.setText("빠른 시일 내에 메뉴 \n 올라오면 업데이트 하겠슈~");
            result.close();
            dbAdapter.close();
        }
        else {  // 만약 DB에 불러오는 값이 공백이 아니라면 정상적으로 날짜에 맞게 메뉴 출력
            // 각 값들 뿌려주기
            date.setText(dateStr);
            breakfast.setText(breakfastStr);
            lunch.setText(lunchStr);
            dinner.setText(dinnerStr);
            result.close();
            dbAdapter.close();
        }
    }
}