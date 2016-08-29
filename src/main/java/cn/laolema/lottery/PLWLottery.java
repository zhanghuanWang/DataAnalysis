package cn.laolema.lottery;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wzh on 16-8-29.
 */
public class PLWLottery {

    public static String URL_BASE = "http://kaijiang.500.com/shtml/plw/";

    public static int startIssue = 4001;
    public static int endIssue = 16366;

    public static List<SortFive> list = new ArrayList<SortFive>();

    public static void main(String args[]) {


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
            System.out.println("sortFive:" + sortFive );
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
}
