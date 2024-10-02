package com.kkcf;

import com.kkcf.controller.DeptController;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
public class BeanTest {
    @Autowired
    private ApplicationContext applicationContext; // IOC 容器对象
    @Autowired
    private SAXReader saxReader;

    @Test
    public void testGetBean() {
        // 根据 Bean 对象的名称，获取
        DeptController bean1 = (DeptController) applicationContext.getBean("deptController"); // 如果在声明 Bean 对象时，没有指定名称，默认就是类名首字母小写。
        System.out.println(bean1);

        // 根据 Bean 的类型获取
        DeptController bean2 = applicationContext.getBean(DeptController.class);
        System.out.println(bean2);

        // 根据 Bean 的名称和类型获取
        DeptController bean3 = applicationContext.getBean("deptController", DeptController.class);
        System.out.println(bean3);
    }

    @Test
    public void testBeanScope() {
        for (int i = 0; i < 10; i++) {
            DeptController bean = applicationContext.getBean(DeptController.class);
            System.out.println(bean);
        }
    }

    @Test
    public void testThirdBean() throws DocumentException {
        //SAXReader saxReader = new SAXReader();

        Document document = saxReader.read(this.getClass().getClassLoader().getResource("1.xml"));
        Element rootElement = document.getRootElement();
        String name = rootElement.element("name").getText();
        String age = rootElement.element("age").getText();

        System.out.println(name + ":" + age);
    }
}
