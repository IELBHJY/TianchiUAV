package com.company;

import com.csvreader.CsvReader;

import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by DELL on 2017/11/22.
 */
public class AStar {
    public static final int MOVE_TETN = 2;
    public static final int LENGHT = 2;
    int totalRow = 548;
    int totalCom = 421;
    int label=0;
    Double[][][] weather = new Double[18][totalRow][totalCom];
    /* 打开的列表 */
    Map<String, Point> openMap = new HashMap<String, Point>();
    /* 关闭的列表 */
    Map<String, Point> closeMap = new HashMap<String, Point>();
    /* 障碍物 */
    Set<Point> barrier;
    /* 起点 */
    public Point startPoint;
    /* 终点 */
    public Point endPoint;
    /* 当前使用节点 */
    Point currentPoint;
    /* 循环次数，为了防止目标不可到达 */
    int num = 0;
    public void move(int x1, int y1, int x2, int y2,Set<Point> barrier1) {
        num = 0;
        startPoint = new Point(x1, y1);
        endPoint=new Point(x2,y2);
        closeMap.put(startPoint.getKey(), startPoint);
        currentPoint = startPoint;
        barrier=barrier1;
        readData();
        Point p=currentPoint;
        toOpen(currentPoint.x,currentPoint.y);
        while(num<=1000000) {
            p = toClose(currentPoint.x,currentPoint.y);
            if (!p.equals(endPoint)) {
                toOpen(p.x, p.y);
            }
            else{
                endPoint=p;
                break;
            }
            num++;
        }
        if(p.equals(endPoint)){
            System.out.println("找到路径！");
        }
        else{//没有找到路径，则在已经找过的点中寻找距离目标点最近的点输出。
            System.out.println("未找到路径，找距离终点理论最近点");
            if(closeMap!=null){
                List<Point> list = new ArrayList<Point>(closeMap.values());
                Collections.sort(list, new Comparator<Point>() {
                    @Override
                    public int compare(Point o1, Point o2) {
                        if (o1.hEstimate > o2.hEstimate) {
                            return 1;
                        } else if (o1.hEstimate < o2.hEstimate) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                });
                if(list.size()>=2){
                endPoint=list.get(1);
                }else{
                    System.out.println("list只有一个元素");
                }
            }
            else{
                System.out.println("closeMap为空，请检查。");
            }
        }
    }
    private void readData()
    {
        try {
            int m = 0;
            while (m < 18){
                ArrayList<String[]> csvFileList = new ArrayList<String[]>();
                String csvFilePath = "/Users/apple/Desktop/TestMazeAvg15/Testmaze_date10_hour"+ String.valueOf(m + 3) + ".csv";
                CsvReader reader = new CsvReader(csvFilePath, ',', Charset.forName("UTF-8"));
                while (reader.readRecord()){
                    csvFileList.add(reader.getValues());
                }
                reader.close();
                for (int i = 0; i<totalRow; i++) {
                    String[] strData = csvFileList.get(i);
                    for (int j = 0; j< totalCom; j++){
                        double tmp =Double.valueOf(strData[j]);
                        weather[m][i][j] = tmp;
                    }
                }
                m++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private int getGuessLength(int x1, int y1, int x2, int y2) {
        //return ((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 -y1))* AStar.LENGHT;
        return (Math.abs(x1 - x2) + Math.abs(y1 - y2)) * AStar.LENGHT;
        // return Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2)) * AStar.LENGHT;
        // return 0;
    }

    private int getMoveLength(Point p1)
    {
        int result=0;
        Point p=p1;
        if(p.prev==null)
        {
            return result;
        }else{
            result+=1;
        }
        //System.out.println(startPoint.getKey()+"  "+p.getKey()+"  "+p.prev.getKey());
        while (!p.prev.equals(startPoint))
        {
            result+=1;
            p=p.prev;
        }
        return result;
    }
    private void toOpen(int x, int y) {
        /*int moveLength=getMoveLength(currentPoint);
        int hour=moveLength/30;
        int minute=(moveLength%30)*2;
        int count=0;
        if(hour<=11) {
            if (minute + 2 < 60) {
                if (x <= 548 && y <= 420 && x >= 1 && y >= 0) {
                    if (weather[hour][x - 1][y] == 1) {
                        addOpenPoint(new Point(x - 1, y), AStar.MOVE_TETN);
                        count++;
                    }
                }
                if (x <= 546 & y <= 420 && x >= 0 && y >= 0) {
                    if (weather[hour][x + 1][y] == 1) {
                        addOpenPoint(new Point(x + 1, y), AStar.MOVE_TETN);
                        count++;
                    }
                }
                if (x <= 547 && y <= 421 && x >= 0 && y >= 1) {
                    if (weather[hour][x][y - 1] == 1) {
                        addOpenPoint(new Point(x, y - 1), AStar.MOVE_TETN);
                        count++;
                    }
                }
                if (y <= 419 && x <= 547 && x >= 0 && y >= 0) {
                    if (weather[hour][x][y + 1] == 1) {
                        addOpenPoint(new Point(x, y + 1), AStar.MOVE_TETN);
                        count++;
                    }
                }
            } else {

                if (x >= 1 && x <= 548 && y >= 0 && y <= 420) {
                    if (weather[hour + 1][x - 1][y] == 1) {
                        addOpenPoint(new Point(x - 1, y), AStar.MOVE_TETN);
                        count++;
                    }
                }
                if (x >= 0 && x <= 546 && y >= 0 && y <= 420) {
                    if (weather[hour + 1][x + 1][y] == 1) {
                        addOpenPoint(new Point(x + 1, y), AStar.MOVE_TETN);
                        count++;
                    }
                }
                if (x >= 0 && x <= 547 && y >= 1 && y <= 420) {
                    if (weather[hour + 1][x][y - 1] == 1) {
                        addOpenPoint(new Point(x, y - 1), AStar.MOVE_TETN);
                        count++;
                    }
                }
                if (x >= 0 && x <= 547 && y >= 0 && y <= 419) {
                    if (weather[hour + 1][x][y + 1] == 1) {
                        addOpenPoint(new Point(x, y + 1), AStar.MOVE_TETN);
                        count++;
                    }
                }
            }
        }*/
        addOpenPoint(new Point(x - 1, y), AStar.MOVE_TETN);
        addOpenPoint(new Point(x + 1, y), AStar.MOVE_TETN);
        addOpenPoint(new Point(x, y - 1), AStar.MOVE_TETN);
        addOpenPoint(new Point(x, y + 1), AStar.MOVE_TETN);
        num++;
    }

    private void addOpenPoint(Point point, int gCost) {
        if (point.x < 0 || point.y < 0) {
            return;
        }
        String key = point.getKey();
        //如果待搜索的点不包含在barrier内，并且该点距离起始点飞行时间小于12小时，并且满足天气情况
        if(!barrier.contains(point)) {
            int moveLength=getMoveLength(currentPoint);
            int hour=moveLength/30;
            int minute=(moveLength%30)*2;
            if(minute+2<60){
                if(hour<=17){
                    if(weather[hour][point.x][point.y]==0){
                        return;
                    }
                }else{
                    return;
                }
            }else{
                if(hour<=16){
                    if(weather[hour+1][point.x][point.y]==0){
                        return;
                    }
                }else{
                    return;
                }
            }
            int hEstimate = getGuessLength(point.x, point.y, endPoint.x, endPoint.y);
            int totalGCost = currentPoint.gCost + gCost;
            int fTotal = totalGCost + hEstimate;//计算待加入点的信息
            if (!closeMap.containsKey(key)) {
                point.hEstimate = hEstimate;
                point.gCost = totalGCost;
                point.fTotal = fTotal;
                Point oldPoint = openMap.get(key);
                if (oldPoint != null) {
                    if (oldPoint.gCost > totalGCost) {
                        oldPoint.fTotal = fTotal;
                        oldPoint.prev = currentPoint;
                        openMap.put(key, oldPoint);
                    }
                } else {
                    point.prev = currentPoint;
                    openMap.put(key, point);
                }
            }/*
            else {
                Point oldPoint = closeMap.get(key);
                if (oldPoint != null) {
                    if ((oldPoint.gCost + gCost) < this.currentPoint.gCost) {
                        if (this.currentPoint.prev != oldPoint) {
                            this.currentPoint.fTotal = oldPoint.fTotal + gCost;
                            this.currentPoint.gCost = oldPoint.gCost + gCost;
                            this.currentPoint.prev = oldPoint;
                        }
                    }
                }
            }*/
        }
    }

    private Point toClose(int x, int y) {
        List<Point> list = new ArrayList<Point>(openMap.values());
        Collections.sort(list, new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                if (o1.fTotal > o2.fTotal) {
                    return 1;
                } else if (o1.fTotal < o2.fTotal) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        if (list.size() > 0) {
            currentPoint = list.get(0);
            closeMap.put(currentPoint.getKey(), currentPoint);
            openMap.remove(currentPoint.getKey());
        }
        return currentPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public Point getStartPoint()
    {
        return startPoint;
    }
}
