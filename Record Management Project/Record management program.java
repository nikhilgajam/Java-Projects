import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;

public class Jp {

    public static void main(String[] args) {

        System.out.println("Record Management Program\n");

        Scanner in = new Scanner(System.in);

        Record r = new Record();

        System.out.println("1. Add a new record");
        System.out.println("2. Modify an existing record");
        System.out.println("3. Delete an existing record");
        System.out.println("4. Search records");
        System.out.println("5. Display all records");
        System.out.println("0. Exit the program");

        while (true){

            System.out.print("\nEnter: ");
            String choice = in.nextLine();
            System.out.println();

            switch (choice){
                case "1":
                    r.append();
                    break;

                case "2":
                    r.modify();
                    break;

                case "3":
                    r.delete();
                    break;

                case "4":
                    r.search();
                    break;

                case "5":
                    r.display_all();
                    break;

                case "0":
                    System.out.println("Thanks for using");
                    return;

                default:
                    System.out.println("Please Enter Mentioned Numbers Only");
            }
        }
    }
}

class Record{

    public void append(){
        System.out.println(">>> Append");

        Scanner in = new Scanner(System.in);

        File r_name = new File("Record.csv");

        try{
            FileWriter w = new FileWriter(r_name, true);

            System.out.print("Enter Roll No: ");
            String rno = in.nextLine();
            System.out.print("Enter Name: ");
            String name = in.nextLine();
            System.out.print("Enter Age: ");
            String age = in.nextLine();

            if(r_name.length() == 0){
                w.write("Roll No, Name, Age\n");
            }

            w.write(rno + ", " + name + ", " + age + "\n");
            w.close();
        }
        catch (Exception ex){
            System.out.println("There is an error: Check your input");
        }
    }


    public void modify(){
        System.out.println(">>> Modify");

        Scanner in = new Scanner(System.in);

        File r_name = new File("Record.csv");
        File temp = new File("temp.csv");

        if(!(r_name.exists())){
            System.out.println("\nRecord.csv not found");
            return;
        }

        String rno, name, age;
        int i = 0;
        System.out.print("Enter Roll No To Modify: ");
        rno = in.nextLine();

        try{
            FileReader r = new FileReader(r_name);
            BufferedReader br = new BufferedReader(r);
            FileWriter tw = new FileWriter(temp, true);

            String data;

            data = br.readLine();
            while(data != null){

                boolean a = data.startsWith(rno);

                if(a){
                    System.out.println("\nExisting Data: ");
                    System.out.println("Roll No, Name, Age");
                    System.out.println(data);

                    System.out.print("\nEnter Roll No: ");
                    rno = in.nextLine();
                    System.out.print("Enter Name: ");
                    name = in.nextLine();
                    System.out.print("Enter Age: ");
                    age = in.nextLine();

                    tw.write(rno + ", " + name + ", " + age + "\n");

                    i++;
                }else{
                    tw.write(data + "\n");
                }

                data = br.readLine();

            }

            r.close();
            tw.close();
            r_name.delete();

            FileWriter w = new FileWriter(r_name, true);
            FileReader tr = new FileReader(temp);
            BufferedReader tbr = new BufferedReader(tr);

            data = tbr.readLine();
            while(data != null) {
                w.write(data + "\n");
                data = tbr.readLine();
            }

            if(i == 0){
                System.out.println(rno + "not found");
            }

            w.close();
            r.close();
            br.close();
            tbr.close();

            temp.delete();

            System.out.println("\nModified Successfully");

        }
        catch (NullPointerException e){
            System.out.print("");
        }catch (Exception ex){
            System.out.print("There is an error: Check your input");
        }
    }


    public void delete(){
        System.out.println(">>> Delete");

        Scanner in = new Scanner(System.in);

        File r_name = new File("Record.csv");
        File temp = new File("temp.csv");

        if(!(r_name.exists())){
            System.out.println("\nRecord.csv not found");
            return;
        }

        String rno, name, age;
        int i = 0;
        System.out.print("Enter Roll No To Delete: ");
        rno = in.nextLine();

        try{
            FileReader r = new FileReader(r_name);
            BufferedReader br = new BufferedReader(r);
            FileWriter tw = new FileWriter(temp, true);

            String data;

            data = br.readLine();
            while(data != null){

                boolean a = data.startsWith(rno);

                if(a){
                    System.out.println("\nExisting Data: ");
                    System.out.println("Roll No, Name, Age");
                    System.out.println(data);

                    i++;
                }else{
                    tw.write(data + "\n");
                }

                data = br.readLine();

            }

            r.close();
            tw.close();
            r_name.delete();

            FileWriter w = new FileWriter(r_name, true);
            FileReader tr = new FileReader(temp);
            BufferedReader tbr = new BufferedReader(tr);

            data = tbr.readLine();
            while(data != null) {
                w.write(data + "\n");
                data = tbr.readLine();
            }

            if(i == 0){
                System.out.println(rno + "not found");
            }

            w.close();
            r.close();
            br.close();
            tbr.close();

            temp.delete();

            System.out.println("\nDeleted Successfully");

        }
        catch (NullPointerException e){
            System.out.print("");
        }catch (Exception ex){
            System.out.print("There is an error: Check your input");
        }
    }


    public void search() throws NullPointerException{
        System.out.println(">>> Search");

        Scanner in = new Scanner(System.in);

        File r_name = new File("Record.csv");

        if(!(r_name.exists())){
            System.out.println("\nRecord.csv not found");
            return;
        }

        String rno;
        int i = 0;
        System.out.print("Enter Roll No To Search: ");
        rno = in.nextLine();

        try{
            FileReader r = new FileReader(r_name);
            BufferedReader br = new BufferedReader(r);
            String data;

            data = br.readLine();
            while(data != null){
                data = br.readLine();

                boolean b = data.startsWith(rno);

                if(b){
                    System.out.println("\nRoll No, Name, Age");
                    System.out.println(data);
                    i++;
                }
            }

            if(i == 0){
                System.out.println(rno + " not found");
            }

            r.close();
        }
        catch (NullPointerException e){
            System.out.print("");
        }catch (Exception ex){
            System.out.print("There is an error: Check your input");
        }

    }


    public void display_all(){
        System.out.println(">>> Display All");

        File r_name = new File("Record.csv");

        if(!(r_name.exists())){
            System.out.println("\nRecord.csv not found");
            return;
        }

        if(r_name.length() <= 20){
            System.out.println("\nNo Records To Display");
            return;
        }

        int i = -1;

        try{
            FileReader r = new FileReader(r_name);
            BufferedReader br = new BufferedReader(r);
            String data;

            data = br.readLine();
            while(data != null){
                System.out.println(data);
                data = br.readLine();
                i++;
            }

            r.close();

            System.out.println("\nTotal no. of records: " + i);

        }
        catch (Exception ex){
            System.out.println("There is an error: Check your input");
        }
    }

}
