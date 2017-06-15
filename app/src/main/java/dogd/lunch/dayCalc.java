package dogd.lunch;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Admin on 2017-05-31.
 */

public class dayCalc {


    public long prevDay(long date) {
        // date 값을 문자열로 변경.
        String dateStr = String.valueOf(date);

        // 문자열을 년, 월, 일 단위로 자르기
        String yearStr   = dateStr.substring(0, 4);
        String monthStr  = dateStr.substring(4, 6);
        String dayStr    = dateStr.substring(6);
        // 달력용 계산을 하기 위해서 int 형으로 변경해준다.
        int year         = Integer.parseInt(yearStr);
        int month        = Integer.parseInt(monthStr);
        int day          = Integer.parseInt(dayStr);

        // 날짜를 1 감소 시킴
        Calendar cal = Calendar.getInstance();
        cal.set(year, month-1, day);
        cal.add(Calendar.DATE, -1);
        java.util.Date tmp01 = cal.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        // 계산된 format 결과 값을 문자열로 변환
        String tmp02 =  formatter.format(tmp01);
        // 문자열로 변환된 값을 long 형으로 변환 후 리턴
        long sumDay = Long.parseLong(tmp02);
        return sumDay;
    }

    public long nextDay(long date) {
        // date 값을 문자열로 변경.
        String dateStr = String.valueOf(date);

        // 문자열을 년, 월, 일 단위로 자르기
        String yearStr   = dateStr.substring(0, 4);
        String monthStr  = dateStr.substring(4, 6);
        String dayStr    = dateStr.substring(6);
        // 달력용 계산을 하기 위해서 int 형으로 변경해준다.
        int year         = Integer.parseInt(yearStr);
        int month        = Integer.parseInt(monthStr);
        int day          = Integer.parseInt(dayStr);

        // 날짜를 1 증가 시킴
        Calendar cal = Calendar.getInstance();
        cal.set(year, month-1, day);
        cal.add(Calendar.DATE, +1);
        java.util.Date tempDate = cal.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        // 계산된 format 결과 값을 문자열로 변환
        String temp = format.format(tempDate);
        // 문자열로 변환된 값을 long 형으로 변환하여 리턴
        long sumDay = Long.parseLong(temp);
        return sumDay;
    }

    // 오늘 날짜 가져오기
    public long whatDay() {
        // 현재시간을 msec으로 구함
        long now = System.currentTimeMillis();
        // 현재 시간을 저장
        Date date = new Date(now);
        // 시간포맷 지정
        SimpleDateFormat curDateFormat = new SimpleDateFormat("yyyyMMdd");
        // 지정된 포맷으로 String 타입으로 리턴
        String todayStr = curDateFormat.format(date);
        long today = Long.parseLong(todayStr);

        return today;
    }

    // 현재 시간 가져오기
    public long whatTime() {
        // 현재시간을 msec 단위로 구함
        long now = System.currentTimeMillis();
        // 현재 시간을 저장
        Date date = new Date(now);
        // 시간 포맷을 지정
        SimpleDateFormat curHourFormat = new SimpleDateFormat("HH");
        // 지정된 포맷으로 타입 리턴
        String strTime = curHourFormat.format(date);
        long time = Long.parseLong(strTime);

        return time;
    }

}
