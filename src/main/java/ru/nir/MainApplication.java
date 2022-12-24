package ru.nir;

import java.security.SecureRandom;
import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
@Slf4j
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class);


       /* String key = getTOTPCode("ALLBFD3BACIVXRGIXEPFVXZ3JIT67QQ4");
        Scanner scanner = new Scanner(System.in);
        String inputKeyFromConsole = scanner.next();
        if (key.equals(inputKeyFromConsole)) {
            System.out.println(true+" "+" key ="+key+", inputKey ="+inputKeyFromConsole);
        } else {
            System.out.println(false+" "+" key ="+key+", inputKey ="+inputKeyFromConsole);
        }*/
    }

   /* public static String getTOTPCode(String secretKey) {
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(secretKey);
        String hexKey = Hex.encodeHexString(bytes);
        return TOTP.getOTP(hexKey);
    }

    public static String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        return base32.encodeToString(bytes);
    }

    public static void synchronizationGenerateSecretKey() {
        String secretKey = "ALLBFD3BACIVXRGIXEPFVXZ3JIT67QQ4";
        String lastCode = null;
        while (true) {
            String code = getTOTPCode(secretKey);
            if (!code.equals(lastCode)) {
                System.out.println(code);
            }
            lastCode = code;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }*/
}
