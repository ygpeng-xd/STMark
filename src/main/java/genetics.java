import io.jenetics.Genotype;
import io.jenetics.IntegerChromosome;
import io.jenetics.IntegerGene;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.Limits;
import io.jenetics.util.Factory;
import io.jenetics.util.IntRange;

import java.security.NoSuchAlgorithmException;
import java.util.Vector;

/**
 * Parameter optimization through a genetic algorithm
 */

public class genetics {
    static Data d;
    static int watlen;//The length of watermark
    static int index;//Current attribute index
    static String key1;


/**
     * 2.fitness fuction
     * @param gt
     * @return*/


    private static Double eval(Genotype<IntegerGene> gt){
        IntegerChromosome chromosome = (IntegerChromosome) gt.getChromosome(0);
        String key2 = "jikeyuan";
        /*for(IntegerGene integerGene : chromosome){
            key2+=(char)integerGene.intValue();
        }*/
        //int i2 = gt.getChromosome(1).getGene().intValue();

        int i1 = gt.getChromosome(0).getGene().intValue();
        int i2 = gt.getChromosome(1).getGene().intValue();
        Watermark wm = new Watermark();
        double res = 0;
        try{
           // res =  wm.compute_fitness(d,key1, (d.dataFre.get(index).size()/(int)(watlen*1.04)),index,watlen,key2,i2);
            res =  wm.compute_fitness(d,key1, i1,index,watlen,key2,i2);
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return res;
    }



/**
     * Optimize the parameters of each attribute using genetic algorithms
     * @param data
     * @param len length of watermark
     * @return*/


    public static Vector<Parameters> getGene(Data data, int len){
        d = data;
        watlen = len;
        Vector<Parameters> vec = new Vector<>(d.dataFre.size());
        for(int i = 0;i<d.dataFre.size();i++){
            index = i;

            // 1.) Define the genotype (factory) suitable
            //     for the problem.
            Factory<Genotype<IntegerGene>> gtf =
                    Genotype.of(//IntegerChromosome.of(65,90,IntRange.of(6,15)),//key
                            IntegerChromosome.of(5,1000),//Range of max weight
                            IntegerChromosome.of(5,1000));//Range of max weight

            // 3.) Create the execution environment.
            Engine<IntegerGene, Double> engine = Engine
                    .builder(genetics::eval, gtf)//The default population size is 50
                    .maximizing()
                    .build();

            // 4.) Start the execution (evolution) and
            //     collect the result.
            Genotype<IntegerGene> result = engine.stream()
                    .limit(500)//the max iterations of
                    .collect(EvolutionResult.toBestGenotype());


            //Save the parameters of the watermark attributes:
            //the parameters at index 2*i corresponds to the m of the i-th watermark attribute,
            //the parameters at index 2i+1 corresponds to the e of the i-th watermark attribute
            //IntegerChromosome keyChromosome = (IntegerChromosome)result.getChromosome(0);
            System.out.println(eval(result));
           /* String key2 = "";
            for(IntegerGene integerGene : keyChromosome){
                key2+=(char)integerGene.intValue();
            }*/
            Parameters parameters = new Parameters();
            parameters.setKey2("jikeyuan");
            //parameters.setM((d.dataFre.get(index).size()/(int)(watlen*1.04)));
            parameters.setM(result.getChromosome(0).getGene().intValue());
            parameters.setMaxWeight(result.getChromosome(1).getGene().intValue());
            vec.add(parameters);
        }


        return vec;
    }
}
