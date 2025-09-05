import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import net.didion.jwnl.JWNLException;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Vector;

public class attack {
/*    public static void main(String[] args) throws CsvValidationException, SQLException, IOException {

        String databaseName = "ne.csv";//for QCEW
        //String databaseName = "geography.csv";//for geography
        ModifiedData d = new ModifiedData(databaseName,'a');//read in data

        ModifiedData tmp = attack.deleteAttackwithOutSQL(d);
        //Data tmp = attack.insertAttackWithoutSQL(d);
        Util.toCsv("attacked database\\" + "attack" + databaseName,tmp);//Store the attacked dataset in data.csv.(for deletion attack and insertion attack)

        //attack.alterattackWithFre(d);
        //attack.altAttackwithOutSQL(d);
        //attack.verticalModificationAttack(d,2);
        //Util.toCsv("attacked database\\" + "attack"+databaseName,d);//Store the attacked dataset in data.csv.(for modification attack)
    }*/
    static HashSet<Integer> select_value(Data d,int pos,double rate){
        //In sample attacks, the proportion indicated for deletion in the deletion attack,
        int tuple_num = (int)((1-rate)*d.dataValue.get(pos).size());

        HashSet<Integer> set = new HashSet<>();
        Random random = new Random();
        while(set.size()<tuple_num){
            int temp = random.nextInt(d.dataValue.get(pos).size());
            set.add(temp);
        }
        return set;
    }


    static HashSet<Integer> select_tuple(ModifiedData d,double rate){
        //In deletion attacks, the proportion indicated for deletion in the deletion attack,
        //In modification attacks, the proportion indicated for not modified in the modification attack

        //tuple_num is the number of tuples retained in the deletion attack and the number of tuples modified in the modification attack.
        int tuple_num = (int)((1-rate)*d.d.get(0).size());

        HashSet<Integer> set = new HashSet<>();
        Random random = new Random();
        while(set.size()<tuple_num){
            int temp = random.nextInt(d.d.get(0).size());
            set.add(temp);
        }
        return set;
    }

    /**
     * Simulate a deletion attack by deleting all tuples except for the selected tuples
     * @return the dataset containing only the remaining selected tuples
     * @throws SQLException
     * @throws CsvValidationException
     * @throws IOException
     */
    public static void deleteAttackwithOutSQL(String databaseName,double rate) throws SQLException, CsvValidationException, IOException {

        ModifiedData d = new ModifiedData(databaseName,'a');//read in data
        HashSet<Integer> set = select_tuple(d,rate);
        ModifiedData datatmp = new ModifiedData(d.d.size(),set.size());

        for(int i : set){//Delete all tuples except for the selected tuples
            for(int j = 0;j<d.d.size();j++){
                datatmp.d.get(j).add(d.d.get(j).get(i));//第j个属性修改为第i个元组的
            }
        }

        Util.toCsv("attacked dataset\\" + "attack" + databaseName,datatmp);
    }

    public static void deleteAttackwithOutSQL(String databaseName, Data data,double ratio) throws SQLException, CsvValidationException, IOException {

        ModifiedData d = new ModifiedData(data);//read in data
        HashSet<Integer> set = select_tuple(d,ratio);
        ModifiedData datatmp = new ModifiedData(d.d.size(),d.d.get(0).size());//read in data

        for(int i : set){//Delete all tuples except for the selected tuples
            for(int j = 0;j<d.d.size();j++){
                datatmp.d.get(j).add(d.d.get(j).get(i));
            }
        }

        Util.toCsv("attacked dataset\\" + "attack" + databaseName,datatmp);
    }


    public static void scalingAttack(Data d,double ratio){//攻击比例
        for(int i = 0;i<d.dataFre.size();i++){
            for(int j = 0;j<d.dataFre.get(i).size();j++){
                d.dataFre.get(i).set(j, (int) (Math.round(d.dataFre.get(i).get(j)*ratio)));
            }
        }
    }

