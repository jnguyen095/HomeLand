package com.test.impl;

import com.test.BatDongSanService;
import com.test.Constants;
import com.test.CrawlerService;
import com.test.business.*;
import com.test.dto.*;
import com.test.utils.EmailValidator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 7/10/15
 * Time: 11:16 PM
 * To change this template use File | Settings | File Templates.
 */
@Stateless(name = "BatDongSanServiceImpl")
public class BatDongSanServiceImpl implements CrawlerService, BatDongSanService {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private ProductManagementRemoteBean productManagementBean;
    private CategoryManagementRemoteBean categoryManagementBean;
    private NewsManagementRemoteBean newsManagementBean;
    private SampleHouseManagementRemoteBean sampleHouseManagementBean;
    private BranchManagementRemoteBean branchManagementRemoteBean;

    public void setBranchManagementRemoteBean(BranchManagementRemoteBean branchManagementRemoteBean) {
        this.branchManagementRemoteBean = branchManagementRemoteBean;
    }

    public void setSampleHouseManagementBean(SampleHouseManagementRemoteBean sampleHouseManagementBean) {
        this.sampleHouseManagementBean = sampleHouseManagementBean;
    }

    public void setNewsManagementBean(NewsManagementRemoteBean newsManagementBean) {
        this.newsManagementBean = newsManagementBean;
    }

    public void setCategoryManagementBean(CategoryManagementRemoteBean categoryManagementBean) {
        this.categoryManagementBean = categoryManagementBean;
    }

    public void setProductManagementBean(ProductManagementRemoteBean productManagementBean) {
        this.productManagementBean = productManagementBean;
    }

    @Override
    public String crawlerUrl(){
        return "https://batdongsan.com.vn";
    }

    @Override
    public Integer crawlerDeep() {
        return 5;
    }

