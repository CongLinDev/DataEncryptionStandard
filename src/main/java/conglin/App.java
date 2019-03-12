package conglin;

import conglin.format.FileFormat;
import conglin.service.DESService;
import conglin.service.DESServiceImpl;

import java.util.Scanner;

public class App 
{
    private static FileFormat fileFormat;
    private static long newText;
    private static long keys[];
    public static void main( String[] args){
        DESService desService = new DESServiceImpl();

        Scanner scanner =new Scanner(System.in);
        boolean isOver = false;

        while(!isOver){
            System.out.print(">");
            String []command = scanner.nextLine().split("\\s+");

            switch (command[0].toLowerCase()){
                case "encrypt":{
                    fileFormat = FileFormat.readFile(command[1]);
                    keys = fileFormat.getKeys();
                    for(int i = 0; i < keys.length; i++){
                        newText = desService.encrypt(fileFormat.getText(), keys[i]);
                        fileFormat.setText(newText);
                    }
                    break;
                }
                case "decrypt":{
                    fileFormat = FileFormat.readFile(command[1]);
                    keys = fileFormat.getKeys();
                    for(int i = keys.length - 1; i > -1; i--){
                        newText = desService.decrypt(fileFormat.getText(), keys[i]);
                        fileFormat.setText(newText);
                    }
                    break;
                }
                case "output":{
                    FileFormat.writeFile(fileFormat, command[1]);
                    System.out.println("Output is done.");
                    break;
                }
                case "exit":{
                    isOver = true;
                    break;
                }
                default:{
                    System.out.println("ERROR COMMAND.");
                    break;
                }
            }
        }

    }


}
