package com.etxtechstack.api.easypos_application.utils;

import com.etxtechstack.api.easypos_application.models.Permission;
import com.etxtechstack.api.easypos_application.vo.GeneralResponse;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.persistence.GeneratedValue;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class CommonHelper {

    private static final Random alphaIdRandom = new Random();

    private static final Logger logger = LoggerFactory.getLogger(CommonHelper.class);
    static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static Date formatDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        Date newDate = null;
        try {
            newDate = formatter.parse(date);
        } catch (Exception ex) {
            logger.info(ex.getMessage());
        }
        return newDate;
    }

    public static String GenerateBCryptEncoder(String rawPassword) {
        return encoder.encode(rawPassword);
    }


    public static String generateIdentifier(String firstString, String secondString){
        if(firstString.length()<3 ){
            firstString = firstString + "00";
        }
        else if(secondString.length() < 3){
            secondString = secondString + "00";
        }
        return firstString.substring(0, 3) + "_" + secondString.substring(0, 3);

    }

    public static String GeneratePassword(String p_str, int p_target_length) {
        int l_length;
        StringBuffer l_buf = null;
        byte l_byte[] = new byte[1];

        if (p_str == null) {
            l_buf = new StringBuffer();
            l_length = 0;
        } else if ((l_length = p_str.length()) == 0) {
            l_buf = new StringBuffer();
            l_length = 0;
        } else if (l_length < p_target_length) {
            l_buf = new StringBuffer(p_str);
        } else {
            return p_str;
        }

        for (; l_length < p_target_length; l_length++) {
            alphaIdRandom.nextBytes(l_byte);
            l_buf.append((char) ((Math.abs(l_byte[0]) % 26) + 'A'));
        }

        return l_buf.toString();
    }

    public static String GenerateRef(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMddss");
        LocalDateTime now = LocalDateTime.now();
        String time = dtf.format(now);
        int rand = new Random().nextInt(14-1)+14;
        String result = String.format("%012d", Integer.parseInt(time+""+""+rand));
        return result;
    }

    public static String GenerateIdentifierField(String idStr, String name) {
        return idStr + "_" + name;
    }

    public static Date addDaysTodate(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return  cal.getTime();
        //mm
    }

    public static boolean MatchBCryptPassword(String hashedPassword, String rawPassword) {
        return encoder.matches(rawPassword, hashedPassword);
    }

    public static String GenerateOTP(int length) {
        String numbers = "1234567890";
        Random random = new Random();
        char[] otp = new char[length];

        for(int i = 0; i< length ; i++) {
            otp[i] = numbers.charAt(random.nextInt(numbers.length()));
        }
        return String.valueOf(otp);
    }

    public static String EncodeToBase64(String str) {
        return String.valueOf(Base64.getEncoder().encode(str.getBytes()));
    }

    public static GeneralResponse GetUnAuthenticatedResponse() {
        GeneralResponse response = new GeneralResponse();
        response.setCode(CommonResponse.TOKEN_INVALID_CODE);
        response.setMessage(CommonResponse.TOKEN_INVALID_MSG);
        return response;
    }

    public static GeneralResponse GetUnAuthorizeResponse() {
        GeneralResponse response = new GeneralResponse();
        response.setCode(CommonResponse.INSUFFICIENT_PERMISSION_CODE);
        response.setMessage(CommonResponse.INSUFFICIENT_PERMISSION_MSG);
        return response;
    }

    public static String FormatDate(Date dateStr) {
        Date date = dateStr;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Instant instant = date.toInstant();
        LocalDate localDate = instant.atZone(defaultZoneId).toLocalDate();
        LocalDateTime localDateTime = instant.atZone(defaultZoneId).toLocalDateTime();
        return localDate.format(dateTimeFormatter);
    }

    public static String FormatDate(String dateStr) {
        Date date = new Date(dateStr);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Instant instant = date.toInstant();
        LocalDate localDate = instant.atZone(defaultZoneId).toLocalDate();
        LocalDateTime localDateTime = instant.atZone(defaultZoneId).toLocalDateTime();
        return localDate.format(dateTimeFormatter);
    }

    public static String GenerateDateString(String dateStr) {
        Date date = new Date(dateStr);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Instant instant = date.toInstant();
        LocalDate localDate = instant.atZone(defaultZoneId).toLocalDate();
        LocalDateTime localDateTime = instant.atZone(defaultZoneId).toLocalDateTime();
        return localDate.format(dateTimeFormatter);
    }

    public static String GenerateTimestampFromDateAndTimeString(String dateStr, String timeStr) {
        Date date = new Date(dateStr+ " " + timeStr);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Instant instant = date.toInstant();
        LocalDate localDate = instant.atZone(defaultZoneId).toLocalDate();
        LocalDateTime localDateTime = instant.atZone(defaultZoneId).toLocalDateTime();
        return localDate.format(dateTimeFormatter);
    }

    public static List<String> GetDatesOfTheWeek() {
        Calendar c = Calendar.getInstance();
        List<String> output = new ArrayList<String>();
        switch (c.get(Calendar.DAY_OF_WEEK))
        {
            case Calendar.SUNDAY:
                c.add(Calendar.DATE, 1);
                break;

            case Calendar.MONDAY:
                //Don't need to do anything on a Monday
                //included only for completeness
                break;

            case Calendar.TUESDAY:
                c.add(Calendar.DATE,-1);
                break;

            case Calendar.WEDNESDAY:
                c.add(Calendar.DATE, -2);
                break;

            case Calendar.THURSDAY:
                c.add(Calendar.DATE,-3);
                break;

            case Calendar.FRIDAY:
                c.add(Calendar.DATE,-4);
                break;

            case Calendar.SATURDAY:
                c.add(Calendar.DATE,2);
                break;
        }
        //Add the Monday to the output
        output.add(c.getTime().toString());
        for (int x = 1; x <7; x++)
        {
            //Add the remaining days to the output
            c.add(Calendar.DATE,1);
            output.add(c.getTime().toString());
        }
        return output;
    }

    public static List<String> GetDatesOfWeekByDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        List<String> output = new ArrayList<String>();
        switch (c.get(Calendar.DAY_OF_WEEK))
        {
            case Calendar.SUNDAY:
                c.add(Calendar.DATE, 1);
                break;

            case Calendar.MONDAY:
                //Don't need to do anything on a Monday
                //included only for completeness
                break;

            case Calendar.TUESDAY:
                c.add(Calendar.DATE,-1);
                break;

            case Calendar.WEDNESDAY:
                c.add(Calendar.DATE, -2);
                break;

            case Calendar.THURSDAY:
                c.add(Calendar.DATE,-3);
                break;

            case Calendar.FRIDAY:
                c.add(Calendar.DATE,-4);
                break;

            case Calendar.SATURDAY:
                c.add(Calendar.DATE,2);
                break;
        }
        //Add the Monday to the output
        output.add(c.getTime().toString());
        for (int x = 1; x <7; x++)
        {
            //Add the remaining days to the output
            c.add(Calendar.DATE,1);
            output.add(c.getTime().toString());
        }
        return output;
    }

    public static boolean IsConstraintError(String msg) {
        boolean isC = false;
        if(msg.contains("ConstraintViolationExceptionsd")) {
            isC = true;
        }
        return isC;
    }

    public static String FormatToDecimal(Double val) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(val);
    }

    public static GeneralResponse SuccessResponse(Map myData) {
        GeneralResponse response = new GeneralResponse();
        response.setData(myData);
        response.setCode(CommonResponse.SUCCESS_CODE);
        response.setMessage(CommonResponse.SUCCESS_MSG);
        return response;
    }

    public static GeneralResponse SuccessResponse() {
        GeneralResponse response = new GeneralResponse();
        Map myData = new HashMap();
        response.setCode(CommonResponse.SUCCESS_CODE);
        response.setMessage(CommonResponse.SUCCESS_MSG);
        return response;
    }

    public static Date GeneratedTokeExpiryDate(Integer mins) {
        Integer ONE_MINUTE_IN_MILLIS=60000;//millisecs

        Calendar date = Calendar.getInstance();
        long t= date.getTimeInMillis();
        Date afterAddingTenMins=new Date(t + (mins * ONE_MINUTE_IN_MILLIS));
        return afterAddingTenMins;
    }

    public static String FormatDateToDateTimeString(Date date) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S", Locale.UK);
//        LocalDateTime localDateTime = LocalDateTime.parse(date);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH-mm-ss");
        String reqDate = dateFormat.format(date).toString();
        return reqDate;
    }

    public static boolean HasPermission(String permissionStr, List<Permission> permissions) {
        List<String> permissionStrList = permissions.stream().map(permission-> {
            return permission.getName();
        }).collect(Collectors.toList());
        if(permissionStrList.contains(permissionStr)) {
            return true;
        }
        return false;
    }

    public static Integer formatMoney(Double amount) {
        Double amountDouble = amount * 100;
        Integer amountInt = amountDouble.intValue();
        return amountInt;
    }

    public static String FormatDateToDateString(Date date) {
        //        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S", Locale.UK);
        //        LocalDateTime localDateTime = LocalDateTime.parse(date);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String reqDate = dateFormat.format(date).toString();
        return reqDate;
    }

    public static String FormatDateToPrettyDateString(Date date) {
        //        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S", Locale.UK);
        //        LocalDateTime localDateTime = LocalDateTime.parse(date);
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String reqDate = dateFormat.format(date).toString();
        return reqDate;
    }
}
