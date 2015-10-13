package com.test.impl;

import com.test.BatDongSanService;
import com.test.Constants;
import com.test.business.CategoryManagementRemoteBean;
import com.test.dto.BatDongSanDTO;
import com.test.dto.CategoryDTO;
import com.test.dto.CategoryTreeDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.io.IOException;
import java.util.ArrayList;
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
public class BatDongSanServiceImpl implements BatDongSanService {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public void setCategoryManagementBean(CategoryManagementRemoteBean categoryManagementBean) {
        this.categoryManagementBean = categoryManagementBean;
    }

    private CategoryManagementRemoteBean categoryManagementBean;

    @Override
    public void crawler() throws Exception{
        String testUrl = "http://batdongsan.com.vn/ban-nha-rieng-tp-hcm";
        //String testUrl = "http://batdongsan.com.vn/ban-nha-rieng-ha-noi";
        List<BatDongSanDTO> listItems = new ArrayList<BatDongSanDTO>();
        for(int i = 1; i < 2; i++){
            testUrl = "http://batdongsan.com.vn/ban-nha-rieng-tp-hcm" + (i > 1 ? "/p" + i : "");
            List<BatDongSanDTO> items = getBrief(testUrl);
            //System.out.println(testUrl + " : " + items.size());
            listItems.addAll(items);
        }
        for(BatDongSanDTO bds : listItems){
            System.out.println(">>>>>>>>>>>>>>");
            System.out.println(bds.getHref());
            System.out.println(bds.getArea());
            System.out.println(bds.getCityDist());
            System.out.println(bds.getPrice());
            System.out.println(bds.getDetail());
            System.out.println(bds.getImages().size());
        }
    }

    @Override
    public void updateMainCategory() {
        String fullUrl = "http://batdongsan.com.vn";
        List<CategoryTreeDTO> trees = new ArrayList<CategoryTreeDTO>();
        try{
            Document doc = Jsoup.connect(fullUrl).userAgent(Constants.userAgent).get();
            Elements elements = doc.getElementsByClass("dropdown-navigative-menu").select("li.lv0");

            if(elements != null && elements.size() > 0){
                for(Element element : elements){
                    Elements aElm = element.getElementsByTag("a").not("li.lv1 a");
                    if(aElm != null){
                        CategoryTreeDTO tree = new CategoryTreeDTO();
                        CategoryDTO root = new CategoryDTO();
                        String text = aElm.get(0).text();
                        String url = aElm.get(0).attr("href");
                        root.setUrl(url);
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
                                children.setUrl(curl);
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

    private void getLastTextNode(Node node, Node parent){
        List<Node> nodes = node.childNodes();
        if(nodes != null && nodes.size() > 0){
            for(Node aNode : nodes){
                getLastTextNode(aNode, node);
            }
        }else{
            TextNode textNode =(TextNode)node;
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setName(textNode.text());
            logger.info(textNode.text());
        }
    }

    private void updateDetail(String url, BatDongSanDTO dto) throws Exception{
        String fullUrl = "http://batdongsan.com.vn" + url;
        Document doc = Jsoup.connect(fullUrl).userAgent(Constants.userAgent).get();
        String detail = doc.getElementById("product-detail").select("div.pm-content").html();
        String contactName = doc.getElementById("LeftMainContent__productDetail_contactName").select("div.right").text().trim();
        String contactPhone = doc.getElementById("LeftMainContent__productDetail_contactMobile").select("div.right").text().trim();
        try{
            String room = doc.getElementById("LeftMainContent__productDetail_roomNumber").select("div.right").text();
            dto.setRoom(room);
        }catch (Exception e){}

        try{
            String floor = doc.getElementById("LeftMainContent__productDetail_floor").select("div.right").text().split(" ")[0];
            dto.setFloor(floor);
        }catch (Exception e){}
        try{
            String widthSize = doc.getElementById("LeftMainContent__productDetail_frontEnd").select("div.right").text().split(" ")[0];
            dto.setWidthSize(widthSize);
        }catch (Exception e){}

        Elements elements = doc.getElementsByClass("pm-content-detail").select("div.left-detail").select("div");//.select("div");
        for(Element element : elements){
            try{
                if(element.select("div.left").text().trim().equals("Mã số")){
                    dto.setCode(element.select("div.right").text().trim());
                }
            }catch (Exception e){}
            try{
                if(element.select("div.left").text().trim().equals("Địa chỉ")){
                    dto.setAddress(element.select("div.right").text().trim());
                }
            }catch (Exception e){}
            try{
                if(element.select("div.left").text().trim().equals("Ngày đăng tin")){
                    dto.setPostDate(element.select("div.right").text().trim());
                }
            }catch (Exception e){}
            try{
                if(element.select("div.left").text().trim().equals("Ngày hết hạn")){
                    dto.setExpireDate(element.select("div.right").text().trim());
                }
            }catch (Exception e){}
        }
        Elements imageElements = doc.getElementById("product-detail").select("div.list-img").select("ul").select("li").select("img");
        List<String> images = new ArrayList<String>();
        for(Element imageElement: imageElements){
            images.add(imageElement.attr("src"));
        }

        dto.setImages(images);
        dto.setDetail(detail);
        dto.setContactName(contactName);
        dto.setContactPhone(contactPhone);
    }

    private List<BatDongSanDTO> getBrief(String url){
        List<BatDongSanDTO> items = new ArrayList<BatDongSanDTO>();
        try{
            Document doc = Jsoup.connect(url).userAgent(Constants.userAgent).get();
            Elements searchProductItems = doc.getElementsByClass("search-productItem");

            for (Element searchProductItem : searchProductItems) {
                String pTitle = searchProductItem.select("div.p-title").select("a").attr("title");
                String href = searchProductItem.select("div.p-title").select("a").attr("href");
                String brief = searchProductItem.select("div.p-main").select("div.p-content").select("div.p-main-text").text();
                String pThumb = searchProductItem.select("div.p-main").select("div.p-main-image-crop").select("a").select("img").attr("src");
                String pPrice = searchProductItem.select("div.p-main").select("div.p-bottom-crop").select("div.floatleft").select("span.product-price").text();
                String pArea = searchProductItem.select("div.p-main").select("div.p-bottom-crop").select("div.floatleft").select("span.product-area").text();
                String cityDist = searchProductItem.select("div.p-main").select("div.p-bottom-crop").select("div.floatleft").select("span.product-city-dist").text();

                BatDongSanDTO batDongSanDTO = new BatDongSanDTO();
                batDongSanDTO.setTitle(pTitle);
                batDongSanDTO.setHref(href);
                batDongSanDTO.setThumb(pThumb);
                batDongSanDTO.setPrice(pPrice);
                batDongSanDTO.setArea(pArea);
                batDongSanDTO.setCityDist(cityDist);
                batDongSanDTO.setBrief(brief);
                try{
                    updateDetail(href, batDongSanDTO);
                }catch (Exception e){
                    continue;
                }

                items.add(batDongSanDTO);
            }
        }catch (IOException e){
            System.out.print("Exception: " + e.getMessage());
        }
        return items;
    }
}