    public static void sampleRatio(Data d, double ratio){//攻击比例

        for(int j = 0;j<d.dataFre.size();j++){
            HashSet<Integer> set = select_value(d,j,1-ratio);
            for(int i : set){//Delete all tuples except for the selected tuples
                d.dataFre.get(j).set(i,0);//第j个属性修改为第i个元组的
            }
        }

    }

    public static void alterRandomAttackwithOutSQL(String databaseName, Data data,double rate) throws SQLException, CsvValidationException, IOException {

        ModifiedData d = new ModifiedData(data);//read in data
        HashSet<Integer> set = select_tuple(d,1-rate);
        Random rand = new Random();

        for(int i : set){//Delete all tuples except for the selected tuples
            for(int j = 0;j<d.d.size();j++){
                d.d.get(j).set(i,Util.getRandomAlphaString(4+rand.nextInt(6)));//第j个属性修改为第i个元组的
            }
        }

        Util.toCsv("attacked database\\" + "attack" + databaseName,d);
    }

    public static Data alterFreAttackwithOutSQL(Data d,double ratio) throws SQLException, CsvValidationException, IOException {
        Random rand = new Random();
        double r = 0.99;
        d.sortVectors();
        for(int i = 0;i<d.dataFre.size();i++){
            int count = 0;
            int tmp = 0;
            while((double)count/d.All_size<r){
                count += d.dataFre.get(i).get(tmp);
                d.dataValue.get(i).set(tmp,Util.getRandomAlphaString(4+rand.nextInt(6)));
                tmp++;
            }
        }


        return d;
    }


    public static Data perbAttack(Data d,Integer perb) throws SQLException, CsvValidationException, IOException {
        Random rand = new Random();

        d.sortVectors();
        for(int i = 0;i<d.dataFre.size();i++){
            for(int j = 0;j<d.dataFre.get(i).size();j++){
                d.dataFre.get(i).set(j,d.dataFre.get(i).get(j)+perb);
            }
        }


        return d;
    }

    public static Data alterRandomAttackwithOutSQL(Data d,double rate) throws SQLException, CsvValidationException, IOException {
        Random rand = new Random();
        Vector<Integer> vec= new Vector<>();
        for(int i = 0;i<d.dataValue.get(0).size();i++){
            vec.add(i);
        }
        Collections.shuffle(vec, rand);
        int count = 0;
        for(int i:vec){

            count += d.dataFre.get(0).get(i);

            if((double)count/d.All_size>rate){
                count -= d.dataFre.get(0).get(i);
                continue;
            }
            d.dataValue.get(0).set(i,Util.getRandomAlphaString(4+rand.nextInt(6)));

        }
        return d;
    }

    public static void altAttackwithPK(Data data,String databaseName,double rate) throws SQLException, CsvValidationException, IOException {
        ModifiedData d = new ModifiedData(data);//read in data
        HashSet<Integer> set = select_tuple(d,1-rate);//The tuples to be modified
        Random random = new Random();
        for(int i : set){
            d.d.get(2).set(i, String.valueOf(random.nextInt(1000000000)));

        }
        Util.toCsv("attacked database\\" + "attack" + databaseName,d);
        return;
    }

    public static void altAttackwithPK1(Data data,String databaseName,double rate) throws SQLException, CsvValidationException, IOException {
        ModifiedData d = new ModifiedData(data);//read in data
        HashSet<Integer> set = select_tuple(d,1-rate);//The tuples to be modified
        Random random = new Random();
        for(int i : set){
            d.d.get(0).set(i,Util.getRandomAlphaString(10));
            d.d.get(1).set(i,Util.getRandomAlphaString(10));
            d.d.get(2).set(i,Util.getRandomAlphaString(10));
        }
        Util.toCsv("attacked dataset\\" + "attack" + databaseName,d);
        return;
    }

    static HashSet<Integer> select_insetred_tuple(ModifiedData d, double rate){
        //In deletion attacks, the proportion indicated for deletion in the deletion attack,
        //In modification attacks, the proportion indicated for not modified in the modification attack

        //tuple_num is the number of tuples retained in the deletion attack and the number of tuples modified in the modification attack.
        int tuple_num = (int)(rate*50000);

        HashSet<Integer> set = new HashSet<>();
        Random random = new Random();
        while(set.size()<tuple_num){
            int temp = 50000+random.nextInt(d.d.get(0).size()-50000);
            set.add(temp);
        }
        return set;
    }




