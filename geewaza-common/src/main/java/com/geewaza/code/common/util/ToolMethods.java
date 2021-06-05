package com.geewaza.code.common.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;

/**
 *
 * @author WangHeng
 * @date 2016/11/7
 */
public class ToolMethods {

    /**
     * 将列表转换成逗号分隔的字符串 如果是字符串元素的 会加上引号
     * @param list
     * @param string
     * @return
     */
    public static String toArrayString(List<? extends  Object> list, boolean string) {
        return toArrayString(list, string, "\"");
    }

    /**
     * 将列表转换成逗号分隔的字符串 如果是字符串元素的 会加上引号
     * @param list
     * @param string
     * @return
     */
    public static String toArrayString(List<? extends  Object> list, boolean string, String quotation) {
        if (list == null || list.size() == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i != 0) {
                sb.append(",");
            }
            if (string) {
                sb.append(quotation).append(list.get(i)).append(quotation);
            } else {
                sb.append(list.get(i));
            }
        }
        return sb.toString();
    }

    /**
     * 将列表按照step指定的每段长度进行切分
     * @param list
     * @param step
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> cutList(List<T> list, int step) {
        if (list == null) {
            return null;
        }
        List<List<T>> resultList = new ArrayList<List<T>>();
        if (list.size() == 0) {
            return resultList;
        }
        if (list.size() <= step) {
            resultList.add(list);
            return resultList;
        }
        int fromIndex = 0;
        int toIndex = fromIndex + step;
        while (fromIndex < list.size()) {
            resultList.add(list.subList(fromIndex, toIndex));
            fromIndex = toIndex;
            toIndex = fromIndex + step;
            if (toIndex > list.size()) {
                toIndex = list.size();
            }
        }
        return resultList;
    }

    public static <T> List<List<T>> cutListBySize(List<T> list, int size) {
        if (list == null) {
            return null;
        }
        List<List<T>> resultList = new ArrayList<List<T>>();
        if (list.size() == 0) {
            return resultList;
        }
        if (size <= 1) {
            resultList.add(list);
            return resultList;
        }

        int step = list.size() / size;
        int plus = list.size() % size;
        if (plus != 0) {
            step++;
        }

        int fromIndex = 0;
        int toIndex = fromIndex + step;
        while (fromIndex < list.size()) {
            resultList.add(list.subList(fromIndex, toIndex));
            fromIndex = toIndex;
            toIndex = fromIndex + step;
            if (toIndex > list.size()) {
                toIndex = list.size();
            }
        }
        return resultList;
    }


    /**
     * 将错误堆栈转成String
     *
     * @param e
     * @return
     * @throws IOException
     */
    public static String getStackTrace(Throwable e) {
        if (e == null) {
            return null;
        }
        try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            PrintStream printStream = new PrintStream(byteArrayOutputStream);
            e.printStackTrace(printStream);
            return new String(byteArrayOutputStream.toByteArray());
        } catch (IOException ex) {
            return ex.getMessage();
        }
    }

    public static File getResourceFile(String pathName) throws FileNotFoundException {
        URL url = ToolMethods.class.getClassLoader().getResource(pathName);
        if (url == null) {
            throw new FileNotFoundException("Resource:" + pathName + " Not found!");
        }
        String path = url.getPath();
        return new File(path);
    }

    public static File getResourceDir(String pathName) throws FileNotFoundException {
        URL url = ToolMethods.class.getClassLoader().getResource(pathName);
        if (url == null) {
            throw new FileNotFoundException("Resource:" + pathName + " Not found!");
        }
        String path = url.getPath();
        return new File(path);
    }


    /**
     * 将字符串按照separator切分并返回切分后的的字符串数组
     *
     * @param value     待切分的字符串
     * @return          字符串数组
     */
    public static List<String> toStringList(String value) {
        return toStringList(value, ",");
    }

    /**
     * 将字符串按照separator切分并返回切分后的的字符串数组
     *
     * @param value     待切分的字符串
     * @param separator 分隔符
     * @return          字符串数组
     */
    public static List<String> toStringList(String value, String separator) {
        List<String> result = new ArrayList<>();
        String[] array = value.split(separator);
        for (String item : array) {
            if (StringUtils.isNotBlank(item)) {
                result.add(item.trim());
            }
        }
        return result;
    }

    /**
     * 创建目录
     * @param dirPath
     */
    public static void buildDirs(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 创建新文件，如果该文件不存在 就创建该文件，如果存在，则删除后再创建新文件，
     * 创建的过程中连带目录一起创建
     * @param filePathName
     * @return
     * @throws IOException
     */
    public static File buildNewFile(String filePathName) throws IOException {
        File result = new File(filePathName);
        if (!result.exists()) {
            File parent = result.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            result.createNewFile();
            return result;
        } else {
            result.delete();
            result.createNewFile();
            return result;
        }
    }

    /**
     * 获取文件名中的扩展名
     *
     * @param fileName
     * @return
     */
    public static String getFileNameSuffix(String fileName) {
        int lastIndex;
        if((lastIndex = fileName.lastIndexOf('.')) != -1) {
            return fileName.substring(lastIndex + 1);
        } else {
            return null;
        }
    }

    /**
     * 复制文件
     *
     * @param sourceFile
     * @param directFileName
     * @throws IOException
     */
    public static File copyFile(File sourceFile, String directFileName) throws IOException {
        File directFile = buildNewFile(directFileName);
        OutputStream outputStream = new FileOutputStream(directFile);
        try {
            Files.copy(sourceFile.toPath(), outputStream);
            return directFile;
        } finally {
            outputStream.close();
        }
    }


    /**
     * 复制文件
     *
     * @param sourceFile
     * @param orgNamePath
     * @throws IOException
     */
    public static File copyFileRandomName(File sourceFile, String orgNamePath) throws IOException {
        String newName = addRandomCodeIntoFileName(orgNamePath);
        return copyFile(sourceFile, newName);
    }

    public static String addRandomCodeIntoFileName(String orgNamePath) {
        String orgName = orgNamePath.substring(orgNamePath.lastIndexOf('/') + 1);
        String orgPath = orgNamePath.substring(0, orgNamePath.lastIndexOf('/') + 1);
        String randomCode = UUID.randomUUID().toString().substring(0, 8);
        return orgName.lastIndexOf('.') > -1?
            (orgPath + orgName.substring(0, orgName.lastIndexOf('.')) + "_" + randomCode + orgName.substring(orgName.lastIndexOf('.'))) :
            (orgPath + orgName + "_" + randomCode);
    }

    /**
     * 获取该文件的行数
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static Long getFileLineCount(File file) throws IOException {
        long fileLength = file.length();
        LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(file));
        try {
            lineNumberReader.skip(fileLength);
            long lineNumber = lineNumberReader.getLineNumber();
            lineNumber++;
            return lineNumber;
        } finally {
            lineNumberReader.close();
        }
    }

    public static File buildFileWithRandomName(String path, String name, String extension) throws IOException {
        String fullName = null;
        if (StringUtils.isBlank(extension)) {
            fullName = String.format("%s%s_%s", path, name, UUID.randomUUID().toString().substring(0, 8));
        } else {
            fullName = String.format("%s%s_%s.%s", path, name, UUID.randomUUID().toString().substring(0, 8), extension);
        }
        return buildNewFile(fullName);
    }

    /**
     * 解析出url参数中的键值对
     * 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
     *
     * @param url url地址
     * @return url请求参数部分
     */
    public static Map<String, String> getUrlRequest(String url, String charset) {
        Map<String, String> result = new HashMap<>();
        if (url.contains("?")) {
            String[] urlPart = url.split("\\?");
            List<NameValuePair> pairList = URLEncodedUtils.parse(urlPart[1], Charset.forName(charset));
            for (NameValuePair nameValuePair : pairList) {
                result.put(nameValuePair.getName(), nameValuePair.getValue());
            }
        }

        return result;
    }

    /**
     * 计算图片高宽比
     * @param height
     * @param width
     * @param scale 保留几位小数
     * @return
     */
    public static Double getImageAspectRatio(int height, int width, int scale) {
        BigDecimal bg = new BigDecimal((double)height / (double)width);
        return bg.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 将日期表达式按照时区转换成date
     * @param dateTime
     * @param format
     * @param timeZone
     * @return
     */
    public static Date parseDate(String dateTime, String format, TimeZone timeZone) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        simpleDateFormat.setTimeZone(timeZone);
        return simpleDateFormat.parse(dateTime);
    }

    /**
     * 判断是否是数字
     * @param str
     * @return
     */
    public static boolean isNumber(String str){
        String reg = "^[0-9]+(.[0-9]+)?$";
        return str.matches(reg);
    }

    /**
     * 处理文件中的每一行
     * @param file
     * @param function
     */
    public static void handleFileLine(File file, Function<String, ?> function) throws IOException {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                function.apply(line);
            }
        } finally {
            IOUtils.closeQuietly(bufferedReader);
        }
    }

    /**
     * 将inputstream 写入到文件中
     * @param inputStream
     * @param downloadFile
     */
    public static void writeStreamToFile(InputStream inputStream, File downloadFile) {
        Integer buffer = 1024;
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(downloadFile);
            byte[] b = new byte[1024];
            int i;
            while ((i = inputStream.read(b, 0, buffer)) != -1) {
                outputStream.write(b, 0, i);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to get file", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }

}
