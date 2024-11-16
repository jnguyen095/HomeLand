import com.test.CrawlerService;

import java.util.logging.Logger;

/**
 * Created by Khang Nguyen.
 * Email: khang.nguyen@banvien.com
 * Date: 9/22/2017
 * Time: 4:24 PM
 */
public class MyThread implements Runnable, CrawlerService {
    private CrawlerService instance;
    private Thread t;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public MyThread(CrawlerService service){
        this.instance = service;
    }

    public void start () {
        System.out.println("Starting " +  this.crawlerUrl() );
        if (t == null) {
            t = new Thread (this, this.crawlerUrl());
            t.start ();
        }
    }

    @Override
    public void run() {
        logger.info("---- Running: " + this.crawlerUrl());
        try {
            this.doCrawler();
            this.crawlerNews();
            this.crawlerSampleHouse();
        }catch (Exception ex){
            logger.info(ex.getMessage());
            ex.getStackTrace();
        }
    }

    @Override
    public String crawlerUrl() {
        return this.instance.crawlerUrl();
    }

    @Override
    public void doCrawler() throws Exception {
        this.instance.doCrawler();
    }

    @Override
    public Integer crawlerDeep() {
        return this.instance.crawlerDeep();
    }

    @Override
    public void crawlerNews() {
        this.instance.crawlerNews();
    }

    @Override
    public void crawlerSampleHouse() {
        this.instance.crawlerSampleHouse();
    }
}
