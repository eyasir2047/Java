import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Problem1 {
    public static void main(String[] args) {
        ArrayList<String>arr = new ArrayList<String>();
        String got = "";
        BufferedReader bufferedReader = null;
        try {
            String str;
            bufferedReader = new BufferedReader(new FileReader("Readerfile.txt"));
            int x = 0;
            while ((str = bufferedReader.readLine()) != null) {
                arr.add(str);
                System.out.println(str);
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                if (bufferedReader != null)
                    bufferedReader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        try{
            FileWriter fileWriter=new FileWriter("WriterFile.txt");
            for(int i=arr.size()-1;i>=0;i--)
            {
                fileWriter.write(arr.get(i)+"\n");
            }
            fileWriter.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
