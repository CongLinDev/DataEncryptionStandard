package conglin.util;

public class DESUtil{
    public final static long MASK_6_BITS = 0xFC0000000000L;//6位掩码
    public final static int MASK_28_BITS = 0x0FFFFFFF;//28位掩码
    public final static long MASK_32_BITS = 0xFFFFFFFFL;//32位掩码
    public final static int  NUM_OF_ROUNDS = 16;//轮询次数
    /**
     * 初始置换
     */
    private final static byte[] INITIAL_PERMUTATION =
    {
        58, 50, 42, 34, 26, 18, 10, 2,
        60, 52, 44, 36, 28, 20, 12, 4,
        62, 54, 46, 38, 30, 22, 14, 6,
        64, 56, 48, 40, 32, 24, 16, 8,
        57, 49, 41, 33, 25, 17, 9,  1,
        59, 51, 43, 35, 27, 19, 11, 3,
        61, 53, 45, 37, 29, 21, 13, 5,
        63, 55, 47, 39, 31, 23, 15, 7
    };

    /**
     * 初始逆置换
     */
    private final static byte[] INITIAL_INVERSE_PERMUTATION =
    {
        40, 8, 48, 16, 56, 24, 64, 32,
        39, 7, 47, 15, 55, 23, 63, 31,
        38, 6, 46, 14, 54, 22, 62, 30,
        37, 5, 45, 13, 53, 21, 61, 29,
        36, 4, 44, 12, 52, 20, 60, 28,
        35, 3, 43, 11, 51, 19, 59, 27,
        34, 2, 42, 10, 50, 18, 58, 26,
        33, 1, 41, 9, 49, 17, 57, 25
    };

    /**
     * 置换选择1
     */
    private final static byte[] PERMUTATION_CHOICE_1 =
    {
        57, 49, 41, 33, 25, 17, 9,
        1,  58, 50, 42, 34, 26, 18,
        10, 2,  59, 51, 43, 35, 27,
        19, 11, 3,  60, 52, 44, 36,
        63, 55, 47, 39, 31, 23, 15,
        7,  62, 54, 46, 38, 30, 22,
        14, 6,  61, 53, 45, 37, 29,
        21, 13, 5,  28, 20, 12, 4
    };

    /**
     * 置换选择2
     */
    private final static byte[] PERMUTATION_CHOICE_2 =
    {
        14, 17, 11, 24, 1,  5,
        3,  28, 15, 6,  21, 10,
        23, 19, 12, 4,  26, 8,
        16, 7,  27, 20, 13, 2,
        41, 52, 31, 37, 47, 55,
        30, 40, 51, 45, 33, 48,
        44, 49, 39, 56, 34, 53,
        46, 42, 50, 36, 29, 32
    };

    /**
     * 生成子密钥，循环左移位数
     */
    private final static byte[] CIRCULAR_SHIFTS =
    {
        1, 1, 2, 2,
        2, 2, 2, 2,
        1, 2, 2, 2,
        2, 2, 2, 1
    };

    /**
     * 选择扩展运算
     */
    private static final byte[] E =
    {
        32, 1,  2,  3,  4,  5,
        4,  5,  6,  7,  8,  9,
        8,  9,  10, 11, 12, 13,
        12, 13, 14, 15, 16, 17,
        16, 17, 18, 19, 20, 21,
        20, 21, 22, 23, 24, 25,
        24, 25, 26, 27, 28, 29,
        28, 29, 30, 31, 32, 1
    };

