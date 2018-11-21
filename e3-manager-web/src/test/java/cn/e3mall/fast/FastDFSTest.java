package cn.e3mall.fast;

import cn.e3mall.utils.FastDFSClient;
import org.csource.fastdfs.*;
import org.junit.Test;

public class FastDFSTest {

    @Test
    public void testFileUpload() throws Exception {
        // 1、加载配置文件，配置文件中的内容就是tracker服务的地址。
        ClientGlobal.init("D:\\Git\\e3-parent\\e3-manager-web\\src\\main\\resources\\conf\\client.conf");
        // 2、创建一个TrackerClient对象。直接new一个。
        TrackerClient trackerClient = new TrackerClient();
        // 3、使用TrackerClient对象创建连接，获得一个TrackerServer对象。
        TrackerServer trackerServer = trackerClient.getConnection();
        // 4、创建一个StorageServer的引用，值为null
        StorageServer storageServer = null;
        // 5、创建一个StorageClient对象，需要两个参数TrackerServer对象、StorageServer的引用
        StorageClient storageClient = new StorageClient(trackerServer, storageServer);
        // 6、使用StorageClient对象上传图片。
        //扩展名不带“.”
        String[] strings = storageClient.upload_file("D:\\Git\\e3-parent\\e3-manager-web\\src\\main\\resources\\reba.jpg", "jpg", null);
        // 7、返回数组。包含组名和图片的路径。
        for (String string : strings) {
            System.out.println(string);
        }
    }

    @Test
    public void testFastDfsClient()throws Exception{

           FastDFSClient fastDFSClient = new FastDFSClient(
                   "D:\\Git\\e3-parent\\e3-manager-web\\src\\main\\resources\\conf\\client.conf");
           String string=fastDFSClient.uploadFile(
                   "D:\\Git\\e3-parent\\e3-manager-web\\src\\main\\resources\\reba.jpg");
           System.out.println(string);
    }

}
