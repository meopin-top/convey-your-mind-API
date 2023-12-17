package muffintop.cym.api.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

class ProjectServiceTest {

    @Test
    void test1() {
        String regex = "^(?:(?:https?://)?34\\.64\\.92\\.123/rolling-paper(?:/edit/|/view/))?(\\w+)$";

        String input = "http://34.64.92.123/rolling-paper/edit/invitecode";
        // Create a Pattern object
        Pattern pattern = Pattern.compile(regex);

        // Create a Matcher object
        Matcher matcher = pattern.matcher(input);

        // Find and print custom URLs
        if (matcher.find()) {
            System.out.println(1);
            System.out.println(matcher.group(1));
            System.out.println("Found Custom URL: " + matcher.group());
        }
    }
}