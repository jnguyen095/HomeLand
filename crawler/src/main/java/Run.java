import com.test.business.CategoryManagementRemoteBean;
import com.test.dto.CategoryTreeDTO;
import com.test.impl.BatDongSanServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.List;
import java.util.Properties;

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
        //CategoryManagementRemoteBean categoryManagementRemoteBean = (CategoryManagementRemoteBean)context.getBean("categoryManagementRemoteBean");
        try{
            //batDongSanService.crawler();
            batDongSanService.updateMainCategory();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
