import com.test.CafeLandService;
import com.test.impl.*;
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
        CafeLandServiceImpl cafeLandService = (CafeLandServiceImpl)context.getBean("cafeLandService");
        BatDongSanOnlineServiceImpl batDongSanOnlineService =(BatDongSanOnlineServiceImpl) context.getBean("batDongSanOnlineService");
        try{
            // Crawler category first, if didn't before
           // batDongSanService.updateMainCategory();
           // batDongSanService.crawlerBrand();

            // Crawler batdongsan.com.vn
            // batDongSanService.doCrawler();
            // batDongSanService.crawlerNews();
             //batDongSanService.crawlerSampleHouse();

            // Crawler muabannhadat.vn --> Obsoleted
             //muaBanNhaDatService.doCrawler();
             //muaBanNhaDatService.crawlerNews();
             //muaBanNhaDatService.crawlerSampleHouse();

            // Crawler dothi.net // -> OK
            // dothiService.doCrawler();//.crawlerSampleHouse();//.crawlerNews();//.doCrawler();
            //dothiService.crawlerNews();
            //dothiService.crawlerSampleHouse();

            // Crawler https://nhadat.cafeland.vn/  // -> OK
            //cafeLandService.doCrawler();
            //cafeLandService.crawlerNews();

            //vnexpressService.crawlerSampleHouse();

            // Crawler https://batdongsanonline.vn
            // batDongSanOnlineService.doCrawler();

            //MyThread th1 = new MyThread(batDongSanService);
            //th1.start();
            //MyThread th2 = new MyThread(muaBanNhaDatService);
            //th2.start();

            /*--OK--*/
            MyThread th3 = new MyThread(dothiService);
            th3.start();
            MyThread th4 = new MyThread(cafeLandService);
            th4.start();/*
            MyThread th5 = new MyThread(batDongSanOnlineService);
            th5.start();*/

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
