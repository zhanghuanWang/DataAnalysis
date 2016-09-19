package cn.laolema.lottery;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by wzh on 16-8-29.
 */
public class PLWLottery {

    public static String URL_BASE = "http://kaijiang.500.com/shtml/plw/";

    public static int startIssue = 4001;
    public static int endIssue = 16256;

    public static List<SortFive> list = new ArrayList<SortFive>();
    public static Map<Integer, SortFive> map = new HashMap<Integer, SortFive>();
    public static Map<Integer, Integer> mapRest = new HashMap<Integer, Integer>();
    public static Map<Integer, Integer> mapW = new HashMap<Integer, Integer>();
    public static Map<Integer, Integer> mapK = new HashMap<Integer, Integer>();
    public static Map<Integer, Integer> mapB = new HashMap<Integer, Integer>();
    public static Map<Integer, Integer> mapS = new HashMap<Integer, Integer>();
    public static Map<Integer, Integer> mapG = new HashMap<Integer, Integer>();

    public static void main(String args[]) {
//        gapDate();
        loadPlw();

        getNone();
        createExcel("/home/wzh/lottery_plw.xls");
    }


    public static void createExcel(String outputFile) {
        try {
            // 创建新的Excel 工作簿
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 在Excel工作簿中建一工作表，其名为缺省值
            // 如要新建一名为"效益指标"的工作表，其语句为：
            // HSSFSheet sheet = workbook.createSheet("效益指标");
            HSSFSheet sheet = workbook.createSheet("排列五");
            HSSFRow row = sheet.createRow((short) 0);
            HSSFCell hssfCell = row.createCell((short) 0);
            hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            hssfCell.setCellValue("期号");

            hssfCell = row.createCell((short) 1);
            hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            hssfCell.setCellValue("万");
            hssfCell = row.createCell((short) 2);
            hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            hssfCell.setCellValue("千");
            hssfCell = row.createCell((short) 3);
            hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            hssfCell.setCellValue("百");
            hssfCell = row.createCell((short) 4);
            hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            hssfCell.setCellValue("十");
            hssfCell = row.createCell((short) 5);
            hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            hssfCell.setCellValue("个");
            hssfCell = row.createCell((short) 6);
            hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            hssfCell.setCellValue("号码1");
            hssfCell = row.createCell((short) 7);
            hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            hssfCell.setCellValue("号码2");
            hssfCell = row.createCell((short) 8);
            hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            hssfCell.setCellValue("中奖注数");

            for (int i = 0; i < list.size(); i++) {
                SortFive sf = list.get(i);
                row = sheet.createRow((short) (i + 1));

                hssfCell = row.createCell((short) 0);
                hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
                hssfCell.setCellValue(sf.getIssue());

                hssfCell = row.createCell((short) 1);
                hssfCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                hssfCell.setCellValue(sf.getMyriabit() + "");
                hssfCell = row.createCell((short) 2);
                hssfCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                hssfCell.setCellValue(sf.getKikobit() + "");
                hssfCell = row.createCell((short) 3);
                hssfCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                hssfCell.setCellValue(sf.getHundreds() + "");
                hssfCell = row.createCell((short) 4);
                hssfCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                hssfCell.setCellValue(sf.getDecade() + "");
                hssfCell = row.createCell((short) 5);
                hssfCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                hssfCell.setCellValue(sf.getUnit() + "");
                hssfCell = row.createCell((short) 6);
                hssfCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                hssfCell.setCellValue(sf.getNumInt() + "");
                hssfCell = row.createCell((short) 7);
                hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
                hssfCell.setCellValue(String.format("%05d", sf.getNumInt()));
                hssfCell = row.createCell((short) 8);
                hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
                hssfCell.setCellValue(sf.getNumber() + "");
            }

            sheet = workbook.createSheet("频率表0");
            row = sheet.createRow((short) 0);
            hssfCell = row.createCell((short) 0);
            hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            hssfCell.setCellValue("数字");
            hssfCell = row.createCell((short) 1);
            hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            hssfCell.setCellValue("频率");

            for (int i = 0; i < 20000; i++) {
                Integer f = mapRest.get(i);

                row = sheet.createRow((short) (i + 1));

                hssfCell = row.createCell((short) 0);
                hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
                hssfCell.setCellValue(String.format("%05d", i));

                hssfCell = row.createCell((short) 1);
                hssfCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                hssfCell.setCellValue(f + "");

            }
            sheet = workbook.createSheet("频率表1");
            row = sheet.createRow((short) 0);
            hssfCell = row.createCell((short) 0);
            hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            hssfCell.setCellValue("数字");
            hssfCell = row.createCell((short) 1);
            hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            hssfCell.setCellValue("频率");

            for (int i = 20000; i < 40000; i++) {
                Integer f = mapRest.get(i);

                row = sheet.createRow((short) (i + 1 - 20000));

                hssfCell = row.createCell((short) 0);
                hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
                hssfCell.setCellValue(String.format("%05d", i));

                hssfCell = row.createCell((short) 1);
                hssfCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                hssfCell.setCellValue(f + "");

            }
            sheet = workbook.createSheet("频率表2");
            row = sheet.createRow((short) 0);
            hssfCell = row.createCell((short) 0);
            hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            hssfCell.setCellValue("数字");
            hssfCell = row.createCell((short) 1);
            hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            hssfCell.setCellValue("频率");

            for (int i = 40000; i < 60000; i++) {
                Integer f = mapRest.get(i);

                row = sheet.createRow((short) (i + 1 - 40000));

                hssfCell = row.createCell((short) 0);
                hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
                hssfCell.setCellValue(String.format("%05d", i));

                hssfCell = row.createCell((short) 1);
                hssfCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                hssfCell.setCellValue(f + "");

            }
            sheet = workbook.createSheet("频率表3");
            row = sheet.createRow((short) 0);
            hssfCell = row.createCell((short) 0);
            hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            hssfCell.setCellValue("数字");
            hssfCell = row.createCell((short) 1);
            hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            hssfCell.setCellValue("频率");

            for (int i = 60000; i < 80000; i++) {
                Integer f = mapRest.get(i);

                row = sheet.createRow((short) (i + 1 - 60000));

                hssfCell = row.createCell((short) 0);
                hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
                hssfCell.setCellValue(String.format("%05d", i));

                hssfCell = row.createCell((short) 1);
                hssfCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                hssfCell.setCellValue(f + "");

            }
            sheet = workbook.createSheet("频率表4");
            row = sheet.createRow((short) 0);
            hssfCell = row.createCell((short) 0);
            hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            hssfCell.setCellValue("数字");
            hssfCell = row.createCell((short) 1);
            hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            hssfCell.setCellValue("频率");

            for (int i = 80000; i < 100000; i++) {
                Integer f = mapRest.get(i);

                row = sheet.createRow((short) (i + 1 - 80000));

                hssfCell = row.createCell((short) 0);
                hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
                hssfCell.setCellValue(String.format("%05d", i));

                hssfCell = row.createCell((short) 1);
                hssfCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                hssfCell.setCellValue(f + "");

            }

            sheet = workbook.createSheet("单个频率表");
            row = sheet.createRow((short) 0);
            hssfCell = row.createCell((short) 0);
            hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            hssfCell.setCellValue("数字");
            hssfCell = row.createCell((short) 1);
            hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            hssfCell.setCellValue("万位");
            hssfCell = row.createCell((short) 2);
            hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            hssfCell.setCellValue("千位");
            hssfCell = row.createCell((short) 3);
            hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            hssfCell.setCellValue("百位");
            hssfCell = row.createCell((short) 4);
            hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            hssfCell.setCellValue("十位");
            hssfCell = row.createCell((short) 5);
            hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            hssfCell.setCellValue("个位");

            for (int i = 0; i < 10; i++) {
                Integer w = mapW.get(i);
                Integer k = mapK.get(i);
                Integer b = mapB.get(i);
                Integer s = mapS.get(i);
                Integer g = mapG.get(i);
                row = sheet.createRow((short) (i + 1));

                hssfCell = row.createCell((short) 0);
                hssfCell.setCellType(HSSFCell.CELL_TYPE_STRING);
                hssfCell.setCellValue(i + "");

                hssfCell = row.createCell((short) 1);
                hssfCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                hssfCell.setCellValue(w + "");
                hssfCell = row.createCell((short) 2);
                hssfCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                hssfCell.setCellValue(k + "");
                hssfCell = row.createCell((short) 3);
                hssfCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                hssfCell.setCellValue(b + "");
                hssfCell = row.createCell((short) 4);
                hssfCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                hssfCell.setCellValue(s + "");
                hssfCell = row.createCell((short) 5);
                hssfCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                hssfCell.setCellValue(g + "");
            }

            // 新建一输出文件流
            FileOutputStream fOut = new FileOutputStream(outputFile);
            // 把相应的Excel 工作簿存盘
            workbook.write(fOut);
            fOut.flush();
            // 操作结束，关闭文件
            fOut.close();
            System.out.println("文件生成...");
        } catch (Exception e) {
            System.out.println("已运行 xlCreate() : " + e);
        }
    }