    public static void insertAttackWithoutSQL(String databaseName,double rate) throws SQLException, CsvValidationException, IOException {
        ModifiedData d = new ModifiedData("dataset\\"+databaseName,'a',1);//read in data
        int tmp = (int)(50000 * rate);//The number of tuples to be inserted

        ModifiedData datatmp = new ModifiedData(d.d.size(),d.d.get(0).size()+tmp);
        Random random = new Random();



        for(int i=0;i<50000;i++){//Copy the original database into the attacked database
            for(int j = 0;j<d.d.size();j++){
                datatmp.d.get(j).add(d.d.get(j).get(i));
            }
        }

        HashSet<Integer> set = select_insetred_tuple(d,rate);

        for(int i : set){
            for(int j = 0;j<d.d.size();j++){
                datatmp.d.get(j).add(d.d.get(j).get(i));
            }
        }

        Util.toCsv("attacked dataset\\" + "attack" + databaseName,datatmp);
    }

    public static void alterAttack(String databaseName,double rate) throws SQLException, IOException, JWNLException, CsvValidationException, JWNLException {
        ModifiedData d = new ModifiedData("dataset\\"+databaseName,'a',50000);//read in data
        HashSet<Integer> set = select_tuple(d,rate);
        SemanticUtils utils = new SemanticUtils();
        for(int i : set){//Delete all tuples except for the select
            utils.synonymSubstitution(d,i);
            d.d.get(0).set(i,Util.getRandomAlphaString(10));
            d.d.get(1).set(i,Util.getRandomAlphaString(10));
            d.d.get(2).set(i,Util.getRandomAlphaString(10));

        }
        Util.toCsv("attacked database\\" + "attack" + databaseName,d);
    }


}

class ModifiedData{
    Vector<Vector<String>> d;//Record the data for each attribute

    public ModifiedData(String str, char ch) throws SQLException, IOException, CsvValidationException {
        Reader reader = Files.newBufferedReader(Paths.get(str));
        CSVReader csvReader = new CSVReader(reader);
        String[] record;
        record = csvReader.readNext();

        d = new Vector<>(record.length);
        for(int i = 0;i< record.length;i++){//Initialize d
            d.add(new Vector<>());
        }

        while ((record = csvReader.readNext()) != null) {
            for(int t = 0; t<record.length;t++){
                d.get(t).add(record[t]);
            }
        }
    }

    public ModifiedData(Data data) throws SQLException, IOException, CsvValidationException {

        d = new Vector<>(data.dataFre.size());
        for(int i = 0;i< data.dataFre.size();i++){//Initialize d
            d.add(new Vector<>());
        }

        for(int i = 0 ;i< data.dataFre.size();i++){
            int tmp = 0;
            for(int j = 0;j<data.dataFre.get(i).size();j++){
                for(int k = 0;k<data.dataFre.get(i).get(j);k++) {
                    d.get(i).add(data.dataValue.get(i).get(j));
                }
            }
        }
    }

    public ModifiedData(String str, char ch, int num) throws SQLException, IOException, CsvValidationException {
        Reader reader = Files.newBufferedReader(Paths.get(str));
        CSVReader csvReader = new CSVReader(reader);
        String[] record;
        record = csvReader.readNext();
        int[] Attrs = {1,2,8,9};

        d = new Vector<>(Attrs.length);
        for(int i = 0;i< Attrs.length;i++){//Initialize d
            d.add(new Vector<>());
        }

        while ((record = csvReader.readNext()) != null) {
            for(int t = 0; t<Attrs.length;t++){
                d.get(t).add(record[Attrs[t]]);
            }
        }
    }



    /**
     * Initialize the dataset for the deletion attack and the insertion attack
     * @param size     The number of watermark attributes
     * @param length   The number of tuples
     */
    public ModifiedData(int size,int length){
        d = new Vector<>(size);
        for(int i = 0;i<size;i++){
            d.add(new Vector<>(length));
        }
    }
}
