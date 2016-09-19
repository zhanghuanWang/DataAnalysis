package cn.laolema.lottery;

import com.google.gson.Gson;

/**
 * Created by wzh on 16-8-29.
 */
public class SortFive {

    //期号
    private String issue;
    //中奖注数
    private String number;
    //万位
    private int myriabit;
    //千位
    private int kikobit;
    //百位
    private int hundreds;
    //十位
    private int decade;
    //个位
    private int unit;
    //销量
    private String sales;
    //
    private String description;
    //
    private String bonus;

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getMyriabit() {
        return myriabit;
    }

    public void setMyriabit(int myriabit) {
        this.myriabit = myriabit;
    }

    public int getKikobit() {
        return kikobit;
    }

    public void setKikobit(int kikobit) {
        this.kikobit = kikobit;
    }

    public int getHundreds() {
        return hundreds;
    }

    public void setHundreds(int hundreds) {
        this.hundreds = hundreds;
    }

    public int getDecade() {
        return decade;
    }

    public void setDecade(int decade) {
        this.decade = decade;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public void setValue(int i, String s) {
        int value = Integer.valueOf(s);
        switch (i) {
            case 0:
                myriabit = value;
                break;
            case 1:
                kikobit = value;
                break;
            case 2:
                hundreds = value;
                break;
            case 3:
                decade = value;
                break;
            case 4:
                unit = value;
                break;
        }
    }

    public int getNumInt() {
        return myriabit * 10000 + kikobit * 1000 + hundreds * 100 + decade * 10 + unit;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
