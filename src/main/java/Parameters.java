public class Parameters {
    String key2;
    int m;//分区数,
    int maxWeight;//最大权重
    Parameters(String key,int m,int maxWeight){
        this.key2 = key;
        this.m = m;
        this.maxWeight = maxWeight;
    }
    Parameters(){}

    public String getKey2() {
        return key2;
    }

    public void setKey2(String key2) {
        this.key2 = key2;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(int maxWeight) {
        this.maxWeight = maxWeight;
    }
}
