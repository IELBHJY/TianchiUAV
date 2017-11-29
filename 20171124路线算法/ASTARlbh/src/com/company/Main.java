package com.company;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    public static int[] xCity=new int[10];
    public static int[] yCity=new int[10];
    public static void main(String[] args) {
	// write your code here
        Set<Point> barrier = new HashSet<Point>();
        for(int i=0;i<548;i++){
            barrier.add(new Point(i,-1));
        }
        for(int i=0;i<421;i++)
        {
            barrier.add(new Point(-1,i));
        }
        for(int i=0;i<548;i++){
            barrier.add(new Point(i,421));
        }
        for(int i=0;i<421;i++){
            barrier.add(new Point(548,i));
        }
        barrier.add(new Point(-1,-1));
        barrier.add(new Point(-1,421));
        barrier.add(new Point(548,421));
        barrier.add(new Point(548,-1));
        readCity();
        long start = System.currentTimeMillis();
        //Point endPoint;
        for (int i = 8; i < 9; i++) {
            Point endPoint;
            List<Point> set =new ArrayList<>();
            AStar aStar=new AStar();
            aStar.move(141, 327, xCity[i]-1, yCity[i]-1,barrier);
            endPoint = aStar.getEndPoint();
            set = get(endPoint, set);
            /*for( int j=set.size()-1;j>=0;j--) {
                System.out.println(set.get(j).getKey());
            }*/
            System.out.println("飞行时间："+(set.size()-1)*2+"分钟");
            writeData(set,i+1,10);
        }
        long end = System.currentTimeMillis();
        System.out.println("求解用时"+(end - start));
    }
    public static List<Point> get(Point p, List<Point> set) {
        if (p != null) {
            set.add(p);
        }
        Point pp = p.prev;
        if (pp != null) {
            get(pp, set);
        } else {
            return set;
        }
        return set;
    }
    private  static void readCity(){
        try {
            int m = 0;
            while (m < 12){
                ArrayList<String[]> csvFileList = new ArrayList<String[]>();
                String csvFilePath = "/Users/apple/Documents/data/CityData.csv";
                CsvReader reader = new CsvReader(csvFilePath, ',', Charset.forName("UTF-8"));
                while (reader.readRecord()){
                    csvFileList.add(reader.getValues());
                }
                reader.close();
                for (int i = 0; i<10; i++) {
                    String[] strData = csvFileList.get(i+2);
                    xCity[i]=Integer.parseInt(strData[1]);
                    yCity[i]=Integer.parseInt(strData[2]);
                }
                m++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void writeData(List<Point> set,int cityNumber,int Data){
        try {
                String csvFilePath = "/Users/apple/Desktop/"+String.valueOf(Data)+"/"+ String.valueOf(cityNumber)+ ".csv" ;
                // 创建CSV写对象 例如:CsvWriter(文件路径，分隔符，编码格式);
                CsvWriter csvWriter = new CsvWriter(csvFilePath, ',', Charset.forName("UTF-8"));
                SimpleDateFormat df=new SimpleDateFormat("HH:mm");
                Calendar calendar=new GregorianCalendar(2017,10,23,2,58,0);
                for(int i=0;i<set.size();i++){
                    String[] csvContent = new String[5];
                    csvContent[0]=String.valueOf(cityNumber);
                    csvContent[1]=String.valueOf(Data);
                    calendar.add(Calendar.MINUTE,2);
                    Date date = calendar.getTime();
                    String str=df.format(date);
                    String[] temp=str.split(":");
                    if(temp[0].charAt(0)=='0'){
                        temp[0]=String.valueOf(temp[0].charAt(1));
                    }
                    csvContent[2]=temp[0]+":"+temp[1];
                    String[] ordination=set.get(set.size()-1-i).getKey().split(",");
                    csvContent[3]=String.valueOf(Integer.parseInt(ordination[0])+1);
                    csvContent[4]=String.valueOf(Integer.parseInt(ordination[1])+1);
                    csvWriter.writeRecord(csvContent);
                }
                csvWriter.close();
                System.out.println("------csv文件已经写入---------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
