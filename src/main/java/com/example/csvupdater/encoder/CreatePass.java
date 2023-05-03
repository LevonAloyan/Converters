package com.example.csvupdater.encoder;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CreatePass {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        c2QvRE>U
        System.out.println(encoder.encode("J5klMnq7LX3!"));
//        ruud.koorevaar@legalintelligence.com
//        ewout.pieters@wolterskluwer.com
    }
}
