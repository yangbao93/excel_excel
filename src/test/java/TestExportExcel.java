import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yonyou.Application;
import com.yonyou.entity.ClassInfo;
import com.yonyou.entity.ExcelExportEntity;
import com.yonyou.entity.Student;
import com.yonyou.service.ExcelService;

/**
 * excel导出样例
 * Created by yangbao on 2018/1/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class TestExportExcel {

    @Autowired
    private ExcelService excelService;

    @Test
    public void testExport() {
        Student stu = new Student();
        stu.setName("yonyou");
        stu.setAge(10);
        stu.setBirthday(new Date());
        ClassInfo cls = new ClassInfo();
        cls.setTeacher("teacher");
        cls.setClassname("class-name");
        List<ClassInfo> classInfos = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            classInfos.add(cls);
        }
        stu.setItems(classInfos);
        // 导出实体创建
        LinkedHashMap<String, String> headAttributeMap = new LinkedHashMap<>(16);
        headAttributeMap.put("姓名","name");
        headAttributeMap.put("年龄", "age");
        headAttributeMap.put("生日", "birthday");
        headAttributeMap.put("班级名称", "items.classname");
        headAttributeMap.put("班级教师", "items.teacher");
        ExcelExportEntity entity = new ExcelExportEntity();
        entity.setConditions(null);
        entity.setHeadAndAttributes(headAttributeMap);
        entity.setDatas((LinkedList<Object>) Arrays.<Object>asList(stu));
        // 设置导出条件
        LinkedHashMap<String, String> conditionsMap = new LinkedHashMap<>(10);
        conditionsMap.put("标题", "这是一个标题你信不");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMHH hh:mm:ss");
        String format = sdf.format(new Date());
        conditionsMap.put("时间", format);
        // 导出
        HSSFWorkbook sheets = excelService.exportExcelData(entity);
        try (OutputStream out = new FileOutputStream("D://TEMP.xls")) {
            sheets.write(out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
