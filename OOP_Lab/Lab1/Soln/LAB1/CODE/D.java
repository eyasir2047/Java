import java.util.Scanner;
public class D 
{
    public static void main(String[] args) 
    {
        Scanner sc = new Scanner(System.in);
        int mat1[][]={{1,2,3},{4,5,6}};
        int mat2[][]={{11,12,13},{15,16,17}};
        int matans[][] = new int [2][3];
        for(int i=0;i<2;i++)
        {
            for(int j=0;j<3;j++)
            {
                matans[i][j]=mat1[i][j]+mat2[i][j];
            }
        }
        for(int i=0;i<2;i++)
        {
            for(int j=0;j<3;j++)
            {
                System.out.print(matans[i][j]+" ");
            }
            System.out.println("");
        }
    }
}
