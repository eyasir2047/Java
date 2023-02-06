public class MatrixAddition {
    public static int [][]addMatrices(int [][]matrix1, int [][] matrix2){
        int [][]sum= new int [matrix1.length][matrix1[0].length];

        for(int i=0;i< matrix1.length;i++){
            for(int j=0;j<matrix1[0].length;j++){
                sum[i][j]=matrix1[i][j]+matrix2[i][j];
            }
        }
        return sum;
    }

    public static void main(String []args){
        int [][]matrix1={{1,2,3},{4,5,6}};
        int [][]matrix2={{7,8,9},{10,11,12}};

        int [][]sum= addMatrices(matrix1, matrix2);

        for(int i=0;i<sum.length;i++){
            for(int j=0;j<sum[0].length;j++){
                System.out.print(sum[i][j]+" ");
            }
            System.out.println();
        }

    }
}
