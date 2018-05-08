package com.zw.util;


import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.*;
import java.util.Map;

/**
 * @Author xiahaiyang
 * @Create 2017年11月14日19:34:11
 **/
public class ContextToPdf {
    public static void insertPDF(Map map) throws IOException {
        // 1.新建document对象
        Document document = new Document();
        String context = "";//协议模板
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(map.get("url").toString());
            // 2.建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中。
            // 创建 PdfWriter 对象 第一个参数是对文档对象的引用，第二个参数是文件的实际名称，在该名称中还会给出其输出路径。
            PdfWriter writer = PdfWriter.getInstance(document, fos);
            // 3.打开文档
            document.open();
            context = String.valueOf(map.get("context"));
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            Font font = new Font(bfChinese);
            // 4.添加一个内容段落
            document.add(new Paragraph(context, font));
            // 5.关闭文档
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }
}
