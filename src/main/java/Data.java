import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jdk.internal.org.objectweb.asm.tree.analysis.Value;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Data {
    public static void main(String[] args) throws IOException, CsvValidationException {
        Data d = new Data("queaterlywages2000.csv");
        System.out.println(d.dataFre.size());
    }
    Vector<Vector<Integer>> dataFre;
    Vector<Vector<String>> dataValue;
    int All_size = 0;

    /**
     * txt读取频率直方图
     * @param filePath  路径
     * @param cls 从什么文件中读入（频率直方图）
     */
    public Data(String filePath, int cls) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        dataFre = new Vector<>();
        dataValue = new Vector<>();
        Vector<Integer> Fretmp = new Vector<>();
        Vector<String> Valuetmp = new Vector<>();
        while ((line = reader.readLine()) != null) {
            String[] record = line.split(",");
            Fretmp.add(Integer.parseInt(record[1]));
            Valuetmp.add(record[0]);
            All_size+=Integer.parseInt(record[1]);
        }
        dataFre.add(Fretmp);
        dataValue.add(Valuetmp);
    }

    public Data(String filePath) throws IOException, CsvValidationException {
        Reader reader = Files.newBufferedReader(Paths.get(filePath), Charset.forName("ISO-8859-1"));
        CSVReader csvReader = new CSVReader(reader);
        String[] record;
        dataFre = new Vector<>();
        dataValue = new Vector<>();
        record = csvReader.readNext();

        Vector<Map<String,Integer>> mapTmp = new Vector<>();
        for(int i = 0;i<record.length;i++){
            mapTmp.add(new HashMap<>());
        }
        while((record = csvReader.readNext()) != null){//将数据读入为直方图形式
            for(int i = 0;i< record.length;i++){
                if(mapTmp.get(i).get(record[i])==null){
                    mapTmp.get(i).put(record[i],1);
                }
                else{
                    mapTmp.get(i).put(record[i],mapTmp.get(i).get(record[i])+1);
                }
            }
            All_size++;
        }
        for(int i = 0;i<mapTmp.size();i++){
            Vector<Integer> Fretmp = new Vector<>();
            Vector<String> Valuetmp = new Vector<>();
            for(Map.Entry<String, Integer> entry : mapTmp.get(i).entrySet()){
                Fretmp.add(entry.getValue());
                Valuetmp.add(entry.getKey());
            }
            dataFre.add(Fretmp);
            dataValue.add(Valuetmp);
        }
        sortVectors();
    }

    public void sortVectors() {
        for(int m = 0;m<dataFre.size();m++){
            int n = dataFre.get(m).size();
            Integer[] indices = new Integer[n];

            // 初始化索引数组
            for (int i = 0; i < n; i++) {
                indices[i] = i;
            }

            // 按照 dataFre 的值排序索引数组
            int finalM = m;
            Arrays.sort(indices, (j, i) -> Integer.compare(dataFre.get(finalM).get(i), dataFre.get(finalM).get(j)));

            // 根据排序后的索引重新排列 dataFre 和 dataValue
            Vector<Integer> sortedFre = new Vector<>(n);
            Vector<String> sortedValue = new Vector<>(n);

            for (int i = 0; i < n; i++) {
                sortedFre.add(dataFre.get(m).get(indices[i]));
                sortedValue.add(dataValue.get(m).get(indices[i]));
            }

            // 更新原始 Vector
            dataFre.get(m).clear();
            dataValue.get(m).clear();
            dataFre.get(m).addAll(sortedFre);
            dataValue.get(m).addAll(sortedValue);
        }

    }
}
