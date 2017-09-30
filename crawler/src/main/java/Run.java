import com.test.impl.BatDongSanServiceImpl;
import com.test.impl.DothiServiceImpl;
import com.test.impl.MuaBanNhaDatServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 7/10/15
 * Time: 11:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class Run {
    public static void main(String[] args){
        ApplicationContext context = new ClassPathXmlApplicationContext("services.xml");
        BatDongSanServiceImpl batDongSanService = (BatDongSanServiceImpl)context.getBean("batDongSanService");
        MuaBanNhaDatServiceImpl muaBanNhaDatService = (MuaBanNhaDatServiceImpl)context.getBean("muaBanNhaDatService");
        DothiServiceImpl dothiService = (DothiServiceImpl)context.getBean("dothiService");
        try{
            // Crawler category first, if didn't before
            // batDongSanService.updateMainCategory();
            // batDongSanService.crawlerBranch();

            // Crawler batdongsan.com.vn
            // batDongSanService.doCrawler();
             //batDongSanService.crawlerNews();
             //batDongSanService.crawlerSampleHouse();

            // Crawler muabannhadat.vn
             // muaBanNhaDatService.doCrawler();
             //muaBanNhaDatService.crawlerNews();
             //muaBanNhaDatService.crawlerSampleHouse();

            // Crawler dothi.net
            // dothiService.doCrawler();

            MyThread th1 = new MyThread(batDongSanService);
            th1.start();
            MyThread th2 = new MyThread(muaBanNhaDatService);
            th2.start();
            MyThread th3 = new MyThread(dothiService);
            th3.start();


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
