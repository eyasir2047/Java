import java.util.Scanner;
import java.io.*;
public class E 
{
    public static int[][] add(int mat1[][],int mat2[][],int mat3[][])
    {
        int ans[][]=new int [2][3];
        for(int i=0;i<2;i++)
        {
            for(int j=0;j<3;j++)
            {
                ans[i][j]=mat1[i][j]+mat2[i][j]+mat3[i][j];
            }
        }
        return ans;
    }
    public static void main(String[] args) 
    {
        Scanner sc = new Scanner(System.in);
        int mat1[][]={{1,2,3},{4,5,6}};
        int mat2[][]={{11,12,13},{15,16,17}};
        int mat3[][]={{20,21,22},{30,31,32}};
        int ans[][]=add(mat1,mat2,mat3);
        for(int i=0;i<2;i++)
        {
            for(int j=0;j<3;j++)
            {
                System.out.print(ans[i][j]+" ");
            }
            System.out.println("");
        }
    }
}