    private static HSSFCell creatSheetData(HSSFSheet sheet, int rowIndex, int cellIndex) {
        // 在索引0的位置创建行（最顶端的行）
        HSSFRow row = sheet.createRow((short) rowIndex);
        //在索引0的位置创建单元格（左上端）
        HSSFCell cell = row.createCell((short) cellIndex);

        return cell;
    }


    public static void getNone() {
        //0~99999
        for (int i = 0; i < 100000; i++) {
            mapRest.put(i, 0);
        }
        for (int i = 0; i < 10; i++) {
            mapW.put(i, 0);
        }
        for (int i = 0; i < 10; i++) {
            mapK.put(i, 0);
        }
        for (int i = 0; i < 10; i++) {
            mapB.put(i, 0);
        }
        for (int i = 0; i < 10; i++) {
            mapS.put(i, 0);
        }
        for (int i = 0; i < 10; i++) {
            mapG.put(i, 0);
        }
        for (int i = 0; i < list.size(); i++) {
            SortFive sortFive = list.get(i);
            int numInt = sortFive.getNumInt();
            Integer integer = mapRest.get(numInt);
            mapRest.put(numInt, ++integer);


            int w = sortFive.getMyriabit();
            int k = sortFive.getKikobit();
            int b = sortFive.getHundreds();
            int s = sortFive.getDecade();
            int g = sortFive.getUnit();

            Integer i1 = mapW.get(w);
            mapW.put(w, ++i1);

            Integer i2 = mapK.get(k);
            mapK.put(k, ++i2);

            Integer i3 = mapB.get(b);
            mapB.put(b, ++i3);

            Integer i4 = mapS.get(s);
            mapS.put(s, ++i4);

            Integer i5 = mapG.get(g);
            mapG.put(g, ++i5);


        }


        Iterator<Map.Entry<Integer, Integer>> entries = mapW.entrySet().iterator();

        while (entries.hasNext()) {

            Map.Entry<Integer, Integer> entry = entries.next();
            Integer key = entry.getKey();

            Integer integer = entry.getValue();
            if (integer != null) {
                System.out.println(key + ":" + integer);
            }

        }
//        Iterator<Map.Entry<Integer, SortFive>> entries = map.entrySet().iterator();
//
//        while (entries.hasNext()) {
//
//            Map.Entry<Integer, SortFive> entry = entries.next();
//            Integer key = entry.getKey();
//
//            Integer integer = mapRest.get(key);
//            if (integer != null) {
//
//            }
//
//        }
    }

