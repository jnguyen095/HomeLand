package com.test.impl;

import com.test.Constants;
import com.test.VnexpressService;
import com.test.business.SampleHouseManagementRemoteBean;
import com.test.dto.SampleHouseDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;

@Stateless(name = "VnexpressServiceImpl")
public class VnexpressServiceImpl implements VnexpressService {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private SampleHouseManagementRemoteBean sampleHouseManagementBean;

    @Override
    public void crawlerSampleHouse() {
        String url = "https://vnexpress.net/bat-dong-san/khong-gian-song";
        try {
            Document doc = Jsoup.connect(url).userAgent(Constants.userAgent).get();
            String detailUrl =  doc.getElementsByClass("article-topstory").select("div.thumb-art").select("a").attr("href");
            String thumb = doc.getElementsByClass("article-topstory").select("div.thumb-art").select("img").attr("src");
            SampleHouseDTO dto = getDetailPost(detailUrl, thumb);

            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm");
            List<SampleHouseDTO> items = new ArrayList<>();
            items.add(dto);
            try {
                Integer[] saveInfos = sampleHouseManagementBean.saveItems(items);
                logger.info("--- Result ----");
                logger.info("--->> [saved: " + saveInfos[0] + ", exists: " + saveInfos[1] + ", error: " + saveInfos[2] + "]");
            }catch (Exception e){
                logger.info(e.getMessage());
            }
        }catch (Exception ex){
            logger.info("------ error: " + url + " ------");
            logger.info(ex.getMessage());
        }
    }

    private SampleHouseDTO getDetailPost(String url, String thumb){
        SampleHouseDTO dto = new SampleHouseDTO();
        try {
            Document doc = Jsoup.connect(url).userAgent(Constants.userAgent).get();
            String title = doc.getElementsByClass("title-detail").text();
            String brief = doc.getElementsByClass("description").html();
            String description = doc.getElementsByClass("fck_detail").outerHtml();
            String dateTime = doc.getElementsByClass("date").text().split(",")[1].trim();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date createdDate = df.parse(dateTime);

            dto.setTitle(title);
            dto.setBrief(brief);
            dto.setDescription(description);
            dto.setThumb(thumb);
            dto.setView(0);
            dto.setStatus(Constants.ACTIVE);
            dto.setSource("VnExpress.net");
            dto.setCreatedDate(new Timestamp(createdDate.getTime()));
            dto.setUrl(url);
        }catch (Exception ex){
            logger.info("------ error: " + url + " ------");
            logger.info(ex.getMessage());
        }
        return dto;
    }


    public void setSampleHouseManagementBean(SampleHouseManagementRemoteBean sampleHouseManagementBean) {
        this.sampleHouseManagementBean = sampleHouseManagementBean;
    }
}
