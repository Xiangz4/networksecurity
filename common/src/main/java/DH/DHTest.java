package DH;

public class DHTest {
    public static int getRandomPrime() {
        int min = 1000;
        int max = 9999;
        int num = (int) (Math.random() * (max - min + 1)) + min;
        if (isPrime(num)) {
            return num;
        }
        return getRandomPrime();
    }
    private static boolean isPrime(int num) {
        if (num < 2) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }
    public static void main(String[] args) throws Exception {
        println("Step1: 生成可公开的公共加密因子p和q");
        int g = getRandomPrime();
        int p = getRandomPrime();
        System.out.println("生成可加密因子g和p分别为"+ g + "和" + p);
        System.out.println("生成各自私钥");
        int x = 28;
        int y = 25;
        System.out.println("A 的私钥为:"+x+"B的私钥为："+y);

        System.out.println("特征码计算");
        int p_x = (int) (Math.pow(g, x) % p);
        int p_y = (int) (Math.pow(g, y) % p);
        System.out.println("A的特征码为:"+p_x);
        System.out.println("B的特征码为:"+p_y);


        println("Step4: 交换特征码生成公共密钥");
        int p_x_y = (int) ((long) Math.pow(g, p_y) % p);
        int p_y_x = (int) ((long) Math.pow(g, p_x) % p);
        println("       A使用公式 p^p_y mod q 计算出对称密钥： ", p_x_y);
        println("       B使用公式 p^p_x mod q 计算出对称密钥： ", p_y_x);
        println("结论：通过这种方式，双方可以得到相同的对称加密的密钥，而在整个传输过程中，由于x和y是不传输的，\n所以即便其他所有信息都被别人获利，也无法计算出对称密钥（因为目前在数学上还无人可以解开此问题）。");
    }


    public static void println(Object... objs) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : objs) {
            sb.append(obj == null ? "" : obj.toString());
            sb.append("");
        }
        sb.append("\n");
        System.out.print(sb.toString());
    }
}
