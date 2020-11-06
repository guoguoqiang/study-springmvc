package org.spring.study.springmvc.context;

import org.spring.study.springmvc.annotation.AutoWired;
import org.spring.study.springmvc.annotation.Controller;
import org.spring.study.springmvc.annotation.Service;
import org.spring.study.springmvc.xml.XmlPaser;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SpringMvc 容器
 * @Author ggq
 * @Date 2020/11/6 14:22
 */
public class WebApplicationContext {

    //classpath:springmvc.xml
    String contextConfigLocation;

    //定义集合  用于存放 bean 的权限名|包名.类名
    List<String> classNameList = new ArrayList<String>();

    //创建Map集合用于扮演IOC容器：  key存放bean的名字   value存放bean实例
    public Map<String,Object> iocMap = new ConcurrentHashMap<>();

    public WebApplicationContext() {
    }

    public WebApplicationContext(String contextConfigLocation) {
        this.contextConfigLocation = contextConfigLocation;
    }

    /**
     * 初始化Spring容器
     */
    public void onRefresh(){

        //1、进行解析springmvc配置文件操作  ==》
        String pack = XmlPaser.getbasePackage(contextConfigLocation.split(":")[1]);

        String[] packs = pack.split(",");
        //2、进行包扫描
        for(String pa : packs){
            excuteScanPackage(pa);
        }

        //3、实例化容器中bean
        executeInstance();

        //4、进行 自动注入操作
        executeAutoWired();
    }

    //进行自动注入操作
    public void executeAutoWired(){

        try {
            //从容器中 取出  bean  ，然后判断 bean中是否有属性上使用 AutoWired，如果使用了该注解，就需要进行自动注入操作
            for (Map.Entry<String, Object> entry : iocMap.entrySet()) {
                //获取容器中的bean
                Object bean = entry.getValue();
                //获取bean中的属性
                Field[] fields = bean.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if(field.isAnnotationPresent(AutoWired.class)){
                        //获取注解中的value值|该值就是bean的name
                        AutoWired autoWiredAno =  field.getAnnotation(AutoWired.class);
                        String beanName = autoWiredAno.value();
                        //取消检查机制
                        field.setAccessible(true);
                        field.set(bean,iocMap.get(beanName));

                    }
                }


            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 实例化容器中的bean
     */
    public void executeInstance(){

        try{

            for (String className : classNameList) {

                Class<?> clazz =   Class.forName(className);

                if(clazz.isAnnotationPresent(Controller.class)){
                    //控制层 bean

                    String beanName = clazz.getSimpleName().substring(0,1).toLowerCase()+ clazz.getSimpleName().substring(1);
                    iocMap.put(beanName,clazz.newInstance());

                }else if(clazz.isAnnotationPresent(Service.class)){
                    //Service层  bean
                    Service serviceAn = clazz.getAnnotation(Service.class);
                    String beanName = serviceAn.value();
                    iocMap.put(beanName,clazz.newInstance());
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }


    }

    /**
     * 扫描包
     */
    public void excuteScanPackage(String pack){
        //   org.spring.study.controller   ==> org/spring/study/controller
        URL url = this.getClass().getClassLoader().getResource("/" + pack.replaceAll("\\.", "/"));
        String path = url.getFile();
        //
        File dir=new File(path);
        for(File f:dir.listFiles()){
            if(f.isDirectory()){
                //当前是一个文件目录
                excuteScanPackage(pack+"."+f.getName());
            }else{
                //文件目录下文件  获取全路径   UserController.class  ==> org.spring.study.controller.UserController.class
                String className=pack+"."+f.getName().replaceAll(".class","");
                classNameList.add(className);
            }
        }
    }

}
