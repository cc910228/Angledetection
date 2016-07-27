package com.khu.chao.angledetection;



import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileOutputStream;

public class MainActivity extends Activity {

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //通过Id来获取界面中组件的引用
        Button rgb2greyBtn  = (Button) findViewById(R.id.rgb2greybtn);
        final ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);
        final ImageView imageView2 = (ImageView) findViewById(R.id.imageView2);
        //为“转换为灰度图”按钮添加监听事件
        rgb2greyBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //将转换过后的灰度图显示出来
                //通过位图工厂，创建一个位图
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Trim/5test2.jpg";
                    //String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Trim/Swing201602261127.png";
                    final Bitmap bitmap = BitmapFactory.decodeFile(path);
                    //imageView1.setImageBitmap(bitmap);
                    Bitmap result = convertGreyImg(bitmap);
                    imageView2.setImageBitmap(result);
                    saveBitmapToSDCard(result,"angleresult");

            }
        });

    }

    /**
     * 将彩色图转换为灰度图
     * @param img 位图
     * @return  返回转换好的位图
     */
    public Bitmap convertGreyImg(Bitmap img) {
        int width = img.getWidth();         //获取位图的宽
        int height = img.getHeight();       //获取位图的高
        Toast.makeText(getApplicationContext(), "width="+width+",height="+height, Toast.LENGTH_SHORT).show();
        int threshold=0x0014FFFF;

        int distance=0;
        double angle=0;
        double ref_ab_2=width*1021/1839;
        double ref_bc_2=height*978/3264;
        double ref_ab_3=width*740/1839;
        double ref_bc_3=height*711/3264;
        double ref_ab_4=width*529/1839;
        double ref_bc_4=height*511/3264;
        double ref_ab_5=width*388/1839;
        double ref_bc_5=height*376/3264;
        double ref_ab_6=width*324/1839;
        double ref_bc_6=height*312/3264;


        int []pixels = new int[width * height]; //通过位图的大小创建像素点数组
        int []v = new int[width * height];

        img.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for(int i = 0; i < height; i++)  {
            for(int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];

                int red = ((grey  & 0x00FF0000 ) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                grey = (int)((float) red * 0.3 + (float)green * 0.59 + (float)blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }

        //angle
        int gap = 50;
        int win = (int)Math.floor(height/100);
        boolean flag = false;
        for(int i1 = 11+win; i1 <= (int)Math.floor(3*height/5); i1++)  {
            for(int j1 = 11+win; j1 <= (int)Math.floor(2*width/3); j1++) {
            int count1=0;
                for(int k1=-win;k1<=win;k1++){
                    for(int k2=-win;k2<=win;k2++){
                        if(pixels[(i1+k1)*width+j1+k2]>-14000000){
                            count1=count1+1;
                        }
                    }
                }
                if(count1<5){
                    for(int i2 = i1-10; i2 <= i1+10; i2++)  {
                        for(int j2 = j1+gap; j2 <= width-win; j2++) {
                            int count2=0;
                            for(int k1=-win;k1<=win;k1++){
                                for(int k2=-win;k2<=win;k2++){
                                    if(pixels[(i2+k1)*width+j2+k2]>-14000000){
                                        count2=count2+1;
                                    }
                                }
                            }
                            if(count2<5){
                                for(int i3 = i2+gap; i3 <= height-win; i3++)  {
                                    for(int j3 = j2-10; j3 <= j2+10; j3++) {
                                        int count3=0;
                                        for(int k1=-win;k1<=win;k1++){
                                            for(int k2=-win;k2<=win;k2++){
                                                if(pixels[(i3+k1)*width+j3+k2]>-14000000){
                                                    count3=count3+1;
                                                }
                                            }
                                        }
                                        if(count3<5){
                                            //Toast.makeText(getApplicationContext(), "i1="+i1+",j1="+j1+",gray="+pixels[i1*width+j1], Toast.LENGTH_SHORT).show();
                                            for(int k1=-5;k1<=5;k1++){
                                                for(int k2=-5;k2<=5;k2++){
                                                    pixels[(i1+k1)*width+j1+k2]=255;
                                                }
                                            }
                                            //Toast.makeText(getApplicationContext(), "i2="+i2+",j2="+j2+",gray="+pixels[i2*width+j2], Toast.LENGTH_SHORT).show();
                                            for(int k1=-5;k1<=5;k1++){
                                                for(int k2=-5;k2<=5;k2++){
                                                    pixels[(i2+k1)*width+j2+k2]=255;
                                                }
                                            }
                                            //Toast.makeText(getApplicationContext(), "i3="+i3+",j3="+j3+",gray="+pixels[i3*width+j3], Toast.LENGTH_SHORT).show();
                                            for(int k1=-5;k1<=5;k1++){
                                                for(int k2=-5;k2<=5;k2++){
                                                    pixels[(i3+k1)*width+j3+k2]=255;
                                                }
                                            }

                                            int abdistance= (int)Math.floor(Math.sqrt((i1-i2)*(i1-i2)+(j1-j2)*(j1-j2)));
                                            //Toast.makeText(getApplicationContext(), "ab="+abdistance, Toast.LENGTH_SHORT).show();
                                            int bcdistance= (int)Math.floor(Math.sqrt((i3-i2)*(i3-i2)+(j3-j2)*(j3-j2)));
                                            //Toast.makeText(getApplicationContext(), "bc="+bcdistance, Toast.LENGTH_SHORT).show();

                                            if(bcdistance<ref_bc_2 && bcdistance>ref_bc_3){
                                                if(bcdistance<(ref_bc_2+ref_bc_3)/2){
                                                    distance=3;
                                                    angle=(int)Math.floor(Math.toDegrees(Math.acos(abdistance/ref_ab_3)));
                                                    Toast.makeText(getApplicationContext(), "distance="+distance+",angle="+angle, Toast.LENGTH_SHORT).show();
                                                }else{
                                                    distance=2;
                                                    angle=(int)Math.floor(Math.toDegrees(Math.acos(abdistance/ref_ab_2)));
                                                    Toast.makeText(getApplicationContext(), "distance="+distance+",angle="+angle, Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            else if(bcdistance<ref_bc_3 && bcdistance>ref_bc_4){
                                                if(bcdistance<(ref_bc_3+ref_bc_4)/2){
                                                    distance=4;
                                                    angle=(int)Math.floor(Math.toDegrees(Math.acos(abdistance/ref_ab_4)));
                                                    Toast.makeText(getApplicationContext(), "distance="+distance+",angle="+angle, Toast.LENGTH_SHORT).show();
                                                }else{
                                                    distance=3;
                                                    angle=(int)Math.floor(Math.toDegrees(Math.acos(abdistance/ref_ab_3)));
                                                    Toast.makeText(getApplicationContext(), "distance="+distance+",angle="+angle, Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            else if(bcdistance<ref_bc_4 && bcdistance>ref_bc_5){
                                                if(bcdistance<(ref_bc_4+ref_bc_5)/2){
                                                    distance=5;
                                                    angle=Math.toDegrees(Math.acos(abdistance/ref_ab_5));
                                                    Toast.makeText(getApplicationContext(), "distance="+distance+",angle="+angle, Toast.LENGTH_SHORT).show();
                                                }else{
                                                    distance=4;
                                                    angle=(int)Math.floor(Math.toDegrees(Math.acos(abdistance/ref_ab_4)));
                                                    Toast.makeText(getApplicationContext(), "distance="+distance+",angle="+angle, Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            else if(bcdistance<ref_bc_5 && bcdistance>ref_bc_6){
                                                if(bcdistance<(ref_bc_5+ref_bc_6)/2){
                                                    distance=6;
                                                    angle=(int)Math.floor(Math.toDegrees(Math.acos(abdistance/ref_ab_6)));
                                                    Toast.makeText(getApplicationContext(), "distance="+distance+",angle="+angle, Toast.LENGTH_SHORT).show();
                                                }else{
                                                    distance=5;
                                                    angle=Math.toDegrees(Math.acos(abdistance/ref_ab_5));
                                                    Toast.makeText(getApplicationContext(), "distance="+distance+",angle="+angle, Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            else if(bcdistance>ref_bc_2){
                                                Toast.makeText(getApplicationContext(), "it is too close", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                Toast.makeText(getApplicationContext(), "distance is not in range", Toast.LENGTH_SHORT).show();
                                            }



                                            flag=true;
                                            break;
                                        }
                                    }
                                    if (flag==true){
                                        break;
                                    }
                                }

                            }
                            if (flag==true){
                                break;
                            }
                        }
                        if (flag==true){
                            break;
                        }
                    }


                }
                if (flag==true){
                    break;
                }

            }
            if (flag==true){
                break;
            }
        }

        //angle


        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        result.setPixels(pixels, 0, width, 0, 0, width, height);
        return result;
    }

    public static void saveBitmapToSDCard(Bitmap bitmap,String imagename)
    {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("/sdcard/Trim/edge/" + imagename + ".jpg");
            //fos = new FileOutputStream("/sdcard/Trim/edgeout.jpg");
            if (fos != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}

