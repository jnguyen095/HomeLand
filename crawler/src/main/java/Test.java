import com.test.Constants;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.util.List;

/**
 * Created by Khang Nguyen.
 * Email: khang.nguyen@banvien.com
 * Date: 9/1/2017
 * Time: 2:40 PM
 */
public class Test {
    public static void main(String[] args){
        try {
           // Document doc = Jsoup.connect("http://www.muabannhadat.vn/Services/Tracking/a5643298/GetPhoneCustom").post();
            String postUrl="http://www.muabannhadat.vn/Services/Tracking/a5643298/GetPhoneCustom";

            FirefoxProfile profile = new FirefoxProfile();
            profile.setPreference("general.useragent.override", "some UA string");
            WebDriver driver = new FirefoxDriver();
            driver.get(postUrl);
            List<WebElement> resultsDiv = driver.findElements(By.xpath("//table[contains (@class,'SpecTable')//td"));

            System.out.println(resultsDiv.size());
            for (int i=0; i<resultsDiv.size(); i++) {
                System.out.println(i+1 + ". " + resultsDiv.get(i).getText());
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
