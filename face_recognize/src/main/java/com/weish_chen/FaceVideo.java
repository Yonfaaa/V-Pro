package com.weish_chen;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;
import java.io.File;

import java.util.Arrays;

import java.util.Base64;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Rect;
import org.opencv.objdetect.CascadeClassifier;
import java.util.Base64;
/**
 * Opencv 图片人脸识别、实时摄像头人脸识别、视频文件人脸识别
 */
public class FaceVideo {

    // 初始化人脸探测器
    static CascadeClassifier faceDetector;

    static int i = 0;

    static {
        // 判断系统
        String os = System.getProperty("os.name");

        // 加载动态库
        if (os != null && os.toLowerCase().startsWith("windows")) {
            // Windows操作系统
            // todo windows 系统部署加载 .dll 文件 - 路径跟据自己存放位置更改
            System.load("E:\\opencv_4.70\\opencv\\build\\java\\x64\\opencv_java470.dll");
//            ClassLoader.getSystemResource("dlls/opencv_java470.dll");
        } else if (os != null && os.toLowerCase().startsWith("linux")) {
            // Linux操作系统
            // todo Linux 服务器部署加载 .so 文件 - 路径跟据自己存放位置更改
            System.load("/opt/face/libopencv_java440.so");
        }

        // 引入 特征分类器配置 文件：haarcascade_frontalface_alt.xml 文件路径
        String property = "E:\\opencv_4.70\\opencv\\build\\etc\\haarcascades\\haarcascade_frontalface_alt.xml";
        System.out.println(property);
        faceDetector = new CascadeClassifier(property);
    }

    private static final String PATH_PREFIX = "D:\\JAVA\\projects\\face_recognize\\face_img\\faces\\";

    public static void main(String[] args) {
        String libraryPath = System.getProperty("java.library.path");
        System.out.println("Java Library Path: " + libraryPath);
        // 1- 从摄像头实时人脸识别，识别成功保存图片到本地
//        getVideoFromCamera();

        // 2- 从本地视频文件中识别人脸
//        getVideoFromFile();

        // 3- 本地图片人脸识别，识别成功并保存人脸图片到本地
//        face("5-1.jpg");

        // 4- 比对本地2张图的人脸相似度 （越接近1越相似）
//        double compareHist = compare_image(PATH_PREFIX + "5-1.jpg", PATH_PREFIX + "6-1.jpg");
//        System.out.println(compareHist);
//        if (compareHist > 0.72) {
//
//            System.out.println("人脸匹配");
//        } else {
//
//            System.out.println("人脸不匹配");
//        }


        String test_image = "D:\\edge_download\\captured_image (21).png";
        double compareHist = 0;
        // 指定要遍历的目录路径
        String directoryPath = "D:\\JAVA\\projects\\face_recognize\\face_img\\faces";

        // 创建File对象，代表目录
        File directory = new File(directoryPath);

        // 获取目录下的所有文件
        File[] files = directory.listFiles();
        int i=0, k = 0;
        for (File file : files) {
            double temp = compare_image(file.getAbsolutePath(), test_image);
            System.out.println(String.format("comparehist to" + file.getAbsolutePath() + "is : %.2f", temp));
            if(temp > compareHist){
                compareHist = temp;
                i = k;
            }
            k++;
        }
        if(compareHist < 0.5){
            System.out.println("doesn't match");
        }
        else
            System.out.println("the most match one is " + files[i].getAbsolutePath());


    }