    /**
     * S-Box
     */
    private final static byte[][][] S_BOX =
    {{
    	{ 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7 },
		{ 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8 },
		{ 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0 },
		{ 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 }
    },
    {
        { 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10 },
		{ 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5 },
		{ 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15 },
		{ 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 }
    },
    {
        { 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8 },
		{ 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1 },
		{ 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7 },
		{ 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 }
    },
    {
        { 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15 },
		{ 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9 },
		{ 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4 },
		{ 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 }
    },
    {
        { 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9 },
		{ 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6 },
		{ 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14 },
		{ 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 }
    },
    {
        { 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11 },
		{ 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8 },
		{ 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6 },
		{ 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 }
    },
    {
        { 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1 },
		{ 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6 },
		{ 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2 },
		{ 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 }
    },
    {
        { 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7 },
		{ 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2 },
		{ 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8 },
		{ 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
    }};

    private final static byte[] P_PERMUTATION =
    {
        16, 7,  20, 21,
        29, 12, 28, 17,
        1,  15, 23, 26,
        5,  18, 31, 10,
        2,  8,  24, 14,
        32, 27, 3,  9,
        19, 13, 30, 6,
        22, 11, 4,  25
    };

    ////////////////////////////////////////////////////////////////////////////////

    /**
     * 置换选择
     */
    private long genericPermutation(long input, byte[] indexTable, int inputLength){
        long output = 0;

        for (byte anIndexTable : indexTable) {
            int index = inputLength - anIndexTable;
            //(input >> index) & 1 获取input的index位的值
            output = (output << 1) | ((input >> index) & 1);
        }
        return output;
    }

    /**
     * 生成置换选择1
     */
    private long permutationChoice1(long input){
        return genericPermutation(input, PERMUTATION_CHOICE_1, 64);
    }

    /**
     * 生成置换选择2
     */
    private long permutationChoice2(long input){
        return genericPermutation(input, PERMUTATION_CHOICE_2, 56);
    }

    /**
     * 初始置换
     */
    public long initialPermutation(long input){
        return genericPermutation(input, INITIAL_PERMUTATION, 64);
    }

    /**
     * 初始逆置换
     */
    public long initialInversePermutation(long input){
        return genericPermutation(input, INITIAL_INVERSE_PERMUTATION, 64);
    }

    /**
     * 向左偏移
     */
    private int circularLeftShift(int input, int shift){
    	return ((input << shift) & MASK_28_BITS) | (input >> (28 - shift));
    }

    /**
     * 生成轮询子密钥
     */
    public long[] generateRoundSubkeys(long input){
        input = permutationChoice1(input);//PC1操作

        int halfC = (int) (input >> 28);//得到C0部分，也就是左半部分，高28位
        int halfD = (int) (input & MASK_28_BITS);//得到D0部分，也就是右半部分，低28位

        long[] roundSubkeys = new long[NUM_OF_ROUNDS];

        for (int i = 0; i < NUM_OF_ROUNDS; i++){
            halfC = circularLeftShift(halfC, CIRCULAR_SHIFTS[i]);
            halfD = circularLeftShift(halfD, CIRCULAR_SHIFTS[i]);
            //将两部分连接起来
            long joinedHalves = ((halfC & MASK_32_BITS) << 28) | (halfD & MASK_32_BITS);
            roundSubkeys[i] = permutationChoice2(joinedHalves);
        }

        return roundSubkeys;
    }

    ////////////////////////////////////////////////////////////////////////////////

    /**
     * 选择扩展运算E
     */
    private long expansionPermutation(int input){
        return genericPermutation(input, E, 32);
    }

    /**
     * 选择压缩运算S
     */
    private byte sBoxSubstitution(int sBoxNum, byte input){
        //6bit输入中的第1和第6组成的二进制数确定行
        byte row = (byte) (((input & 0x20) >> 4) | input & 0x01);
        //6bit输入中的第2、3、4、5组成的二进制数确定列
    	byte col = (byte) ((input & 0x1E) >> 1);
        return S_BOX[sBoxNum][row][col];
    }

    /**
     * 置换运算P
     */
    private int pPermutation(int input){
        return (int) genericPermutation(input, P_PERMUTATION, 32);
    }

    /**
     * DES核心函数
     */
    public int coreFunction(int input, long roundSubkey){
        //选择扩展运算
        long output = expansionPermutation(input);
        //与子密钥进行异或运算
        output ^= roundSubkey;
        //选择压缩运算
        int sBoxOutputs = 0;
        for (int i = 0; i < 8; i++){
            sBoxOutputs <<= 4;
            sBoxOutputs |= sBoxSubstitution(i, (byte) ((output & MASK_6_BITS) >> 42));
            output = output << 6;
        }
        
        return pPermutation(sBoxOutputs);
    }
}