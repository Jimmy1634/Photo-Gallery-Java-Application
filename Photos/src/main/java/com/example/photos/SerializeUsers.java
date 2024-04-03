package application;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SerializeUsers implements Serializable{

    public static void SerializeAllUsers(){

        try{
            FileOutputStream fileOut = new FileOutputStream("Users.dat");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(AdminController.nonAdminUsers);
            out.close();
            fileOut.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static List<User> DeserializeAllUsers(){
        List<User> deserializedUsers = new ArrayList<>();
        try {
            FileInputStream fileIn = new FileInputStream("Users.dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            List<User> temp = (List<User>) in.readObject();
            deserializedUsers.addAll(temp);
            in.close();
            fileIn.close();
        }
        catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return deserializedUsers;
    }

}
