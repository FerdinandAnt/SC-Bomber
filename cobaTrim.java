import java.util.Scanner;

public class cobaTrim
{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);   
            String input = "[  xcx  ][  xxx  ][  sad  ]";
            
            // Read board state
            // Read until "END" is detected
           input = input.replace(" ","");
           input = input.replace("]","");
           input = input.replace("["," ");
           String [] inputarray = input.split(" ");
           System.out.println(inputarray[1]);
    }
}