    @Override
    public void doCrawler() throws Exception{
        List<CategoryDTO> categoryDTOs = categoryManagementBean.findAll(true);
        int addNew = 0;
        int exists = 0;
        int error = 0;
        for(CategoryDTO categoryDTO : categoryDTOs){
            for(int i = 1; i < crawlerDeep(); i++){
                String url = categoryDTO.getBatdongsanUrl() + (i > 1 ? "/p" + i : "");
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
            categoryManagementBean.updateCrawlerStatus(categoryDTO.getCategoryId(), Constants.CRAWLER_DONE);
        }
        productManagementBean.updateCrawlerHistory(crawlerUrl(), addNew, exists, error);
    }

    @Override
    public void updateMainCategory() {
        List<CategoryTreeDTO> trees = new ArrayList<CategoryTreeDTO>();
        try{
            Document doc = Jsoup.connect(crawlerUrl()).userAgent(Constants.userAgent).get();
            Elements elements = doc.getElementsByClass("dropdown-navigative-menu").select("li.lv0");

            if(elements != null && elements.size() > 0){
                for(Element element : elements){
                    Elements aElm = element.getElementsByTag("a").not("li.lv1 a");
                    if(aElm != null){
                        CategoryTreeDTO tree = new CategoryTreeDTO();
                        CategoryDTO root = new CategoryDTO();
                        String text = aElm.get(0).text();
                        String url = aElm.get(0).attr("href");
                        root.setBatdongsanUrl(crawlerUrl() + url);
                        root.setName(text);
                        tree.setRoot(root);

                        Elements liElm = element.getElementsByTag("ul").select("li.lv1");
                        for(Element li : liElm){
                            Elements a1Elm = li.getElementsByTag("a");
                            if(aElm != null){
                                CategoryDTO children = new CategoryDTO();
                                String ctext = a1Elm.get(0).text();
                                String curl = a1Elm.get(0).attr("href");
                                children.setName(ctext);
                                children.setBatdongsanUrl(crawlerUrl() + curl);
                                if(tree.getNodes() == null){
                                    tree.setNodes(new ArrayList<CategoryDTO>());
                                }
                                tree.getNodes().add(children);
                            }
                        }
                        trees.add(tree);
                    }
                }
            }
        }catch (Exception e){
            logger.info(e.getMessage());
        }
        if(trees != null){
            categoryManagementBean.updateMainCategory(trees);
        }
    }

    @Override
    public void crawlerBranch() {
        List<BrandDTO> branchs = branchManagementRemoteBean.findAll();
        for(BrandDTO dto : branchs){
            String url = "https://batdongsan.com.vn/phan-muc-cac-du-an-bds?k=" + dto.getBrandName().replace(" ", "+");
            try{
                Document doc = Jsoup.connect(url).userAgent(Constants.userAgent).get();
                Elements elements = doc.getElementsByClass("list-view").select("li");
                for(int i = 0; i < elements.size(); i++){
                    Element element = elements.get(i);
                    String href = element.select("div.thumb").select("a").attr("href");
                    String thumb = element.select("div.thumb").select("a").select("img").attr("src");
                    String address = element.select("div.detail").select("div.add").text();
                    String price = element.select("div.detail").select("span.price").text();
                    String investor = element.select("div.detail").select("div.investor").text().split(":")[1].trim();
                    String area = element.select("div.detail").select("div.area").text().split(":")[1].trim();
                    String progress = element.select("div.detail").select("div.prgrs").text().split(":")[1].trim();
                    if (!href.contains("http")) {
                        href = crawlerUrl() + href;
                    }
                    Document doc1 = Jsoup.connect(href).userAgent(Constants.userAgent).get();
                    String detail = doc1.getElementsByClass("prj-noidung").html();

                    dto.setThumb(thumb);
                    dto.setDescription(address);
                    dto.setArea(area);
                    dto.setPrice(price);
                    dto.setOwner(investor);
                    dto.setProcess(progress);
                    dto.setDetail(detail);

                    branchManagementRemoteBean.update(dto);
                }
            }catch (Exception e){
                logger.info("------ error: " + url + " ------");
                logger.info(e.getMessage());
            }
        }
    }

    @Override
    public void crawlerNews() {
        String newUrl = "https://batdongsan.com.vn/tin-thi-truong";
        //String newUrl = "https://batdongsan.com.vn/chinh-sach-quan-ly";
        try {
            SimpleDateFormat df = new SimpleDateFormat("hh:mm dd/MM/yyyy");
            for(int i = 0; i < crawlerDeep(); i++) {
                String url = newUrl + (i > 1 ? "/p" + i : "");
                logger.info("--- Gathering: " + url);
                List<NewsDTO> items = gatherNews(url, df);
                logger.info("--- Processing list: " + items.size());
                try {
                    Integer[] saveInfos = newsManagementBean.saveItems(items);
                    logger.info("--- Result ----");
                    logger.info("--->> [saved: " + saveInfos[0] + ", exists: " + saveInfos[1] + ", error: " + saveInfos[2] + "]");
                }catch (Exception e){
                    logger.info(e.getMessage());
                    continue;
                }
            }

        }catch (Exception e){
            logger.info(e.getMessage());
        }
    }

    @Override
    public void crawlerSampleHouse() {
        String newUrl = "https://batdongsan.com.vn/nha-dep";

        try {
            SimpleDateFormat df = new SimpleDateFormat("hh:mm dd/MM/yyyy");
            for(int i = 0; i < crawlerDeep(); i++) {
                String url = newUrl + (i > 1 ? "/p" + i : "");
                logger.info("--- Gathering: " + url);
                List<SampleHouseDTO> items = gatherSampleHouse(url, df);
                logger.info("--- Processing list: " + items.size());
                try {
                    Integer[] saveInfos = sampleHouseManagementBean.saveItems(items);
                    logger.info("--- Result ----");
                    logger.info("--->> [saved: " + saveInfos[0] + ", exists: " + saveInfos[1] + ", error: " + saveInfos[2] + "]");
                }catch (Exception e){
                    logger.info(e.getMessage());
                    continue;
                }
            }

        }catch (Exception e){
            logger.info(e.getMessage());
        }
    }

    private List<SampleHouseDTO> gatherSampleHouse(String url, SimpleDateFormat df){
        List<SampleHouseDTO> results = new ArrayList<>();
        try{
            Document doc = Jsoup.connect(url).userAgent(Constants.userAgent).get();
            Elements news = doc.getElementsByClass("tintuc-row1");

            for(Element newElement : news){
                String dateTime = newElement.getElementsByClass("datetime").text();
                String thumb = newElement.getElementsByTag("img").attr("src");
                String detailUrl = newElement.getElementsByTag("h3").get(0).getElementsByTag("a").attr("href");
                String title = newElement.getElementsByTag("h3").get(0).getElementsByTag("a").text();
                String brief = newElement.getElementsByTag("p").text();
                if (!detailUrl.contains("http")) {
                    detailUrl = crawlerUrl() + detailUrl;
                }
                Document detailDoc = Jsoup.connect(detailUrl).userAgent(Constants.userAgent).get();
                String detail = "<div class='summary'><h2>" + brief + "</h2></div>" + detailDoc.getElementById("divContents").html();
                String source = detailDoc.getElementsByClass("soucenews").html();
                Date createdDate = df.parse(dateTime);

                SampleHouseDTO dto = new SampleHouseDTO();
                dto.setTitle(title);
                dto.setBrief(brief);
                dto.setThumb(thumb);
                dto.setDescription(detail);
                dto.setView(0);
                dto.setStatus(Constants.ACTIVE);
                dto.setSource(StringUtils.isNotBlank(source) ? source : "https://batdongsan.com.vn");
                dto.setCreatedDate(new Timestamp(createdDate.getTime()));
                dto.setUrl(detailUrl);

                results.add(dto);
            }
        }catch (Exception e){
            logger.info(e.getMessage());
        }
        return results;
    }

    private List<NewsDTO> gatherNews(String url, SimpleDateFormat df){
        List<NewsDTO> results = new ArrayList<>();
        try{
            Document doc = Jsoup.connect(url).userAgent(Constants.userAgent).get();
            Elements news = doc.getElementsByClass("tintuc-row1");

            for(Element newElement : news){
                String dateTime = newElement.getElementsByClass("datetime").text();
                String thumb = newElement.getElementsByTag("img").attr("src");
                String detailUrl = newElement.getElementsByTag("h3").get(0).getElementsByTag("a").attr("href");
                String title = newElement.getElementsByTag("h3").get(0).getElementsByTag("a").text();
                String brief = newElement.getElementsByTag("p").text();
                if (!detailUrl.contains("http")) {
                    detailUrl = crawlerUrl() + detailUrl;
                }
                Document detailDoc = Jsoup.connect(detailUrl).userAgent(Constants.userAgent).get();
                String detail = "<div class='summary'><h2>" + brief + "</h2></div>" + detailDoc.getElementById("divContents").html();
                String source = detailDoc.getElementsByClass("soucenews").html();
                Date createdDate = df.parse(dateTime);

                NewsDTO dto = new NewsDTO();
                dto.setTitle(title);
                dto.setBrief(brief);
                dto.setThumb(thumb);
                dto.setDescription(detail);
                dto.setView(0);
                dto.setStatus(Constants.ACTIVE);
                dto.setSource(source);
                dto.setCreatedDate(new Timestamp(createdDate.getTime()));
                dto.setUrl(detailUrl);

                results.add(dto);
            }
        }catch (Exception e){
            logger.info(e.getMessage());
        }
        return results;
    }

    private List<BatDongSanDTO> gatherInfo(String url){
        List<BatDongSanDTO> items = new ArrayList<BatDongSanDTO>();
        try{
            Document doc = Jsoup.connect(url).userAgent(Constants.userAgent).get();
            Elements searchProductItems = doc.getElementsByClass("search-productItem");

            for (Element searchProductItem : searchProductItems) {
                BatDongSanDTO batDongSanDTO = getBrief(searchProductItem);
                if(searchProductItem.hasClass("vip0")){
                    batDongSanDTO.setVip(Constants.VIP_1);
                }else if(searchProductItem.hasClass("vip1")){
                    batDongSanDTO.setVip(Constants.VIP_2);
                }else if(searchProductItem.hasClass("vip2")){
                    batDongSanDTO.setVip(Constants.VIP_3);
                }else{
                    batDongSanDTO.setVip(Constants.VIP_5);
                }
                batDongSanDTO.setHref(url);
                items.add(batDongSanDTO);
            }
        }catch (Exception e){
            logger.info(e.getMessage());
        }
        return items;
    }

    private BatDongSanDTO getBrief(Element searchProductItem){
        String pTitle = searchProductItem.select("div.p-title").select("a").attr("title");
        String href = searchProductItem.select("div.p-title").select("a").attr("href");
        String brief = searchProductItem.select("div.p-main").select("div.p-content").select("div.p-main-text").text();
        String pThumb = searchProductItem.select("div.p-main").select("div.p-main-image-crop").select("a").select("img").attr("src");
        String pPrice = searchProductItem.select("div.p-main").select("div.p-bottom-crop").select("div.floatleft").select("span.product-price").text();
        String pArea = searchProductItem.select("div.p-main").select("div.p-bottom-crop").select("div.floatleft").select("span.product-area").text();
        String cityDist = searchProductItem.select("div.p-main").select("div.p-bottom-crop").select("div.floatleft").select("span.product-city-dist").text();

        BatDongSanDTO batDongSanDTO = new BatDongSanDTO();
        batDongSanDTO.setTitle(pTitle);
        batDongSanDTO.setHref(crawlerUrl() + href);
        batDongSanDTO.setThumb(pThumb);
        batDongSanDTO.setPriceString(pPrice);

        Object[] objects = getPriceFromString(pPrice);
        batDongSanDTO.setPrice(objects != null ? (Float)objects[0] : -1);
        batDongSanDTO.setUnitString(objects != null ? (String)objects[1]: "");
        batDongSanDTO.setArea(getAreaFromString(pArea));
        batDongSanDTO.setCityDist(cityDist);
        batDongSanDTO.setBrief(brief);
        batDongSanDTO.setSource(crawlerUrl());
        try{
            updateDetail(crawlerUrl() + href, batDongSanDTO);
        }catch (Exception e){
            logger.info(e.getMessage());
        }
        return batDongSanDTO;
    }

    private Object[] getPriceFromString(String priceStr){
        Float result = -1f;
        if(StringUtils.isNotBlank(priceStr)){
            String[] strs = priceStr.split(" ");
            if(strs.length > 1){
                String val = strs[0].trim();
                if(NumberUtils.isNumber(val)){
                    float price = Float.valueOf(val);
                    String unitString = strs[1];
                    return new Object[]{price, unitString};
                }
            }
        }
        return null;
    }

    private String getAreaFromString(String areaStr){
        String result = areaStr;
        if(StringUtils.isNotBlank(areaStr)){
            String[] strs = areaStr.split(" ");
            if(strs.length > 1){
                String val = strs[0].trim();
                if(NumberUtils.isNumber(val)){
                    return val;
                }
            }
        }
        return result;
    }

    private void updateDetail(String fullUrl, BatDongSanDTO dto) throws Exception{
        Document doc = Jsoup.connect(fullUrl).userAgent(Constants.userAgent).get();
        String detail = doc.getElementById("product-detail").select("div.pm-desc").html();

        Elements divDacDiemBatDongSan = doc.getElementById("product-detail").select("div.div-table-cell.table1").select("div.div-hold").select("div.row");
        //logger.info("------- DAC DIEM ----------");
        for(Element dacDiem : divDacDiemBatDongSan){
            String key = dacDiem.select("div.left").text().trim();
            String value = dacDiem.select("div.right").text();
            //logger.info("[" + key + ": " + value + "]");

            switch (key){
                case "Địa chỉ": dto.setAddress(value);break;
                //case "Mã tin đăng": dto.setCode(value);break;
                case "Loại tin rao": dto.setGroup(value);break;
                case "Ngày đăng": dto.setPostDateStr(value);break;
                case "Ngày hết hạn": dto.setExpireDateStr(value);break;
                case "Số phòng ngủ": dto.setRoom(value);break;
                case "Số toilet": dto.setToilet(value);break;
                case "Hướng nhà": dto.setDirectionString(value);break;
                case "Số tầng": dto.setFloor(value);break;
                case "Mặt tiền": dto.setWidthSize(value);break;
                default:
            }
        }

        //logger.info("------- LIEN HE ----------");
        Elements divLienHe = doc.getElementById("divCustomerInfo").select("div.right-content");
        for(Element lienHe : divLienHe){
            String key = lienHe.select("div.left").text().trim();
            String value = lienHe.select("div.right").text();
            //logger.info("[" + key + ": " + value + "]");
            switch (key){
                case "Tên liên lạc": dto.setContactName(value);break;
                case "Địa chỉ": dto.setContactAddress(value);break;
                case "Điện thoại": dto.setContactPhone(value);break;
                case "Mobile": dto.setContactMobile(value);break;
                case "Email": {
                    if(EmailValidator.validate(value)){
                        dto.setContactEmail(value);
                    }
                    break;
                }
                default:
            }
        }

        // ma tin dang, loai tin dang
        Elements productType =  doc.getElementById("product-detail").select(" > div.prd-more-info").select(" > div");
        for(Element prType : productType){
            String key = prType.select("span.normalblue").text().trim();
            String value = prType.text().split(":")[1].trim();
            //logger.info("[" + key + ": " + value + "]");
            switch (key){
                case "Mã tin đăng:": dto.setCode(value);break;
                case "Ngày đăng:": dto.setPostDateStr(value);break;
                case "Ngày hết hạn:": dto.setExpireDateStr(value);break;
                default:
            }
        }

        Elements imageElements = doc.getElementById("product-detail").select("div.list-img").select("ul").select("li").select("img");
        List<String> images = new ArrayList<String>();
        for(Element imageElement: imageElements){
            images.add(imageElement.attr("src"));
        }

        String latitude = doc.getElementById("hdLat").attr("value");
        String longitude = doc.getElementById("hdLong").attr("value");
        String ward = doc.getElementById("divWardOptions").select("li.current").text();
        String street = doc.getElementById("divStreetOptions").select("li.current").text();
        String brand = doc.getElementById("divProjectOptions").select("li.current").text();
        String room = doc.getElementById("divBedRoomOptions").select("li.current").text();

        dto.setRoom(room);
        dto.setWardString(ward);
        dto.setStreetString(street);
        dto.setBrandString(brand);
        dto.setLongitude(longitude);
        dto.setLatitude(latitude);
        dto.setImages(images);
        dto.setDetail(detail);
    }
}
