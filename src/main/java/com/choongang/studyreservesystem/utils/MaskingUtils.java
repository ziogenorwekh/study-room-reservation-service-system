package com.choongang.studyreservesystem.utils;

/*

    🚨주의! 회원가입 로직과 충돌하는 부분이 있는지 확인해봐야 함!

    개인정보 마스킹 처리를 위한 유틸리티 클래스
        - 이메일 마스킹 : user@domain.com => u***@domain.com
        - 이름 마스킹 : 홍길동 => 홍*동
        - 사용자 아이디(username) 마스킹 : hong123 => h*****3
        - 전화번호 마스킹 : 010-1234-5678 => 010-****-5678
 */
public class MaskingUtils {

    // 이메일 마스킹
    public static String maskEmail(String email) {
        //1. 입력값 유효성 검사 : null이거나 빈 문자열의 경우 처리
        if (email == null || email.isEmpty()) {
            return "N/A";
        }

        //2. @ 기호의 위치 찾기 (이메일 형식 확인)
        int atIndex = email.indexOf("@");

        //3. @ 기호가 없거나 맨 앞에 있는 경우 (잘못된 이메일 형식)
        if (atIndex < 1) {
            return email;       // "@test.com", "a@test.com"
        }

        //4. 이메일을 사용자명(@앞)과 도메인(@뒤) 부분으로 분리
        String username = email.substring(0, atIndex);        //@ 앞부분 추출
        String domain = email.substring(atIndex);             //@포함 뒤부분 추출

        //5. 사용자명 길이에 따른 마스킹 처리
        if (username.length() <= 2) {
            //첫글자 + "*" + 도메인
            return username.charAt(0) + "*" + domain;
        } else {
            //사용자명이 3글자 이상인 경우 : 첫글자 +***+ 도메인
            return username.charAt(0) + "***" + domain;
        }
    }

    // 이름 마스킹 (예: 홍길동 -> 홍*동)
    public static String maskName(String name) {
        if (name == null || name.isEmpty()) {
            return "N/A";
        }
        if (name.length() == 2) {
            return name.charAt(0) + "*";
        }
        if (name.length() > 2) {
            return name.charAt(0) + "*" + name.substring(2);
        }
        return name;
    }

    // username 마스킹 (회원가입 시 만든 고유 아이디)
    public static String maskUsername(String username) {
        if (username == null || username.isEmpty()) {
            return "N/A";
        }
        if (username.length() <= 2) {   //2개 이하면
            return username.charAt(0) + "*"; 
        }
        return username.charAt(0)   //2개 초과
                + "*".repeat(username.length() - 2)
                + username.charAt(username.length() - 1);
    }


    // 전화번호 마스킹 (예: 010-1234-5678 -> 010-****-5678)
    public static String maskPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return "N/A";
        }
        return phone.replaceAll("(\\d{3})-\\d{3,4}-(\\d{4})", "$1-****-$2");
    }
}