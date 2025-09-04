import com.opencsv.exceptions.CsvValidationException;
import org.junit.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class test {
    @Test
    public void test_eyewndr() throws CsvValidationException, SQLException, IOException, NoSuchAlgorithmException {
        Watermark wm = new Watermark();
        //wm.embeddingWatermarkforEyewndrPerbatationAttack();//frequency perturbation attack
        //wm.embeddingWatermarkforEyewndrScalingAttack();//scaling attack
        wm.embeddingWatermarkforEyewndrDeletionAttack();//deletion attack
        //wm.embeddingWatermarkforEyewndrValueReplacement();//Value Repalce Attack
    }

    @Test
    public void test_geographic() throws CsvValidationException, SQLException, IOException, NoSuchAlgorithmException {
        Watermark wm = new Watermark();
        //wm.embeddingWatermarkforGeographicDeletionAttack();//deletion attack
        wm.embeddingWatermarkforGeographicRandomAlterationAttack();//Random Alteration Attack,
    }
}
