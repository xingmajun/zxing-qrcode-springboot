package com.zxing.controller;

import com.google.common.collect.Maps;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;

@RestController
public class ZxingController {

    @RequestMapping(value = "/zxing",method = RequestMethod.GET)
    public void zxingMap( HttpServletResponse response) throws Exception{
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType,Object> hints  = Maps.newHashMap();
        hints.put(EncodeHintType.CHARACTER_SET,"utf-8");//中文编码
        hints.put(EncodeHintType.MARGIN,2);//中心扫码区域距离边距 空白区域
        BitMatrix bitMatrix = qrCodeWriter.encode("http://www.baidu.com" , BarcodeFormat.QR_CODE,400,400,hints);
        //onColor 为 二维码颜色
        MatrixToImageConfig config = new MatrixToImageConfig(-88888,-1);

        //向二维码中添加Logo
        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix, config);
        Graphics2D g = image.createGraphics();
        BufferedImage localImg = ImageIO.read(new File("./src/main/resources/1.jpg"));
        int widthLogo = localImg.getWidth(), heightLogo = localImg.getHeight();
        int x = (image.getWidth() - widthLogo) / 2;
        int y = (image.getHeight() - heightLogo) / 2;
        g.drawImage(localImg, x, y, widthLogo, heightLogo, null);
        g.drawRoundRect(x, y, widthLogo, heightLogo, 15, 15);
        g.setStroke(new BasicStroke(5));
        g.drawRect(x, y, widthLogo, heightLogo);
        g.dispose();
        ImageIO.write(image,"png",response.getOutputStream());
//        MatrixToImageWriter.writeToStream(bitMatrix,"png",response.getOutputStream(),config);
    }
}
