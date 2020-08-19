package cn.guet.gdmbs.util;

import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class DownloadFile {
    public void download(HttpServletResponse response,String fileName,String storePath,List<List<String>> data,List<String> attributes){
        Workbook workbook = POIUtil.createExcelFile(attributes, data, POIUtil.XLS);
        if (workbook == null) {
            throw new RuntimeException("create excel file error");
        }
        // 输出到临时文件
        File file1=new File(storePath);
        if(!file1.exists()){
            file1.mkdir();
        }
        OutputStream fout=null;
        try {
            fout=response.getOutputStream();
            response.reset();
            response.setContentType("application/x-download");//下面三行是关键代码，处理乱码问题
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename="+new String(fileName.getBytes("gbk"), "iso8859-1"));
            workbook.write(fout);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fout != null) {
                    fout.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