    private static final String DB_URL = "jdbc:mysql://localhost:3306/mysql";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    // 读取数据库中的 base64 图像数据
//    public static void readBase64ImagesFromTable(String tableName, String columnName) {
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//
//        try {
//            // 连接数据库
//            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
//
//            // 准备查询语句
//            String sql = "SELECT " + columnName + " FROM " + tableName;
//            stmt = conn.prepareStatement(sql);
//
//            // 执行查询
//            rs = stmt.executeQuery();
//
//            // 逐行处理结果集
//            while (rs.next()) {
//                // 从结果集中读取base64编码的图像数据
//                String base64Data = rs.getString(columnName);
//                System.out.println(base64Data);
//                // 提取实际的 base64 编码字符串（去掉前缀）
//                String base64Image = base64Data.split(",")[1];
//                // 对base64编码进行解码
//                byte[] imageBytes = Base64.getDecoder().decode(base64Image);
//                // 现在您可以使用这些图像数据进行进一步处理，比如显示、保存等
//                // 这里仅示例打印图像数据的长度
//                System.out.println("Image size: " + imageBytes.length);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            // 关闭资源
//            try {
//                if (rs != null) rs.close();
//                if (stmt != null) stmt.close();
//                if (conn != null) conn.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
    public static String readBase64ImagesFromTable(String tableName, String columnName) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String res = "";
        try {
            // 连接数据库
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            // 准备查询语句
            String sql = "SELECT " + columnName + " FROM " + tableName;
            stmt = conn.prepareStatement(sql);

            // 执行查询
            rs = stmt.executeQuery();
            rs.next();
            res = rs.getString(columnName);
            System.out.println(res);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return res;
    }
    /**
     * OpenCV-4.7.0 从摄像头实时读取
     */
    public static void getVideoFromCamera() {
        //1 如果要从摄像头获取视频 则要在 VideoCapture 的构造方法写 0
        VideoCapture capture = new VideoCapture(0);
        Mat video = new Mat();
        int index = 0;
        if (capture.isOpened()) {

            while (i < 3) {
                // 匹配成功3次退出
                capture.read(video);
                HighGui.imshow("实时人脸识别", getFace(video));
                index = HighGui.waitKey(100);
                if (index == 27) {

                    capture.release();
                    break;
                }
            }
        } else {

            System.out.println("摄像头未开启");
        }
        try {

            capture.release();
            Thread.sleep(1000);
            System.exit(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return;
    }

    /**
     * OpenCV-4.7.0 从视频文件中读取
     */
    public static void getVideoFromFile() {

        VideoCapture capture = new VideoCapture();
        capture.open(PATH_PREFIX + "yimi.mp4");//1 读取视频文件的路径

        if (!capture.isOpened()) {

            System.out.println("读取视频文件失败！");
            return;
        }
        Mat video = new Mat();
        int index = 0;
        while (capture.isOpened()) {

            capture.read(video);//2 视频文件的视频写入 Mat video 中
            HighGui.imshow("本地视频识别人脸", getFace(video));//3 显示图像
            index = HighGui.waitKey(100);//4 获取键盘输入
            if (index == 27) {
                //5 如果是 Esc 则退出
                capture.release();
                return;
            }
        }
    }

    /**
     * OpenCV-4.7.0 人脸识别
     *
     * @param image 待处理Mat图片(视频中的某一帧)
     * @return 处理后的图片
     */
    public static Mat getFace(Mat image) {
        // 1 读取OpenCV自带的人脸识别特征XML文件(faceDetector)
//        CascadeClassifier facebook = new CascadeClassifier("D:\\Sofeware\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_alt.xml");
        // 2 特征匹配类
        MatOfRect face = new MatOfRect();
        // 3 特征匹配
        faceDetector.detectMultiScale(image, face);
        Rect[] rects = face.toArray();
        System.out.println("匹配到 " + rects.length + " 个人脸");
        if (rects != null && rects.length >= 1) {

            // 4 为每张识别到的人脸画一个圈
            for (int i = 0; i < rects.length; i++) {

                Imgproc.rectangle(image, new Point(rects[i].x, rects[i].y), new Point(rects[i].x + rects[i].width, rects[i].y + rects[i].height), new Scalar(0, 255, 0));
                Imgproc.putText(image, "Human", new Point(rects[i].x, rects[i].y), Imgproc.FONT_HERSHEY_SCRIPT_SIMPLEX, 1.0, new Scalar(0, 255, 0), 1, Imgproc.LINE_AA, false);
                //Mat dst=image.clone();
                //Imgproc.resize(image, image, new Size(300,300));
            }
            i++;
            if (i == 3) {
                // 获取匹配成功第10次的照片
                Imgcodecs.imwrite(PATH_PREFIX + "face.png", image);
            }
        }
        return image;
    }


    /**
     * OpenCV-4.7.0 图片人脸识别
     * 从图像中检测人脸并剪裁出来，保存在本地
     */
    public static void face(String filename) {
        // 1 读取OpenCV自带的人脸识别特征XML文件
        // OpenCV 图像识别库一般位于 opencv\sources\data 下面
//        CascadeClassifier facebook=new CascadeClassifier("E:\\opencv_4.70\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_alt.xml");
        // 2 读取测试图片
        String imgPath = PATH_PREFIX + filename;
        Mat image = Imgcodecs.imread(imgPath);
        if (image.empty()) {

            System.out.println("image 内容不存在！");
            return;
        }
        // 3 特征匹配
        MatOfRect face = new MatOfRect();
        faceDetector.detectMultiScale(image, face);
        // 4 匹配 Rect 矩阵 数组
        Rect[] rects = face.toArray();
        System.out.println("匹配到 " + rects.length + " 个人脸");
        // 5 为每张识别到的人脸画一个圈
        int i = 1;
        for (Rect rect : face.toArray()) {

            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 255, 0), 3);
            imageCut(imgPath, PATH_PREFIX + i + ".jpg", rect.x, rect.y, rect.width, rect.height);// 进行图片裁剪
            i++;
        }
        // 6 展示图片
        HighGui.imshow("人脸识别", image);
        HighGui.waitKey(0);
    }

