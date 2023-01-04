package kr.co.kyurworld.kyur.member.model;

import lombok.Data;

@Data
public class AuthNumInfo {
    private String user_id;
    private Integer auth_num;
    private String user_name;
    private String user_email;
}