    private static void gapDate() {
        for (int i = startIssue; i < endIssue; i++) {
            connectHtml(getUrl(i));
            if (i % 1000 > 366) {
                i = (i / 1000 + 1) * 1000 + 1;
            }
        }

        save(new Gson().toJson(list));
    }

    public static void connectHtml(String url) {
        try {
            Document doc = Jsoup.connect(url).get();

            Elements divValue = doc.select("div.ball_box01>ul>li");
            Elements spanDescription = doc.select("span.span_right");

            Elements spanSales = doc.select("span.cfont1 ");
            System.out.println("spanSales:" + spanSales.text() + "");
            Elements spanTable = doc.select("table.kj_tablelist02");

            Elements fontIssue = doc.select("font.cfont2");
            String issue = fontIssue.select("strong").text();
            Element e = spanTable.get(1);
            Elements trs = e.select("tr");
            Elements tds = trs.get(2).select("td");
            String number = tds.get(1).text();
            String bonus = tds.get(2).text();

//            System.out.println("number:" + number + "");
//            System.out.println("bonus:" + bonus + "");
//            System.out.println("issue:" + issue + "");


            SortFive sortFive = new SortFive();
            sortFive.setDescription(spanDescription.text());
            sortFive.setNumber(number);
            sortFive.setBonus(bonus);
            sortFive.setIssue(issue);
            for (int i = 0; i < divValue.size(); i++) {
                Element element = divValue.get(i);
                String data = element.text();
                sortFive.setValue(i, data);
            }
            System.out.println("sortFive:" + sortFive);
            list.add(0, sortFive);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getUrl(int issue) {
//        System.out.println("issue:" + issue + "--" + String.format("%05d", issue));
        return URL_BASE + String.format("%05d", issue) + ".shtml";
    }

    public static void save(String content) {
        try {
            FileUtils.write(new File("/home/wzh/lottery_plw.json"), content, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadPlw() {
        try {
            String s = FileUtils.readFileToString(new File("/home/wzh/lottery_plw.json"), "UTF-8");
            if (s != null) {
                list = new Gson().fromJson(s, new TypeToken<List<SortFive>>() {
                }.getType());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