    /**
     * 裁剪人脸
     *
     * @param imagePath
     * @param outFile
     * @param posX
     * @param posY
     * @param width
     * @param height
     */
    public static void imageCut(String imagePath, String outFile, int posX, int posY, int width, int height) {
        // 原始图像
        Mat image = Imgcodecs.imread(imagePath);
        // 截取的区域：参数,坐标X,坐标Y,截图宽度,截图长度
        Rect rect = new Rect(posX, posY, width, height);
        // 两句效果一样
        Mat sub = image.submat(rect); // Mat sub = new Mat(image, rect);
        Mat mat = new Mat();
        Size size = new Size(width, height);
        Imgproc.resize(sub, mat, size);// 将人脸进行截图并保存
        Imgcodecs.imwrite(outFile, mat);
        System.out.println(String.format("图片裁切成功，裁切后图片文件为： %s", outFile));
    }

    /**
     * 人脸比对
     *
     * @param img_1
     * @param img_2
     * @return
     */
    public static double compare_image(String img_1, String img_2) {

        Mat mat_1 = conv_Mat(img_1);
        Mat mat_2 = conv_Mat(img_2);
        Mat hist_1 = new Mat();
        Mat hist_2 = new Mat();

        //颜色范围
        MatOfFloat ranges = new MatOfFloat(0f, 256f);
        //直方图大小， 越大匹配越精确 (越慢)
        MatOfInt histSize = new MatOfInt(1000);

        Imgproc.calcHist(Arrays.asList(mat_1), new MatOfInt(0), new Mat(), hist_1, histSize, ranges);
        Imgproc.calcHist(Arrays.asList(mat_2), new MatOfInt(0), new Mat(), hist_2, histSize, ranges);

        // CORREL 相关系数
        double res = Imgproc.compareHist(hist_1, hist_2, Imgproc.CV_COMP_CORREL);
        return res;
    }

    /**
     * 灰度化人脸
     *
     * @param img
     * @return
     */
    public static Mat conv_Mat(String img) {
        Mat image0;

        // 如果输入的是 base64 编码的图像数据
        if (img.startsWith("data:image")) {
            image0 = convMatFromBase64(img);
        } else {
            image0 = Imgcodecs.imread(img);
        }

        Mat image1 = new Mat();
        // 灰度化
        Imgproc.cvtColor(image0, image1, Imgproc.COLOR_BGR2GRAY);
        // 探测人脸
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image1, faceDetections);
        System.out.println("检测" + faceDetections.toArray().length + " 张人脸");
        // rect中人脸图片的范围
        for (Rect rect : faceDetections.toArray()) {
            Mat face = new Mat(image1, rect);
            return face;
        }
        return null;
    }

    // 从 base64 数据中解码为 Mat 对象
    private static Mat convMatFromBase64(String base64Img) {
        // 提取实际的 base64 编码字符串（去掉前缀）
        String base64Image = base64Img.split(",")[1];
        // 解码 base64 编码字符串
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        // 将字节数组转换为 Mat 对象
        Mat mat = Imgcodecs.imdecode(new MatOfByte(imageBytes), Imgcodecs.IMREAD_COLOR);
        return mat;
    }
    /**
     * OpenCV-4.7.0 将摄像头拍摄的视频写入本地
     */
    public static void writeVideo() {

        //1 如果要从摄像头获取视频 则要在 VideoCapture 的构造方法写 0
        VideoCapture capture = new VideoCapture(0);
        Mat video = new Mat();
        int index = 0;
        Size size = new Size(capture.get(Videoio.CAP_PROP_FRAME_WIDTH), capture.get(Videoio.CAP_PROP_FRAME_HEIGHT));
        VideoWriter writer = new VideoWriter("D:/a.mp4", VideoWriter.fourcc('D', 'I', 'V', 'X'), 15.0, size, true);
        while (capture.isOpened()) {

            capture.read(video);//2 将摄像头的视频写入 Mat video 中
            writer.write(video);
            HighGui.imshow("像头获取视频", video);//3 显示图像
            index = HighGui.waitKey(100);//4 获取键盘输入
            if (index == 27) {
                //5 如果是 Esc 则退出
                capture.release();
                writer.release();
                return;
            }
        }
    }

}
