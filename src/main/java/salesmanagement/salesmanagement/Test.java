package salesmanagement.salesmanagement;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<String> phoneCodes = new ArrayList<>();
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

        for (String regionCode : phoneUtil.getSupportedRegions()) {
            Phonenumber.PhoneNumber exampleNumber = phoneUtil.getExampleNumber(regionCode);
            Collections.sort(phoneCodes);
            System.out.println(phoneUtil.getRegionCodeForCountryCode(84));
        }
    }
}
