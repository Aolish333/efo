package com.zhazhapan.efo.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * @author aolish333@gmail.com
 * @date 2019/4/27 16:45
 * User:Lee
 */
public class OperFile {

    static Configuration conf = new Configuration();

    static {
        conf.set("fs.defaultFS", "hdfs://min1:9000");
        conf.set("user","root");
    }


    /**
     * 重命名文件
     * @param oldName
     * @param newName
     * @throws IOException
     */
    public static void rename(String oldName,String newName) throws IOException{
        FileSystem fs = FileSystem.get(conf);
        Path oldPath = new Path(oldName);
        Path newPath = new Path(newName);
        boolean isok = fs.rename(oldPath, newPath);
        if(isok){
            System.out.println("rename ok!");
        }else{
            System.out.println("rename failure");
        }
        fs.close();
    }

    /**
     * 上传文件到hadoop服务器
     * @param src
     * @param dst
     * @throws IOException
     */
    public static void uploadFile(String src,String dst) throws IOException {
        FileSystem fs = FileSystem.get(conf);
        Path srcPath = new Path(src);
        Path dstPath = new Path(dst);
        fs.copyFromLocalFile(false, srcPath, dstPath);
        //打印文件路径
        System.out.println("Upload to "+conf.get("fs.default.name"));
        System.out.println("------------list files------------"+"\n");
        FileStatus[] fileStatus = fs.listStatus(dstPath);
        for (FileStatus file : fileStatus)
        {
            System.out.println(file.getPath());
        }
        fs.close();
    }


    //删除文件
    public static void delete(String filePath) throws IOException{
        //Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
        Path path = new Path(filePath);
        boolean isok = fs.deleteOnExit(path);
        if(isok){
            System.out.println("delete ok!");
        }else{
            System.out.println("delete failure");
        }
        fs.close();
    }


    //创建目录
    public static void mkdir(String path) throws IOException{
        //Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
        Path srcPath = new Path(path);
        boolean isok = fs.mkdirs(srcPath);
        if(isok){
            System.out.println("create " + path + " dir ok!");
        }else{
            System.out.println("create " + path + " dir failure");
        }
        fs.close();
    }

    //读取文件的内容
    public static void readFile(String filePath) throws IOException{
        //Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
        Path srcPath = new Path(filePath);
        InputStream in = null;
        try {
            in = fs.open(srcPath);
            IOUtils.copyBytes(in, System.out, 4096, false);
            //复制到标准输出流
        } finally {
            IOUtils.closeStream(in);
        }
    }

    /**
     * 遍历指定目录(direPath)下的所有文件
     */
    public static void  getDirectoryFromHdfs(String direPath){
        try {
            FileSystem fs = FileSystem.get(URI.create(direPath),conf);
            FileStatus[] filelist = fs.listStatus(new Path(direPath));
            for (int i = 0; i < filelist.length; i++) {
                System.out.println("_________" + direPath + "目录下所有文件______________");
                FileStatus fileStatus = filelist[i];
                System.out.println("Name:"+fileStatus.getPath().getName());
                System.out.println("Size:"+fileStatus.getLen());
                System.out.println("Path:"+fileStatus.getPath());
            }
            fs.close();
        } catch (Exception e){

        }
    }
}
