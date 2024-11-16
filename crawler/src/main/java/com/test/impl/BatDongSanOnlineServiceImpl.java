package com.test.impl;

import com.test.Constants;
import com.test.CrawlerService;
import com.test.BatDongSanOnlineService;
import com.test.business.CategoryManagementRemoteBean;
import com.test.business.ProductManagementRemoteBean;
import com.test.dto.BatDongSanDTO;
import com.test.dto.CategoryDTO;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class BatDongSanOnlineServiceImpl implements CrawlerService, BatDongSanOnlineService {

    private CategoryManagementRemoteBean categoryManagementBean;
    private ProductManagementRemoteBean productManagementBean;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public String crawlerUrl() {
        return "https://batdongsanonline.vn";
    }

    @Override
    public void doCrawler() throws Exception {
        List<CategoryDTO> categoryDTOs = categoryManagementBean.findAll(true);
        int addNew = 0;
        int exists = 0;
        int error = 0;
        for(CategoryDTO categoryDTO : categoryDTOs){
            for(int i = 1; i < crawlerDeep(); i++){
                String orgUrl = categoryDTO.getBatDongSanOnlineUrl();
                if(StringUtils.isNotBlank(orgUrl)){
                    String url = orgUrl + (i > 1 ? "page-" + i + "/" : "");
                    logger.info("--- Gathering: " + url);
                    List<BatDongSanDTO> items = gatherInfo(url);
                    logger.info("--- Processing list: " + items.size());
                    try {
                        Integer[] saveInfos = productManagementBean.saveOrUpdate(categoryDTO.getCategoryId(), items);
                        addNew += saveInfos[0];
                        exists += saveInfos[1];
                        error += saveInfos[2];
                        logger.info("--- Result ----");
                        logger.info("--->> [saved: " + saveInfos[0] + ", exists: " + saveInfos[1] + ", error: " + saveInfos[2] + "] " + url);
                    }catch (Exception e){
                        logger.info(e.getMessage());
                        continue;
                    }
                }
            }
            categoryManagementBean.updateCrawlerStatus(categoryDTO.getCategoryId(), Constants.CRAWLER_DONE);
        }
        productManagementBean.updateCrawlerHistory(crawlerUrl(), addNew, exists, error);
    }

    private List<BatDongSanDTO> gatherInfo(String url){
        List<BatDongSanDTO> items = new ArrayList<BatDongSanDTO>();
        try{
            System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
            Document doc = Jsoup.connect(url).ignoreHttpErrors(true).userAgent(Constants.userAgent).get();
            Elements searchProductItems = doc.getElementById("append_data").select("li");

            for (Element searchProductItem : searchProductItems) {
                BatDongSanDTO batDongSanDTO = getBrief(searchProductItem);
                if(searchProductItem.hasClass("loaitin_5")){
                    batDongSanDTO.setVip(Constants.VIP_0);
                }else if(searchProductItem.hasClass("loaitin_4")){
                    batDongSanDTO.setVip(Constants.VIP_1);
                }else if(searchProductItem.hasClass("loaitin_3")){
                    batDongSanDTO.setVip(Constants.VIP_2);
                }else if(searchProductItem.hasClass("loaitin_2")){
                    batDongSanDTO.setVip(Constants.VIP_3);
                } else {
                    batDongSanDTO.setVip(Constants.VIP_5);
                }
                batDongSanDTO.setHref(url);
                items.add(batDongSanDTO);
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.info(e.getMessage());
        }
        return items;
    }

    private BatDongSanDTO getBrief(Element searchProductItem){
        String pTitle = searchProductItem.select("div.titleTindang").select("a").attr("title");
        String href = searchProductItem.select("div.titleTindang").select("a").attr("href");
        String brief = searchProductItem.select("div.des_spec_bds").text();
        String pThumb = searchProductItem.select("div.imgChinh").select("img").attr("data-src");
        Elements imgs = searchProductItem.select("div.list_img").select("img");
        String pPrice = searchProductItem.select("div.w10").select("span").get(1).text();
        String pArea = searchProductItem.select("div.w10").select("span").get(0).text();
        String cityDist = searchProductItem.select("div.address").text();
        List<String> imgList = new ArrayList<>();
        if(imgs.size() > 0){
            for(Element img : imgs){
                imgList.add(img.attr("data-src"));
            }
        }

        BatDongSanDTO batDongSanDTO = new BatDongSanDTO();
        batDongSanDTO.setTitle(pTitle);
        batDongSanDTO.setHref(href);
        batDongSanDTO.setThumb(StringUtils.isNotBlank(pThumb) ? pThumb : imgList.get(0));
        batDongSanDTO.setImages(imgList);
        batDongSanDTO.setPriceString(pPrice);

        Object[] objects = getPriceFromString(pPrice);
        batDongSanDTO.setPrice(objects != null ? (Float)objects[0] : -1);
        batDongSanDTO.setUnitString(objects != null ? (String)objects[1]: "");
        batDongSanDTO.setArea(getAreaFromString(pArea));
        batDongSanDTO.setCityDist(cityDist);
        batDongSanDTO.setBrief(brief);
        batDongSanDTO.setSource(crawlerUrl());
        try{
            updateDetail(href, batDongSanDTO);
        }catch (Exception e){
            logger.info(e.getMessage());
        }
        return batDongSanDTO;
    }

    private void updateDetail(String fullUrl, BatDongSanDTO dto) throws Exception{
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
        Document doc = Jsoup.connect(fullUrl).userAgent(Constants.userAgent).get();
        String detail = doc.getElementsByClass("product-detail").select("div.f-detail").html();

        Elements divDacDiemBatDongSan = doc.getElementsByClass("listTienich").select("li");
        //logger.info("------- DAC DIEM ----------");
        for(Element dacDiem : divDacDiemBatDongSan){
            String key = dacDiem.select("span").text().trim();
            String value = dacDiem.select("div.text-right").text();
            //logger.info("[" + key + ": " + value + "]");

            switch (key){
                case "Địa chỉ": dto.setAddress(value);break;
                //case "Mã tin đăng": dto.setCode(value);break;
                // case "Loại tin rao": dto.setGroup(value);break;
                case "Ngày đăng": dto.setPostDateStr(value.replace("/", "-"));break;
                case "Ngày hết hạn": dto.setExpireDateStr(value);break;
                case "Số phòng ngủ": dto.setRoom(value);break;
                case "Số phòng vệ sinh": dto.setToilet(value);break;
                case "Hướng cửa chính": dto.setDirectionString(value);break;
                case "Tổng số tầng": dto.setFloor(value);break;
                case "Chiều ngang": dto.setWidthSize(value);break;
                case "Chiều dài": dto.setLongSize(value);break;
                default:
            }
        }

        Elements divMaTinNgayDangs = doc.select("div.m-0.row.text-left").select("div.col-md-4");
        for(Element divMaTinNgayDang : divMaTinNgayDangs){
            String[] temps = divMaTinNgayDang.text().split(":");
            if(temps.length == 2){
                String key = temps[0].trim();
                String val = temps[1].trim();
                if(key.equals("Mã tin")){
                    dto.setCode(val);
                } else if(key.equals("Ngày đăng")){
                    dto.setPostDateStr(val.replace("/", "-"));
                }
            }
        }

        //logger.info("------- LIEN HE ----------");
        try {
            String contactName = doc.getElementsByClass("main_sb").get(0).select("div.our_list").select("div.avt").select("span.name").text();
            String contactPhone = doc.getElementsByClass("main_sb").select("div.our_list").select("div.contact-info").select("a").get(0).attr("data-phone");
            dto.setContactName(contactName);
            dto.setContactPhone(contactPhone);
        }catch (Exception ex){
            logger.info("Can't get contact info: " + ex.getMessage());
        }


        Elements imageElements = doc.getElementById("galleryGrid").select("div.imgs-grid-image").select("div.image-wrap").select("img");
        List<String> images = new ArrayList<String>();
        for(Element imageElement: imageElements){
            dto.getImages().add(imageElement.attr("src"));
        }

        String strAdd = doc.getElementsByClass("info_Ds").select("span.Viethoa1").text();
        String[] strAddrs = strAdd.split(",");
        if(strAddrs.length > 0) {
            String street = strAddrs[0].trim();
            dto.setStreetString(street);
            dto.setWardString(strAddrs[1].trim());
        }
        dto.setDetail(detail);
    }

    private Object[] getPriceFromString(String priceStr){
        if(StringUtils.isNotBlank(priceStr)){
            String[] strs = priceStr.split(":");
            if(strs.length > 1){
                String valStr = strs[1].trim();
                String[] temp = valStr.split(" ");
                if(temp.length == 2) {
                    String val = temp[0].trim();
                    String unitString = temp[1].trim();
                    if (NumberUtils.isNumber(val)) {
                        float price = Float.valueOf(val);
                        return new Object[]{price, unitString};
                    }
                }
            }
        }
        return null;
    }

    private String getAreaFromString(String areaStr){
        String result = areaStr;
        if(StringUtils.isNotBlank(areaStr)){
            String[] strs = areaStr.split(":");
            if(strs.length == 2){
                String[] temp = strs[1].trim().split(" ");
                if(NumberUtils.isNumber(temp[0].trim())){
                    return temp[0].trim();
                }
            }
        }
        return result;
    }

    @Override
    public Integer crawlerDeep() {
        return 5;
    }

    @Override
    public void crawlerNews() {

    }

    @Override
    public void crawlerSampleHouse() {

    }

    public void setCategoryManagementBean(CategoryManagementRemoteBean categoryManagementBean) {
        this.categoryManagementBean = categoryManagementBean;
    }

    public void setProductManagementBean(ProductManagementRemoteBean productManagementBean) {
        this.productManagementBean = productManagementBean;
    }
}
