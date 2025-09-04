import com.opencsv.exceptions.CsvValidationException;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class Watermark {
    int sumLessMedian = 0;
    int all_res = 0;
    int cou1 = 0;
    int cou0 = 0;
    @Test
    public void embeddingWatermarkforEyewndrPerbatationAttack() throws IOException, NoSuchAlgorithmException, CsvValidationException, SQLException {

        int[] perbs = {1,3,5,7,15,31,63,127,255,511,1023};//perturbation magnitude in perturbation attack
        for(int ratio:perbs){
            String file  = "dataset\\eyewndr.txt";
            int wLen = 40;
            for(int c = 0;c<10;c++) {
                Data d = new Data(file, 1);
                String watermark = generateWatermark(wLen);
                Vector<Parameters> parameters = new Vector<>();

                // numerator
                double numeratorInner = 0.5 * (1.0 - Math.sqrt(9.5481 / (wLen + 9.5481)));
                double numerator = Math.log(numeratorInner);

                // denominator
                double logBase = 1.0 - 1.0 / wLen;
                double denominator = Math.log(logBase);

                String Key1 = "xidian";
                String Key2 = "西电";

                {//for eyewndr dataset
                    parameters.add(new Parameters(Key1, (int) (d.dataValue.get(0).size() / (numerator / denominator)), 100));
                }

                //data partition
                Vector<Vector<Vector<Integer>>> partitions = getAllPartitions(parameters,d,Key2);

                int totalGroups = 0;
                for(int i = 0;i<parameters.size();i++){
                    totalGroups+=parameters.get(i).getM();
                }

                Vector<StringBuffer> Code = getAllVeri(totalGroups,partitions,parameters,d,watermark);

                attack.perbAttack(d,ratio);//frequency perturbation attack

                String result = decodeWatermark(d,Key2,parameters,Code,watermark.length());
                if(result.equals(watermark)){
                    System.out.println("success");
                }

                int count = 0;
                for(int i = 0;i<40;i++){
                    if(result.charAt(i) == watermark.charAt(i)){
                        count++;
                    }
                }

                all_res+=count;
                System.out.println(count);
            }
            System.out.println("attack ratio" + ratio + "WMR="+all_res/(wLen*10.0));
            all_res = 0;

        }
    }

    @Test
    public void embeddingWatermarkforEyewndrScalingAttack() throws IOException, NoSuchAlgorithmException, CsvValidationException, SQLException {
        double[] ratios = new double[]{0.2,0.4,0.6,0.8,1.0,1.2,1.4,1.6,1.8,2.0};
        for(double ratio : ratios){
            String file  = "dataset\\eyewndr.txt";
            int wLen = 40;
            for(int c = 0;c<10;c++) {
                Data d = new Data(file, 1);
                String watermark = generateWatermark(wLen);
                Vector<Parameters> parameters = new Vector<>();

                // numerator
                double numeratorInner = 0.5 * (1.0 - Math.sqrt(9.5481 / (wLen + 9.5481)));
                double numerator = Math.log(numeratorInner);

                // denominator
                double logBase = 1.0 - 1.0 / wLen;
                double denominator = Math.log(logBase);

                String Key1 = "xidian";
                String Key2 = "西电";

                {//for eyewndr dataset
                    parameters.add(new Parameters(Key1, (int) (d.dataValue.get(0).size() / (numerator / denominator)), 100));
                }

                //data partition
                Vector<Vector<Vector<Integer>>> partitions = getAllPartitions(parameters,d,Key2);

                int totalGroups = 0;
                for(int i = 0;i<parameters.size();i++){
                    totalGroups+=parameters.get(i).getM();
                }

                Vector<StringBuffer> Code = getAllVeri(totalGroups,partitions,parameters,d,watermark);

                attack.scalingAttack(d,ratio);//scaling attack

                String result = decodeWatermark(d,Key2,parameters,Code,watermark.length());
                if(result.equals(watermark)){
                    System.out.println("success");
                }

                int count = 0;
                for(int i = 0;i<40;i++){
                    if(result.charAt(i) == watermark.charAt(i)){
                        count++;
                    }
                }

                all_res+=count;
                System.out.println(count);
            }
            System.out.println("attack ratio" + ratio + "WMR="+all_res/(wLen*10.0));
            all_res = 0;

        }
    }

    public void embeddingWatermarkforEyewndrDeletionAttack() throws IOException, NoSuchAlgorithmException, CsvValidationException, SQLException {
        double[] ratios = new double[]{0.1,0.2,0.3,0.4,0.5, 0.6, 0.7, 0.8, 0.9, 0.95, 0.98, 0.99,0.99999};
        for(double ratio : ratios){
            String fileName = "eyewndr.txt";
            String file  = "dataset\\eyewndr.txt";
            int wLen = 40;
            for(int c = 0;c<10;c++) {
                Data d = new Data(file, 1);
                String watermark = generateWatermark(wLen);
                Vector<Parameters> parameters = new Vector<>();

                // numerator
                double numeratorInner = 0.5 * (1.0 - Math.sqrt(9.5481 / (wLen + 9.5481)));
                double numerator = Math.log(numeratorInner);

                // denominator
                double logBase = 1.0 - 1.0 / wLen;
                double denominator = Math.log(logBase);

                String Key1 = "xidian";
                String Key2 = "西电";

                {//for eyewndr dataset
                    parameters.add(new Parameters(Key1, (int) (d.dataValue.get(0).size() / (numerator / denominator)), 100));
                }

                //data partition
                Vector<Vector<Vector<Integer>>> partitions = getAllPartitions(parameters,d,Key2);

                int totalGroups = 0;
                for(int i = 0;i<parameters.size();i++){
                    totalGroups+=parameters.get(i).getM();
                }

                Vector<StringBuffer> Code = getAllVeri(totalGroups,partitions,parameters,d,watermark);

                {
                    attack.deleteAttackwithOutSQL(fileName,d,ratio);
                    d = new Data("attacked dataset\\attack"+fileName);
                }

                String result = decodeWatermark(d,Key2,parameters,Code,watermark.length());
                if(result.equals(watermark)){
                    System.out.println("success");
                }



                int count = 0;
                for(int i = 0;i<40;i++){
                    if(result.charAt(i) == watermark.charAt(i)){
                        count++;
                    }
                }

                all_res+=count;
                System.out.println(count);
            }
            System.out.println("attack ratio" + ratio + "WMR="+all_res/(wLen*10.0));
            all_res = 0;

        }
    }

    public void embeddingWatermarkforEyewndrValueReplacement() throws IOException, NoSuchAlgorithmException, CsvValidationException, SQLException {
        double[] ratios = new double[]{0.1,0.2,0.3,0.4,0.5, 0.6, 0.7, 0.8, 0.9, 0.95, 0.98, 0.99,0.99999};
        for(double ratio : ratios){
            String fileName = "eyewndr.txt";
            String file  = "dataset\\eyewndr.txt";
            int wLen = 40;
            for(int c = 0;c<10;c++) {
                Data d = new Data(file, 1);
                String watermark = generateWatermark(wLen);
                Vector<Parameters> parameters = new Vector<>();

                // numerator
                double numeratorInner = 0.5 * (1.0 - Math.sqrt(9.5481 / (wLen + 9.5481)));
                double numerator = Math.log(numeratorInner);

                // denominator
                double logBase = 1.0 - 1.0 / wLen;
                double denominator = Math.log(logBase);

                String Key1 = "xidian";
                String Key2 = "西电";

                {//for eyewndr dataset
                    parameters.add(new Parameters(Key1, (int) (d.dataValue.get(0).size() / (numerator / denominator)), 100));
                }

                //data partition
                Vector<Vector<Vector<Integer>>> partitions = getAllPartitions(parameters,d,Key2);

                int totalGroups = 0;
                for(int i = 0;i<parameters.size();i++){
                    totalGroups+=parameters.get(i).getM();
                }

                Vector<StringBuffer> Code = getAllVeri(totalGroups,partitions,parameters,d,watermark);

                {
                    d= attack.alterRandomAttackwithOutSQL(d,ratio);
                }

                String result = decodeWatermark(d,Key2,parameters,Code,watermark.length());
                if(result.equals(watermark)){
                    System.out.println("success");
                }



                int count = 0;
                for(int i = 0;i<40;i++){
                    if(result.charAt(i) == watermark.charAt(i)){
                        count++;
                    }
                }

                all_res+=count;
                System.out.println(count);
            }
            System.out.println("attack ratio" + ratio + "WMR="+all_res/(wLen*10.0));
            all_res = 0;

        }
    }

    public void embeddingWatermarkforGeographicDeletionAttack() throws IOException, NoSuchAlgorithmException, CsvValidationException, SQLException {
        double[] ratios = new double[]{0.1,0.2,0.3,0.4,0.5, 0.6, 0.7, 0.8, 0.9, 0.95, 0.98, 0.99,0.99999};
        for(double ratio : ratios){
            String fileName = "geography.csv";
            String file  = "dataset\\geography.csv";
            int wLen = 10000;
            for(int c = 0;c<10;c++) {
                Data d = new Data(file);
                String watermark = generateWatermark(wLen);
                Vector<Parameters> parameters = new Vector<>();

                // numerator
                double numeratorInner = 0.5 * (1.0 - Math.sqrt(9.5481 / (wLen + 9.5481)));
                double numerator = Math.log(numeratorInner);

                // denominator
                double logBase = 1.0 - 1.0 / wLen;
                double denominator = Math.log(logBase);

                String Key2 = "西电";

                {//for eyewndr dataset
                    parameters.add(new Parameters("xidian", (int) (d.dataValue.get(0).size()/(numerator/denominator)),100));
                    parameters.add(new Parameters("jikeyuan", (int) (d.dataValue.get(1).size()/(numerator/denominator)),100));
                    parameters.add(new Parameters("xidianjikeyuan", (int) (d.dataValue.get(2).size()/(numerator/denominator)),100));
                }

                //data partition
                Vector<Vector<Vector<Integer>>> partitions = getAllPartitions(parameters,d,Key2);

                int totalGroups = 0;
                for(int i = 0;i<parameters.size();i++){
                    totalGroups+=parameters.get(i).getM();
                }

                Vector<StringBuffer> Code = getAllVeri(totalGroups,partitions,parameters,d,watermark);

                {
                      attack.deleteAttackwithOutSQL(fileName,ratio);
                      d = new Data("attacked database\\attack"+fileName);
                 }

                String result = decodeWatermark(d,Key2,parameters,Code,watermark.length());
                if(result.equals(watermark)){
                    System.out.println("success");
                }



                int count = 0;
                for(int i = 0;i<wLen;i++){
                    if(result.charAt(i) == watermark.charAt(i)){
                        count++;
                    }
                }

                all_res+=count;
                System.out.println(count);
            }
            System.out.println("attack ratio" + ratio + "WMR="+all_res/(wLen*10.0));
            all_res = 0;

        }
    }

    public void embeddingWatermarkforGeographicRandomAlterationAttack() throws IOException, NoSuchAlgorithmException, CsvValidationException, SQLException {
        double[] ratios = new double[]{0.1,0.2,0.3,0.4,0.5, 0.6, 0.7, 0.8, 0.9, 0.95, 0.98, 0.99,0.99999};
        for(double ratio : ratios){
            String fileName = "geography.csv";
            String file  = "dataset\\geography.csv";
            int wLen = 10000;
            for(int c = 0;c<10;c++) {
                Data d = new Data(file);
                String watermark = generateWatermark(wLen);
                Vector<Parameters> parameters = new Vector<>();

                // numerator
                double numeratorInner = 0.5 * (1.0 - Math.sqrt(9.5481 / (wLen + 9.5481)));
                double numerator = Math.log(numeratorInner);

                // denominator
                double logBase = 1.0 - 1.0 / wLen;
                double denominator = Math.log(logBase);

                String Key2 = "西电";

                {//for eyewndr dataset
                    parameters.add(new Parameters("xidian", (int) (d.dataValue.get(0).size()/(numerator/denominator)),100));
                    parameters.add(new Parameters("jikeyuan", (int) (d.dataValue.get(1).size()/(numerator/denominator)),100));
                    parameters.add(new Parameters("xidianjikeyuan", (int) (d.dataValue.get(2).size()/(numerator/denominator)),100));
                }

                //data partition
                Vector<Vector<Vector<Integer>>> partitions = getAllPartitions(parameters,d,Key2);

                int totalGroups = 0;
                for(int i = 0;i<parameters.size();i++){
                    totalGroups+=parameters.get(i).getM();
                }

                Vector<StringBuffer> Code = getAllVeri(totalGroups,partitions,parameters,d,watermark);

                {
                    attack.alterRandomAttackwithOutSQL(fileName,d,ratio);
                    d = new Data("attacked database\\attack"+fileName);
                }

                String result = decodeWatermark(d,Key2,parameters,Code,watermark.length());
                if(result.equals(watermark)){
                    System.out.println("success");
                }
                int count = 0;
                for(int i = 0;i<wLen;i++){
                    if(result.charAt(i) == watermark.charAt(i)){
                        count++;
                    }
                }

                all_res+=count;
                System.out.println(count);
            }
            System.out.println("attack ratio" + ratio + "WMR="+all_res/(wLen*10.0));
            all_res = 0;

        }
    }

    public void embeddingWatermarkforGeographicHighFreAlterationAttack() throws IOException, NoSuchAlgorithmException, CsvValidationException, SQLException {
        double[] ratios = new double[]{0.1,0.2,0.3,0.4,0.5, 0.6, 0.7, 0.8, 0.9, 0.95, 0.98, 0.99,0.99999};
        for(double ratio : ratios){
            String fileName = "geography.csv";
            String file  = "dataset\\geography.csv";
            int wLen = 10000;
            for(int c = 0;c<10;c++) {
                Data d = new Data(file);
                String watermark = generateWatermark(wLen);
                Vector<Parameters> parameters = new Vector<>();

                // numerator
                double numeratorInner = 0.5 * (1.0 - Math.sqrt(9.5481 / (wLen + 9.5481)));
                double numerator = Math.log(numeratorInner);

                // denominator
                double logBase = 1.0 - 1.0 / wLen;
                double denominator = Math.log(logBase);

                String Key2 = "西电";

                {//for eyewndr dataset
                    parameters.add(new Parameters("xidian", (int) (d.dataValue.get(0).size()/(numerator/denominator)),100));
                    parameters.add(new Parameters("jikeyuan", (int) (d.dataValue.get(1).size()/(numerator/denominator)),100));
                    parameters.add(new Parameters("xidianjikeyuan", (int) (d.dataValue.get(2).size()/(numerator/denominator)),100));
                }

                //data partition
                Vector<Vector<Vector<Integer>>> partitions = getAllPartitions(parameters,d,Key2);

                int totalGroups = 0;
                for(int i = 0;i<parameters.size();i++){
                    totalGroups+=parameters.get(i).getM();
                }

                Vector<StringBuffer> Code = getAllVeri(totalGroups,partitions,parameters,d,watermark);

                {
                    d = attack.alterFreAttackwithOutSQL(d,ratio);
                }

                String result = decodeWatermark(d,Key2,parameters,Code,watermark.length());
                if(result.equals(watermark)){
                    System.out.println("success");
                }
                int count = 0;
                for(int i = 0;i<wLen;i++){
                    if(result.charAt(i) == watermark.charAt(i)){
                        count++;
                    }
                }

                all_res+=count;
                System.out.println(count);
            }
            System.out.println("attack ratio" + ratio + "WMR="+all_res/(wLen*10.0));
            all_res = 0;
        }
    }

    public void embeddingWatermarkforGeographicPKAlterationAttack() throws IOException, NoSuchAlgorithmException, CsvValidationException, SQLException {
        double[] ratios = new double[]{0.1,0.2,0.3,0.4,0.5, 0.6, 0.7, 0.8, 0.9, 0.95, 0.98, 0.99,0.99999};
        for(double ratio : ratios){
            String fileName = "geography.csv";
            String file  = "dataset\\geography.csv";
            int wLen = 10000;
            for(int c = 0;c<10;c++) {
                Data d = new Data(file);
                String watermark = generateWatermark(wLen);
                Vector<Parameters> parameters = new Vector<>();

                // numerator
                double numeratorInner = 0.5 * (1.0 - Math.sqrt(9.5481 / (wLen + 9.5481)));
                double numerator = Math.log(numeratorInner);

                // denominator
                double logBase = 1.0 - 1.0 / wLen;
                double denominator = Math.log(logBase);

                String Key2 = "西电";

                {//for eyewndr dataset
                    parameters.add(new Parameters("xidian", (int) (d.dataValue.get(0).size()/(numerator/denominator)),100));
                    parameters.add(new Parameters("jikeyuan", (int) (d.dataValue.get(1).size()/(numerator/denominator)),100));
                    parameters.add(new Parameters("xidianjikeyuan", (int) (d.dataValue.get(2).size()/(numerator/denominator)),100));
                }

                //data partition
                Vector<Vector<Vector<Integer>>> partitions = getAllPartitions(parameters,d,Key2);

                int totalGroups = 0;
                for(int i = 0;i<parameters.size();i++){
                    totalGroups+=parameters.get(i).getM();
                }

                Vector<StringBuffer> Code = getAllVeri(totalGroups,partitions,parameters,d,watermark);

                {
                    d = attack.alterFreAttackwithOutSQL(d,ratio);
                }

                String result = decodeWatermark(d,Key2,parameters,Code,watermark.length());
                if(result.equals(watermark)){
                    System.out.println("success");
                }
                int count = 0;
                for(int i = 0;i<wLen;i++){
                    if(result.charAt(i) == watermark.charAt(i)){
                        count++;
                    }
                }

                all_res+=count;
                System.out.println(count);
            }
            System.out.println("attack ratio" + ratio + "WMR="+all_res/(wLen*10.0));
            all_res = 0;
        }
    }




/**
     * Data partitioning for a specific watermark attribute.
     * @param m     Partition count
     * @param key   Secret key : key1
     * @return partitions
     * @throws NoSuchAlgorithmException*/


    public Vector<Vector<Integer>> getPartitions(int m, Vector<String> values, String key) throws NoSuchAlgorithmException {//进行初始分区，m表示最终的分组数
        Vector<Vector<Integer>> vec = new Vector<Vector<Integer>>(m);
        for(int i = 0;i<m;i++){//Initialize m partitions
            Vector<Integer> partition = new Vector<Integer>();
            vec.add(partition);
        }

        for(int i = 0;i<values.size();i++){//遍历所有值，给分组，序号对应
            String str = values.get(i) + key;
            BigInteger bigInteger1 = Util.getMd5Result(str);// Hash(str[i]||SK1)
            str = bigInteger1.toString();
            str = key + str;
            BigInteger bigInteger = Util.getMd5Result(str);
            int partition = bigInteger.mod(BigInteger.valueOf(m)).intValue();
            vec.get(partition).add(i);//Place str[i] into the corresponding partition
        }
        return vec;
    }
/**
     * Perform data partitioning for each attribute separately
     * @param paras The set of all attribute partition numbers m and embedding intervals e.
     *             The parameters at index 2*i corresponds to the m of the i-th watermark attribute,
     *             and 2i+1 corresponds to the e of the i-th watermark attribute
     * @param d
     * @param key   SK1
     * @return   The index of the selected value in each partition.
     * @throws NoSuchAlgorithmException*/

    public Vector<Vector<Vector<Integer>>> getAllPartitions(Vector<Parameters> paras, Data d, String key) throws NoSuchAlgorithmException {
        Vector<Vector<Vector<Integer>>> vec = new Vector<>();
        for(int i = 0;i<d.dataFre.size();i++){
            vec.add(getPartitions(paras.get(i).getM(),d.dataValue.get(i),key));

        }
        return vec;
    }

/**
     * Randomly generate a watermark in the form of a fixed-length binary string
     * @param size   The length of watermark
     * @return*/


    public String generateWatermark(int size){
        StringBuffer str = new StringBuffer("");
        Random random = new Random();
        for(int i = 0;i<size;i++){
            str.append(random.nextInt(2));
        }

        String res = str.toString();
        return res;
    }

/**
     * Extract features from each group to generate masks, and use the masks for watermark embedding and extraction
     * @param len   length of watermark
     * @param vec   The indices of selected values for watermarking in this partition
     * @param key2
     * @param d
     * @param pos 正在处理哪个属性
     * @param watermark   In the embedding process, it refers to the watermark,
     *                    while in the extraction process, it refers to the verification information corresponding to the partition.
     * @return
     * @throws NoSuchAlgorithmException*/


    public StringBuffer getMaskCode(int len,Vector<Integer> vec,String key2,String watermark,Data d,int maxWeight, int pos,int gNum) throws NoSuchAlgorithmException {
        StringBuffer markCode = new StringBuffer(len);
        for(int i = 0;i<len;i++){//Initialize a mask string of length len filled with zeros

        }
        int[] freqy = new int[watermark.length()];
        int[] freqyTmp = new int[watermark.length()];
        for(int i = 0;i<vec.size();i++){//
            String str = d.dataValue.get(pos).get(vec.get(i))+ key2;
            BigInteger bigInteger1 = Util.getMd5Result(str);//Hash(vec[i]|| SK2)
            int b = Integer.parseInt(bigInteger1.mod(new BigInteger(len+"")).toString());
            str = key2+bigInteger1.toString();
            bigInteger1 = Util.getMd5Result(str);//Hash(vec[i] || SK2)
            int weight = Integer.parseInt(bigInteger1.mod(new BigInteger(maxWeight+"")).toString());
            freqy[b] = weight*d.dataFre.get(pos).get(vec.get(i));//计算频率
            freqyTmp[b] = freqy[b];
        }

        Arrays.sort(freqyTmp);
        double median = 0;
        if(watermark.length()%2==0){
            median = ((double)freqyTmp[freqyTmp.length/2-1]+freqyTmp[freqyTmp.length/2])/2;
        }
        else{
            median = freqyTmp[freqyTmp.length/2];
        }

        if(gNum%2==0){
            for(int i = 0;i<freqy.length;i++){//生成中位数感知哈希
                if(freqy[i]<=median){
                    markCode.append(0);
                    sumLessMedian++;
                    cou0++;
                }
                else{
                    markCode.append(1);
                    cou1++;
                }
            }
        }
        else{
            for(int i = 0;i<freqy.length;i++){//生成中位数感知哈希
                if(freqy[i]<median){
                    markCode.append(0);
                    sumLessMedian++;
                    cou0++;
                }
                else{
                    markCode.append(1);
                    cou1++;
                }
            }
        }



        return Util.xxor(markCode,watermark);//
    }



    public double compute_fitness(Data d, String key1,int m,int pos,int watermarklen,String key2,int maxWeight) throws NoSuchAlgorithmException {
        Vector<Vector<Integer>> partitions = getPartitions(m,d.dataValue.get(pos),key1);
        double diff = 0.0;
        int countMedian = 0;
        for(int j = 0;j<partitions.size();j++){
            int[] freqy = new int[watermarklen];
            int[] freqyTmp = new int[watermarklen];
            for(int i = 0;i<partitions.get(j).size();i++){//
                String str = d.dataValue.get(pos).get(partitions.get(j).get(i))+ key2;
                BigInteger bigInteger1 = Util.getMd5Result(str);//Hash(vec[i]|| SK2)
                int b = Integer.parseInt(bigInteger1.mod(new BigInteger(watermarklen+"")).toString());
                str = key2+bigInteger1.toString();
                bigInteger1 = Util.getMd5Result(str);//Hash(vec[i] || SK2)
                int weight = Integer.parseInt(bigInteger1.mod(new BigInteger(maxWeight+"")).toString());
                freqy[b] = weight*d.dataFre.get(pos).get(partitions.get(j).get(i));//计算频率
                freqyTmp[b] = freqy[b];
            }

            Arrays.sort(freqyTmp);
            double median = 0;
            if(watermarklen%2==0){
                median = ((double)freqyTmp[freqyTmp.length/2-1]+freqyTmp[freqyTmp.length/2])/2;
            }
            else{
                median = freqyTmp[freqyTmp.length/2];
            }

            double[] freDiff = new double[watermarklen];
            int sum = 0;
            for(int i = 0;i<freqy.length;i++){//计算与中位数的差值
                freDiff[i] = Math.abs(freqy[i] - median);

                sum+=freqy[i];
            }
            if(sum == 0){//一个组里啥也没有，证明分组太多了，太不均匀了肯定是，所有舍去
                diff = 0.0;
                return diff;
            }
            Arrays.sort(freDiff);

            double diffTmp = 0.0;
            int count = 1;//用于排名
            for(Double dou: freDiff){
                if(dou == 0){
                    countMedian++;
                    count++;
                }
                else{
                    diffTmp += Math.log(dou+1) * (1/count++);
                }
            }
            diff += diffTmp/sum;
        }
        if(countMedian>=0.01*m*watermarklen){//中位数过多，不考虑
            diff = 0.0;
        }
        //System.out.println(diff);
        return diff;
    }




    /**
     * Calculate the verification information for all partitions
     * @param num    The sum of the number of partitions for all watermark attributes
     * @param groups The indices of the selected values in each partition.
     * @param parameters   参数信息
     * @param d
     * @param watermark
     * @return    The verification information for all partitions
     * @throws NoSuchAlgorithmException
     */
    public Vector<StringBuffer> getAllVeri(int num,Vector<Vector<Vector<Integer>>> groups,Vector<Parameters> parameters,Data d,String watermark) throws NoSuchAlgorithmException{
        Vector<StringBuffer> res = new Vector<>(num);//Initialize the verification information set
        for(int i = 0;i<d.dataFre.size();i++){
            for(int j = 0;j<groups.get(i).size();j++){//按顺序处理，一个属性一个属性，一组一组
                res.add(getMaskCode(watermark.length(),groups.get(i).get(j),parameters.get(i).getKey2(),watermark,d,parameters.get(i).getMaxWeight(),i,j));
                //Calculate the verification bit string for the j-th partition of the i-th attribute

            }
        }
        return res;
    }


    /**
     * extract the watermark
     * @param d        Attacked dataset
     * @param key1     SK1
     * @param paras    Parameters stored locally (partition count m and embedding interval e)
     * @param veriinfor    The verification information for all partitions
     * @param len      The length of watermark
     * @return         The extracted result.
     * @throws NoSuchAlgorithmException
     */
    public String decodeWatermark(Data d, String key1,Vector<Parameters> paras,Vector<StringBuffer> veriinfor,int len) throws NoSuchAlgorithmException{
        int t = 0;//存储目前为止组数
        Vector<Vector<Vector<Integer>>> groups = getAllPartitions(paras,d,key1);
        Vector<StringBuffer> res = new Vector<>();
        for(int i = 0;i<groups.size();i++){
            for(int j = 0;j<groups.get(i).size();j++){
                res.add(getMaskCode(len,groups.get(i).get(j),paras.get(i).getKey2(), String.valueOf(veriinfor.get(t+j)),d,paras.get(i).getMaxWeight(),i,j));
            }
            t+=groups.get(i).size();
        }


        String str = "";
        int[] count0 = new int[len];//Below steps are for the majority vote.
        int[] count1 = new int[len];
        for(int i = 0;i<t;i++){
            for(int j = 0;j<len;j++){
                if(res.get(i).charAt(j) == '0'){
                    count0[j]++;
                }
                else{
                    count1[j]++;
                }
            }
        }

        for(int i = 0;i<len;i++){
            if(count1[i]>=count0[i]){
                str = str + "1";
            }
            else{
                str = str + "0";
            }
        }
        return str;
    }
}
