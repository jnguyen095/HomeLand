package com.test.business.impl;

import com.test.business.ProductManagementRemoteBean;
import com.test.domain.*;
import com.test.dto.BatDongSanDTO;
import com.test.session.*;
import com.test.utils.DozerSingletonMapper;
import org.apache.commons.lang.StringUtils;

import javax.ejb.DuplicateKeyException;
import javax.ejb.EJB;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.Stateless;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nkhang
 * Date: 10/16/15
 * Time: 11:40 AM
 * To change this template use File | Settings | File Templates.
 */
@Stateless(name = "ProductManagementSessionEJB")
public class ProductManagementSessionBean implements ProductManagementRemoteBean {

    @EJB
    private ProductLocalBean productLocalBean;
    @EJB
    private CategoryLocalBean categoryLocalBean;
    @EJB
    private CityLocalBean cityLocalBean;
    @EJB
    private DistrictLocalBean districtLocalBean;
    @EJB
    private DirectionLocalBean directionLocalBean;
    @EJB
    private WardLocalBean wardLocalBean;
    @EJB
    private StreetLocalBean streetLocalBean;
    @EJB
    private BrandLocalBean brandLocalBean;
    @EJB
    private ProductAssetLocalBean productAssetLocalBean;
    @EJB
    private CrawlerHistoryLocalBean crawlerHistoryLocalBean;

    @Override
    public Integer[] saveOrUpdate(Integer categoryId, List<BatDongSanDTO> items) {
        int saved = 0, exists = 0, error = 0;
        for(BatDongSanDTO batDongSanDTO : items){
            Boolean isExists = productLocalBean.findByCode(batDongSanDTO.getCode());
            if(!isExists){
                Boolean ok = validateRequired(batDongSanDTO);
                if(ok){
                    try{
                        ProductEntity productEntity = saveProduct(categoryId, batDongSanDTO);
                        saveProductAsset(productEntity, batDongSanDTO.getImages());
                        saved++;
                    }catch (DuplicateKeyException e){
                        error++;
                    }
                }else{
                    error++;
                }
            }else{
                exists++;
            }
        }
        Integer[] result = new Integer[3];
        result[0] = saved;
        result[1] = exists;
        result[2] = error;
        updateCrawlerHistory(categoryId, saved, exists, error);
        return result;
    }

