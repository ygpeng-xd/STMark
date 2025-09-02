import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;


import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Data {
    Vector<Vector<Integer>> dataFre;
    Vector<Vector<String>> dataValue;
    int All_size = 0;

    /**
     * read frequency data
     * @param filePath  path
     * @param cls frequency form
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


    /**
     * read data and organize it into frequency form
     * @param filePath  path
     */
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

    /**
     * Sort by frequency
     */
    public void sortVectors() {
        for(int m = 0;m<dataFre.size();m++){
            int n = dataFre.get(m).size();
            Integer[] indices = new Integer[n];

            for (int i = 0; i < n; i++) {
                indices[i] = i;
            }

            int finalM = m;
            Arrays.sort(indices, (j, i) -> Integer.compare(dataFre.get(finalM).get(i), dataFre.get(finalM).get(j)));

            Vector<Integer> sortedFre = new Vector<>(n);
            Vector<String> sortedValue = new Vector<>(n);

            for (int i = 0; i < n; i++) {
                sortedFre.add(dataFre.get(m).get(indices[i]));
                sortedValue.add(dataValue.get(m).get(indices[i]));
            }

            // Update the original Vector
            dataFre.get(m).clear();
            dataValue.get(m).clear();
            dataFre.get(m).addAll(sortedFre);
            dataValue.get(m).addAll(sortedValue);
        }

    }
}