    private void updateCrawlerHistory(Integer categoryId, Integer saved, Integer exists, Integer error){
        try{
            CrawlerHistoryEntity crawlerHistoryEntity = crawlerHistoryLocalBean.findToday(categoryId);
            if(crawlerHistoryEntity != null){
                crawlerHistoryEntity.setAdded(crawlerHistoryEntity.getAdded() + saved);
                crawlerHistoryEntity.setUpdated(0);
                crawlerHistoryEntity.setError(crawlerHistoryEntity.getError() + error);
                crawlerHistoryEntity.setUpdatedDate(crawlerHistoryEntity.getUpdatedDate());
                crawlerHistoryLocalBean.update(crawlerHistoryEntity);
            }else{
                crawlerHistoryEntity = new CrawlerHistoryEntity();
                crawlerHistoryEntity.setAdded(saved);
                crawlerHistoryEntity.setUpdated(0);
                crawlerHistoryEntity.setError(error);
                CategoryEntity categoryEntity = new CategoryEntity();
                categoryEntity.setCategoryId(categoryId);
                crawlerHistoryEntity.setCategory(categoryEntity);
                crawlerHistoryLocalBean.save(crawlerHistoryEntity);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private Boolean validateRequired(BatDongSanDTO batDongSanDTO){
        Boolean ok = Boolean.TRUE;
        if(StringUtils.isBlank(batDongSanDTO.getCode())){
            ok = Boolean.FALSE;
        }else if(StringUtils.isBlank(batDongSanDTO.getTitle())){
            ok = Boolean.FALSE;
        }else if(StringUtils.isBlank(batDongSanDTO.getBrief())){
            ok = Boolean.FALSE;
        }else if(StringUtils.isBlank(batDongSanDTO.getPostDateStr())){
            ok = Boolean.FALSE;
        }else if(StringUtils.isBlank(batDongSanDTO.getExpireDateStr())){
            ok = Boolean.FALSE;
        }

        return ok;
    }

    private void saveProductAsset(ProductEntity productEntity, List<String> images) {
        if(images != null && images.size() > 0){
            for(String img : images){
                ProductAssetEntity productAssetEntity = new ProductAssetEntity();
                productAssetEntity.setOrgUrl(img);
                productAssetEntity.setUrl(img);
                productAssetEntity.setProduct(productEntity);
                try{
                    productAssetLocalBean.save(productAssetEntity);
                }catch (DuplicateKeyException e){}
            }
        }
    }

    private ProductEntity saveProduct(Integer categoryId, BatDongSanDTO batDongSanDTO) throws DuplicateKeyException{
        ProductEntity productEntity = DozerSingletonMapper.getInstance().map(batDongSanDTO, ProductEntity.class);

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategoryId(categoryId);
        productEntity.setCategory(categoryEntity);

        if(StringUtils.isNotBlank(batDongSanDTO.getCityDist())){
            String[] strs = batDongSanDTO.getCityDist().split(",");
            if(strs.length == 2){
                String city = strs[1].trim();
                String district = strs[0].trim();
                CityEntity cityEntity = saveOrLoadCity(city);
                if(cityEntity != null){
                    productEntity.setCity(cityEntity);
                    DistrictEntity districtEntity = saveOrLoadDistrict(cityEntity, district);
                    if(districtEntity != null){
                        productEntity.setDistrict(districtEntity);
                        WardEntity wardEntity = saveOrLoadWard(districtEntity, batDongSanDTO.getWardString());
                        if(wardEntity != null){
                            productEntity.setWard(wardEntity);
                            StreetEntity streetEntity = saveOrLoadStreet(wardEntity, batDongSanDTO.getStreetString());
                            if(streetEntity != null){
                                productEntity.setStreet(streetEntity);
                            }
                        }
                    }
                }
            }
        }
        DirectionEntity directionEntity = saveOrLoadDirection(batDongSanDTO.getDirectionString());
        if(directionEntity != null){
            productEntity.setDirection(directionEntity);
        }

        BrandEntity brandEntity = saveOrLoadBrand(batDongSanDTO.getBrandString());
        if(brandEntity != null){
            productEntity.setBrand(brandEntity);
        }

        productEntity.setPostDate(parseDate(batDongSanDTO.getPostDateStr()));
        productEntity.setExpireDate(parseDate(batDongSanDTO.getExpireDateStr()));

        productEntity = productLocalBean.save(productEntity);

        return productEntity;
    }

    private BrandEntity saveOrLoadBrand(String brandString){
        BrandEntity brandEntity = null;
        if(StringUtils.isNotBlank(brandString)){
            try{
                brandEntity = brandLocalBean.findEqualUnique("brandName", brandString);
            }catch (ObjectNotFoundException e){
                brandEntity = new BrandEntity();
                brandEntity.setBrandName(brandString);
                brandEntity.setDescription(brandString);
                try{
                    brandEntity = brandLocalBean.save(brandEntity);
                }catch (DuplicateKeyException de){}
            }
        }
        return brandEntity;
    }

    private StreetEntity saveOrLoadStreet(WardEntity wardEntity, String street){
        StreetEntity streetEntity = null;
        try{
            streetEntity = streetLocalBean.findEqualUnique("streetName", street);
        }catch (ObjectNotFoundException e){
            streetEntity = new StreetEntity();
            streetEntity.setWard(wardEntity);
            streetEntity.setStreetName(street);
            try{
                streetEntity = streetLocalBean.save(streetEntity);
            }catch (DuplicateKeyException du){}
        }
        return streetEntity;
    }

    private WardEntity saveOrLoadWard(DistrictEntity districtEntity, String ward){
        WardEntity wardEntity = null;
        if(StringUtils.isNotBlank(ward)){
            try{
                wardEntity = wardLocalBean.findEqualUnique("wardName", ward);
            }catch (ObjectNotFoundException e){
                wardEntity = new WardEntity();
                wardEntity.setDistrict(districtEntity);
                wardEntity.setWardName(ward);
                try{
                    wardEntity = wardLocalBean.save(wardEntity);
                }catch (DuplicateKeyException de){}
            }
        }
        return wardEntity;
    }

    private Timestamp parseDate(String dateStr){
        Timestamp result = null;
        if(StringUtils.isNotBlank(dateStr)){
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            try{
                Date date = format.parse(dateStr);
                result = new Timestamp(date.getTime());
            }catch (ParseException e){}
        }
        return result;
    }

    private DirectionEntity saveOrLoadDirection(String direction){
        DirectionEntity directionEntity = null;
        if(StringUtils.isNotBlank(direction)){
            try{
                directionEntity = directionLocalBean.findEqualUnique("directionName", direction);
            }catch (ObjectNotFoundException e){
                directionEntity = new DirectionEntity();
                directionEntity.setDirectionName(direction);
                try{
                    directionEntity = directionLocalBean.save(directionEntity);
                }catch (DuplicateKeyException de){}
            }
        }
        return directionEntity;
    }

    private DistrictEntity saveOrLoadDistrict(CityEntity city, String district) {
        DistrictEntity districtEntity = null;
        try{
            districtEntity = districtLocalBean.findEqualUnique("districtName", district);
        }catch (ObjectNotFoundException e){
            districtEntity = new DistrictEntity();
            districtEntity.setDistrictName(district);
            districtEntity.setCity(city);
            try{
                districtEntity = districtLocalBean.save(districtEntity);
            }catch (DuplicateKeyException de){}
        }
        return districtEntity;
    }

    private CityEntity saveOrLoadCity(String city){
        CityEntity cityEntity = null;
        try{
            cityEntity = cityLocalBean.findEqualUnique("cityName", city);
        }catch (ObjectNotFoundException e){
            cityEntity = new CityEntity();
            cityEntity.setCityName(city);
            try{
                cityEntity = cityLocalBean.save(cityEntity);
            }catch (DuplicateKeyException de){}
        }
        return cityEntity;
    }
}
